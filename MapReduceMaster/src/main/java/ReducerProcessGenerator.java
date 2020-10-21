import generated.KVStore;
import generated.KVStoreServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class ReducerProcessGenerator implements  Runnable  {
    String name;
    String args;
    ConcurrentMap mapperTaskStatus;
    int reducerCount;
    String kvStoreIP;
    int kvStorePort;
    String outputFile;
    String inputFileLocation;

    private static final String intermediateKeyIdentifier = "Intermediate_";
    private static final String reducerKeyIdentifier = "R_";
    ConcurrentMap<String,String> mapperDataMap;

    ReducerProcessGenerator(String name, String args, ConcurrentMap mapperTaskStatus,int reducerCount,String kvStoreIP,int kvStorePort,String outputFile, String inputFileLocation) {
        this.name = name;
        this.args = args;
        this.mapperTaskStatus= mapperTaskStatus;
        this.reducerCount=reducerCount;
        this.kvStoreIP=kvStoreIP;
        this.kvStorePort=kvStorePort;
        this.outputFile =outputFile;
        this.inputFileLocation=inputFileLocation;
        mapperDataMap= new ConcurrentHashMap<>();


    }

    @Override
    public void run() {
        try {

            System.out.println(this.name);
            ManagedChannel channel = ManagedChannelBuilder.forAddress(kvStoreIP, kvStorePort).usePlaintext().build();
            KVStoreServiceGrpc.KVStoreServiceBlockingStub keyValueStoreBlockingStub =  KVStoreServiceGrpc.newBlockingStub(channel);
            String val= keyValueStoreBlockingStub.get(KVStore.GetRequest.newBuilder().setKey(args).build()).getValue();
            String[] listOfIntermediateKeys = val.split(",");




            ConcurrentHashMap<String,List<String>>  wordsToReduce = new ConcurrentHashMap<>();

            List<String> listOfIntermediateFiles = new ArrayList<>();
            for(String intermediateKey: listOfIntermediateKeys){
                intermediateKey=intermediateKey.trim();
                //create list of intermdeiate files for this reducer and pass through the command line.
                listOfIntermediateFiles.add(intermediateKey);

//                String[] wordOccurances = keyValueStoreBlockingStub.get(KVStore.GetRequest.newBuilder().setKey(intermediateKey).build()).getValue().replaceAll("\\[", "").replaceAll("\\]","").split(",");    //search with intermdeiate key
//                wordsToReduce.put(word,Arrays.asList(wordOccurances));
//                writeToFile(wordsToReduce,"E:\\CloudA2Output\\Red");

            }

            System.out.println("The list of intermediate keys is"+listOfIntermediateFiles.toString());
            Process process = new ProcessBuilder(new String[]{"java", "-jar", this.name,this.args, Integer.toString(this.reducerCount),kvStoreIP,Integer.toString(kvStorePort),outputFile,inputFileLocation,listOfIntermediateFiles.toString()}).start();
            if( process.getErrorStream().read() != -1 ){
                print("Errors ",process.getErrorStream());
            }else{
                print("OUTPUT ",process.getInputStream());
            }

            mapperTaskStatus.put(this.args,"completed");
            channel.shutdown();
            process.destroy();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void print(String status,InputStream input) throws IOException{
        BufferedReader in = new BufferedReader(new InputStreamReader(input));
        String line = null;
        while((line = in.readLine()) != null ){
            System.out.println(line);
        }

        in.close();
    }

    public static void writeToFile(ConcurrentMap<String,List<String>> output, String file){
        try{
            File outputFile = new File(file);
            BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile.getAbsoluteFile(), false));
            output.forEach((k, v) -> {
                try {
                    bw.write(k + "-" + v + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

