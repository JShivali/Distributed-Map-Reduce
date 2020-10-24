import generated.KVStore;
import generated.KVStoreServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.netty.shaded.io.netty.util.internal.ConcurrentSet;
import org.apache.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InvertedIndexMapper {
    private static final String intermediateKeyIdentifier = "intermediate_";
    private static final String reducerKeyIdentifier = "reducer-";
    static Logger log = Logger.getLogger(InvertedIndexMapper.class);

    public static void main(String[] args) {

        //log.debug("Mapper for val "+ args[0]+" started at : "+ new Date());
        String mapperNo=args[0];
        String kvStoreIP=args[1];
        String outputfile=args[2];
        String inputFileLocation= args[3];
        int reducerCount = Integer.parseInt(args[4]);
        //String wordsList= args[5];

        System.out.println("Mapper for val "+ mapperNo +" started at : "+ new Date());

        log.debug("*********** ENTERED MAPPERS PROCESS FOR MAPPER NUMBER  ************ "+mapperNo);
        ManagedChannel channel = ManagedChannelBuilder.forAddress(kvStoreIP, 9091).usePlaintext().build();
        KVStoreServiceGrpc.KVStoreServiceBlockingStub keyValueStoreBlockingStub =  KVStoreServiceGrpc.newBlockingStub(channel);
        String inputSplitsList= keyValueStoreBlockingStub.get(KVStore.GetRequest.newBuilder().setKey(mapperNo).build()).getValue();
        List<String> wordsList= new ArrayList<>();
        keyValueStoreBlockingStub.set(KVStore.SetRequest.newBuilder().setKey(mapperNo+"_status").setValue("inprogress").build());
        // from the list of inputSplitsList, for each input get list of words and create aggregated list of strings
        log.debug("*********** ENTERED MAPPERS PROCESS IS SPLITING INPUTS  ************ "+wordsList);
        for(String inputSplit:inputSplitsList.split(",")){
            //System.out.println("split is "+inputSplit);

            //just pass input split name to mapper
            //String words=keyValueStoreBlockingStub.get(KVStore.GetRequest.newBuilder().setKey(inputSplit.trim()).build()).getValue();
            // read data from file inputsplit_sample.txt_0


            log.debug("INPUT SPLIT FOR FILE IS"+inputSplit);
            log.debug ("******* DOWNLOADING FILE ******");


            log.debug("****** GOING TO read from file /usr/mapperData/"+inputSplit.trim() +"*********");
            //String words=readFromFile(new File(inputFileLocation+"/"+inputSplit.trim())).toString();
            String words= keyValueStoreBlockingStub.get(KVStore.GetRequest.newBuilder().setKey(inputSplit.trim()).build()).getValue();



            log.debug("words received from KV store input split "+words);
            words= words.replaceAll("\\[", "").replaceAll("\\]", "").toLowerCase();
            String[] wordsArray=words.split(",");
            String fileName= inputSplit.trim().split("_")[1];
            log.debug("************** words ***************"+Arrays.toString(wordsArray));
            List<String> wordlist=new ArrayList<>();
            for(String s: wordsArray){
                wordlist.add(fileName+"_"+s.trim());
            }
            wordsList.addAll(wordlist);
        }
        wordsList.removeAll(Collections.singleton("key not present"));




        log.debug("********* WORDSLIST GOING TO MAPPER FUNCTION IS "+wordsList);


        //String[] wordlist=wordsList.replaceAll("\\[", "").replaceAll("\\]", "").toLowerCase().split(",");
        ConcurrentMap<String,List<Integer>> map = new ConcurrentHashMap<String,List<Integer>>();

        //System.out.println("WORDLIST RECEIVED IS "+Arrays.toString(wordlist));
        for(String word: wordsList) {
            System.out.println(inputFileLocation+"/"+"Intermediate_"+word.trim());
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
           // writeToFile(fileName,entry.getValue().toString(),inputFileLocation+"\\"+"Intermediate_"+word.trim());
            //filename+" - "+data.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(",","").replaceAll("\\s", "")
            keyValueStoreBlockingStub.append(KVStore.AppendRequest.newBuilder().setKey("Intermediate_"+word.trim()).setValue(fileName+"~"+entry.getValue().toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(",","").replaceAll("\\s", "")).build());
        }

        for(String word:wordsList){
            //keyValueStoreBlockingStub.append(KVStore.AppendRequest.newBuilder().setKey(intermediateKeyIdentifier+word).setValue(e.getValue().toString()).build());
            // find which reducer would get the key using hash finction and add entry R_no : key into the KV store
            // create intermeduiate files and write to it
            int hashval = Math.abs(word.hashCode());
            int reducerNo= hashval%reducerCount;
            keyValueStoreBlockingStub.append(KVStore.AppendRequest.newBuilder().setKey(reducerKeyIdentifier+reducerNo).setValue(intermediateKeyIdentifier+word.split("_")[1].trim()).build());
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
        keyValueStoreBlockingStub.set(KVStore.SetRequest.newBuilder().setKey(mapperNo+"_status").setValue("completed").build());
        log.debug("Mapper for "+ mapperNo +" finished at : "+ new Date());
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
