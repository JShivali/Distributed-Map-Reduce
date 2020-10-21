import generated.KVStore;
import generated.KVStoreServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.netty.shaded.io.netty.util.internal.ConcurrentSet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InvertedIndexMapper {
    private static final String reducerKeyIdentifier = "R_";

    public static void main(String[] args) {

        //log.debug("Mapper for val "+ args[0]+" started at : "+ new Date());
        String mapperNo=args[0];
        int reducerCount = Integer.parseInt(args[1]);
        String kvStoreIP=args[2];
        int kvStorePort = Integer.parseInt(args[3]);
        String outputfile=args[4];
        String inputFileLocation= args[6];
        String wordsList= args[5];

        System.out.println("Mapper for val "+ mapperNo +" started at : "+ new Date());

//        ManagedChannel channel = ManagedChannelBuilder.forAddress(kvStoreIP, kvStorePort).usePlaintext().build();
//        KVStoreServiceGrpc.KVStoreServiceBlockingStub keyValueStoreBlockingStub =  KVStoreServiceGrpc.newBlockingStub(channel);
//        String inputSplitsList= keyValueStoreBlockingStub.get(KVStore.GetRequest.newBuilder().setKey(mapperNo).build()).getValue();
        String[] wordlist=wordsList.replaceAll("\\[", "").replaceAll("\\]", "").toLowerCase().split(",");
        ConcurrentMap<String,List<Integer>> map = new ConcurrentHashMap<String,List<Integer>>();

        System.out.println("WORDLIST RECEIVED IS "+Arrays.toString(wordlist));
        for(String word: wordlist) {
            System.out.println(inputFileLocation+"\\"+"Intermediate_"+word.trim());
            String filename=word.split("_")[0];
            String wordKey=word.split("_")[1];
            if(!map.containsKey(wordKey+"_"+filename)){
                map.put(wordKey+"_"+filename,new ArrayList<>());
                map.get(wordKey+"_"+filename).add(1);
            }else{
                map.get(wordKey+"_"+filename).add(1);
            }

        }


        for(Map.Entry<String,List<Integer>> entry:map.entrySet()){
            String word= entry.getKey().split("_")[0];
            String fileName= entry.getKey().split("_")[1];
            writeToFile(fileName,entry.getValue().toString(),inputFileLocation+"\\"+"Intermediate_"+word.trim());
        }





        // from the list of inputSplitsList, for each input get list of words and create aggregated list of strings
//        for(String inputSplit:inputSplitsList.split(",")){
//            String url= inputSplit.split("_")[1];
//            String val=keyValueStoreBlockingStub.get(KVStore.GetRequest.newBuilder().setKey(inputSplit.trim()).build()).getValue();
//            List<String> al= new ArrayList<>();
//            val= val.replaceAll("\\[", "").replaceAll("\\]", "").toLowerCase();
//
//            String[] ap=val.split(",");
//
//            al.addAll(Arrays.asList(ap));
//            al.removeAll(Collections.singleton("key not present"));
//
//            for(String sp: al){
//                sp=sp.replaceAll("[^a-zA-Z ]", "").toLowerCase().trim();
//                if(!map.containsKey(sp)){
//                    map.put(sp,new ArrayList<String>());
//                    map.get(sp).add(url+" "+1);
//
//                }else{
//                    map.get(sp).add(url+" "+1);
//                    //8map.get(sp).
//                }
//            }
//        }

//        for(Map.Entry<String,List<String>> e: map.entrySet()){
//            for(String str:e.getValue() ){
//                keyValueStoreBlockingStub.append(KVStore.AppendRequest.newBuilder().setKey(e.getKey()).setValue(str).build());
//            }
//
//            // find which reducer would get the key using hash finction and add entry R_no : key into the KV store
//            int hashval = Math.abs(e.getKey().hashCode());
//            int reducerNo= hashval%reducerCount;
//            keyValueStoreBlockingStub.append(KVStore.AppendRequest.newBuilder().setKey(reducerKeyIdentifier+reducerNo).setValue(e.getKey().toString()).build());
//
//        }

        System.out.println("Mapper for val "+ args[0]+" finished at : "+ new Date());
        System.exit(0);
    }

    public static void writeToFile( String filename,String data,String file){
        try{

            // create files as intermediate_word and emit 1 appending in it
            File outputFile = new File(file);
            if (!outputFile.exists()) {
                if (!outputFile.createNewFile()) {
                    throw new IOException("Cannot create file KVStore.txt");
                }
            }

            BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile.getAbsoluteFile(), true));

            try {
                bw.write(filename+" - "+data.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(",","").replaceAll("\\s", "")+"\n");
            } catch (IOException e) {
                e.printStackTrace();
            }

            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
