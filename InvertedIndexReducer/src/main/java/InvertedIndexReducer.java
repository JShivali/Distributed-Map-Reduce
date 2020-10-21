import generated.KVStore;
import generated.KVStoreServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.netty.shaded.io.netty.util.internal.ConcurrentSet;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InvertedIndexReducer {


    static String outputFile="InvertedOutput";
    static ConcurrentMap<String,HashSet<String>> reducerData = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        System.out.println("Reducer for val "+ args[0]+" started at : "+ new Date());
        String kvStoreIP=args[2];
        int kvStorePort = Integer.parseInt(args[3]);
        String outputfile = args[4];
        String inputFileLocation=args[5];
        String[] intermdiateKeysList = args[6].replaceAll("\\[", "").replaceAll("\\]","").split(",");


//        System.out.println("Entered reducer of inverted index");
//        ManagedChannel channel = ManagedChannelBuilder.forAddress(kvStoreIP, kvStorePort).usePlaintext().build();
//        KVStoreServiceGrpc.KVStoreServiceBlockingStub keyValueStoreBlockingStub =  KVStoreServiceGrpc.newBlockingStub(channel);
//        String val= keyValueStoreBlockingStub.get(KVStore.GetRequest.newBuilder().setKey(args[0]).build()).getValue();

        String reducerNo=args[0];
        outputFile=outputFile+"_"+reducerNo;

        reduceInputs(inputFileLocation,intermdiateKeysList);
        System.out.println("Reducer for val "+ args[0]+" finished at : "+ new Date());
        System.exit(0);
    }

    public static void reduceInputs(String inputFileLocation, String[] intermediateKeysList){

        // Split inputs
        ConcurrentSet<String>  set = new ConcurrentSet();
        //set.addAll(Arrays.asList(s.split(",")));
        ConcurrentMap<String,List<String>> map = new ConcurrentHashMap<String,List<String>>();

        for(String intermediateKey:intermediateKeysList){
            readFromFile(new File(inputFileLocation+"\\"+intermediateKey.trim()),intermediateKey.split("_")[1]);

        }

        writeToFile(reducerData, outputFile);

        System.out.println("The aggregated values are: "+Arrays.asList(reducerData));


    }

    public static void writeToFile(ConcurrentMap<String,HashSet<String>> output, String file){
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
    public static void readFromFile(File file,String word){
        try {
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    throw new IOException("Cannot create file KVStore.txt");
                }
            }
            BufferedReader br = new BufferedReader(new FileReader(file.getAbsoluteFile()));
            HashMap<String,Integer> combinerMap = new HashMap<>();

            String line;
            while ((line = br.readLine()) != null) {
                String input = line;
                try {
                    String filename = input.split("-")[0].trim();
                    int count = input.split("-")[1].trim().length();
                    if(!combinerMap.containsKey(filename)){
                        combinerMap.put(filename,count);
                    }else{
                        combinerMap.replace(filename,combinerMap.get(filename),combinerMap.get(filename)+count);
                    }

                } catch (Exception e) {
                    System.out.println( "buffered reader exception");
                }
            }

            for (Map.Entry<String,Integer> entry: combinerMap.entrySet()){
                if(!reducerData.containsKey(word)){
                    reducerData.put(word,new HashSet<>());
                    reducerData.get(word).add(entry.getKey()+" "+entry.getValue());
                }else{
                    reducerData.get(word).add(entry.getKey()+" "+entry.getValue());
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
