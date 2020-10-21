import generated.KVStore;
import generated.KVStoreServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import javafx.util.Pair;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MapReduceMasterCoordinator {

    ConcurrentMap<String, String> tasks = new ConcurrentHashMap<String, String>();
    private String kVStoreIp ;
    private int kVStorePort;
    private String masterIP;
    private int masterPort;
    private String fileLocation;
    private int mapperCount;
    private int reducerCount;
    private String mapperFunction;
    private String reducerFunction;
    private String outputFile;

    private static final String inputKeyAppender = "input_";
    private static final String mapperKeyIdentifier = "M_";
    private static final String reducerKeyIdentifier = "R_";

    private ConcurrentMap<String,String>  mapperTaskStatus;
    private ConcurrentMap<String,String>  reducerTaskStatus;
    static int counter=0;

    static Logger log = Logger.getLogger(MapReduceMasterCoordinator.class);


    public MapReduceMasterCoordinator(String kVStoreIp,int kVStorePort,String fileLocation,int mapperCount,int reducerCount,String masterIP,int masterPort, String mapperFunction, String reducerFunction,String outputFile){
        this.kVStoreIp=kVStoreIp;
        this.kVStorePort=kVStorePort;
        this.masterIP=masterIP;
        this.masterPort=masterPort;
        this.fileLocation=fileLocation; //source file location (directory)
        this.mapperCount=mapperCount;
        this.reducerCount=reducerCount;
        this.mapperFunction = mapperFunction;
        this.reducerFunction = reducerFunction;
        this.outputFile = outputFile;

        mapperTaskStatus = new ConcurrentHashMap();
        reducerTaskStatus= new ConcurrentHashMap();

    }


    public void mapReduce()  {
        log.debug("File path that master received is"+ fileLocation);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(kVStoreIp, kVStorePort).usePlaintext().build();
        KVStoreServiceGrpc.KVStoreServiceBlockingStub blockingStub = KVStoreServiceGrpc.newBlockingStub(channel);

        // Create splits
        List<String> inputFiles=null;
        inputFiles=Stream.of(new File(fileLocation).listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toList());
        List<String> totalKeys = new ArrayList();
        List<List<String>> splittedKey = new ArrayList<>();
        for(String file: inputFiles) {
            List<String[]> splits=createSplits(fileLocation+"\\"+file);
            List<String> storedInputData=storeInputSplits(splits, file, blockingStub);
            for (String storedInputDatum : storedInputData) {
                totalKeys.add(storedInputDatum);
            }
            splittedKey.add(totalKeys);
        }

        Set<String> allSplittedKeys =
                splittedKey.stream()
                        .flatMap(List::stream)
                        .collect(Collectors.toSet());
        List<String> keys=new ArrayList<>();
        keys.addAll(allSplittedKeys);
            distributeKeysChunksToMappers(keys,mapperCount, blockingStub, mapperFunction);
    }

    public List<String[]> createSplits(String fileLocation){
        List<String[]> splits=null;
        try {
                splits = Files.lines(Paths.get(fileLocation)).map(l->l.split(" ")).collect(Collectors.toList());
                for(String s[]: splits){
                    log.debug(Arrays.toString(s));
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return splits;
    }


    public List<String> storeInputSplits(List<String[]> splits,String fileName, KVStoreServiceGrpc.KVStoreServiceBlockingStub blockingStub){
        int count=0;
        List<String> split=new ArrayList<>();
        for(String s[]:splits){
            writeToFile(Arrays.toString(s),fileLocation+"\\"+inputKeyAppender+fileName+"_"+count);
            split.add(fileLocation+"\\"+inputKeyAppender+fileName+"_"+count);
            count++;
        }
        return split;
    }

    public void distributeKeysChunksToMappers(List<String> inputSplitKeys, int mapperCount,KVStoreServiceGrpc.KVStoreServiceBlockingStub blockingStub, String mapperFunction) {
        HashMap<String, String> mapperData = new HashMap();
        for(String inputSplitKey: inputSplitKeys){
            if(counter==mapperCount) {
                counter = 0;
            }
            List<String> valuesToAdd= new ArrayList<>();
            valuesToAdd.add(inputSplitKey);
            if(!mapperData.containsKey(mapperKeyIdentifier+counter)){
                mapperData.put(mapperKeyIdentifier+counter,inputSplitKey);
            }else {
                mapperData.replace(mapperKeyIdentifier + counter, mapperData.get(mapperKeyIdentifier + counter) + ", " + inputSplitKey);
            }
            counter++;
        }

        int i=0;

        List<String> mapperKeys= new ArrayList<>();
        for(Map.Entry<String, String> s : mapperData.entrySet()){
           blockingStub.set(KVStore.SetRequest.newBuilder().setKey(mapperKeyIdentifier+i).setValue(mapperData.get(mapperKeyIdentifier+i).toString()).build());
           mapperKeys.add(mapperKeyIdentifier+i);
           i++;
        }


        for(String s: mapperKeys){
            assignKeyChunkToMapper(mapperFunction,s,blockingStub,mapperTaskStatus,reducerCount);
        }


        // start reducers
        log.debug(Arrays.asList(mapperTaskStatus));

        while(true){
            if(mapperTaskStatus.values().stream().allMatch("completed"::equals)){
                log.debug("Mappers status"+Arrays.asList(mapperTaskStatus));
                System.out.println("starting reducers");
                for(int redInstance=0;redInstance<reducerCount;redInstance++){
                    new Thread(new ReducerProcessGenerator(reducerFunction,reducerKeyIdentifier+redInstance, reducerTaskStatus,reducerCount,kVStoreIp,kVStorePort,outputFile,fileLocation)).start();
                }
                break;
            }
        }

    }

    public void assignKeyChunkToMapper(String mapperFunction,String mapperKey, KVStoreServiceGrpc.KVStoreServiceBlockingStub blockingStub,ConcurrentMap mapperTaskStatus,int reducerCount){
        mapperTaskStatus.put(mapperKey,"inprocess");
        //List<MapperProcessGenerator> mapperProcessList= new ArrayList<>();
       new Thread(new MapperProcessGenerator(mapperFunction,mapperKey, mapperTaskStatus,reducerCount, kVStoreIp,kVStorePort,outputFile,fileLocation)).start();
       // return mapperProcessList;
    }
    public static void writeToFile(String output, String file){
        try{
            File outputFile = new File(file);
            BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile.getAbsoluteFile(), false));
            bw.write(output);
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
