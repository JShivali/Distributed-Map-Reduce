import generated.KVStore;
import generated.KVStoreServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class MapperProcessGenerator implements  Runnable  {
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

    MapperProcessGenerator(String name, String args, ConcurrentMap mapperTaskStatus,int reducerCount,String kvStoreIP,int kvStorePort,String outputFile, String inputFileLocation) {
        this.name = name;
        this.args = args;
        this.mapperTaskStatus= mapperTaskStatus;
        this.reducerCount=reducerCount;
        this.kvStoreIP=kvStoreIP;
        this.kvStorePort=kvStorePort;
        this.outputFile =outputFile;
        this.inputFileLocation=inputFileLocation;
        mapperDataMap= new ConcurrentHashMap<>();
      ;

    }

    @Override
    public void run() {
        try {

            System.out.println(this.name);
            ManagedChannel channel = ManagedChannelBuilder.forAddress(kvStoreIP, kvStorePort).usePlaintext().build();
            KVStoreServiceGrpc.KVStoreServiceBlockingStub keyValueStoreBlockingStub =  KVStoreServiceGrpc.newBlockingStub(channel);
            String inputSplitsList= keyValueStoreBlockingStub.get(KVStore.GetRequest.newBuilder().setKey(this.args).build()).getValue();
            List<String> wordsList= new ArrayList<>();
            // from the list of inputSplitsList, for each input get list of words and create aggregated list of strings
            for(String inputSplit:inputSplitsList.split(",")){
                //System.out.println("split is "+inputSplit);
                System.out.println(inputSplit);
                //just pass input split name to mapper
                //String words=keyValueStoreBlockingStub.get(KVStore.GetRequest.newBuilder().setKey(inputSplit.trim()).build()).getValue();
                // read data from file inputsplit_sample.txt_0
                System.out.println("INPUT SPLIT FOR FILE IS"+inputSplit);
                String words=readFromFile(new File(inputSplit.trim())).toString();
                System.out.println("words"+words);
                words= words.replaceAll("\\[", "").replaceAll("\\]", "").toLowerCase();
                String[] wordsArray=words.split(",");
                String fileName= inputSplit.trim().split("_")[1];
                System.out.println("words"+Arrays.toString(wordsArray));
                List<String> wordlist=new ArrayList<>();
                for(String s: wordsArray){
                    wordlist.add(fileName+"_"+s.trim());
                }
                wordsList.addAll(wordlist);
            }
            wordsList.removeAll(Collections.singleton("key not present"));

            Process process = new ProcessBuilder(new String[]{"java", "-jar", this.name,this.args, Integer.toString(this.reducerCount),kvStoreIP,Integer.toString(kvStorePort),outputFile,wordsList.toString(),inputFileLocation}).start();
                if( process.getErrorStream().read() != -1 ){
                    print("Errors ",process.getErrorStream());
                }else{
                    print("OUTPUT ",process.getInputStream());
                }


                // write from files to KV store if args contains M_
                if(this.args.contains("M_")){

                    // This is a mapper process
                    //write data from file to KV store
                        //readFromFile(new File(outputFile+"\\"+this.args)); //read from intermdiate files and
                    //create list of intermediate files from words list.
                    for(String word:wordsList){
                        //keyValueStoreBlockingStub.append(KVStore.AppendRequest.newBuilder().setKey(intermediateKeyIdentifier+word).setValue(e.getValue().toString()).build());
                        // find which reducer would get the key using hash finction and add entry R_no : key into the KV store
                        // create intermeduiate files and write to it
                        int hashval = Math.abs(word.hashCode());
                        int reducerNo= hashval%reducerCount;
                        keyValueStoreBlockingStub.append(KVStore.AppendRequest.newBuilder().setKey(reducerKeyIdentifier+reducerNo).setValue(intermediateKeyIdentifier+word.split("_")[1].trim()).build());
                    }
                    //go through each word in the list and calculate hash and assign to reducer
//                    for(Map.Entry<String, String> e: mapperDataMap.entrySet()){
//                        keyValueStoreBlockingStub.append(KVStore.AppendRequest.newBuilder().setKey(intermediateKeyIdentifier+e.getKey()).setValue(e.getValue().toString()).build());
//                        // find which reducer would get the key using hash finction and add entry R_no : key into the KV store
//                        // create intermeduiate files and write to it
//
//
//                        int hashval = Math.abs(e.getKey().hashCode());
//                        int reducerNo= hashval%reducerCount;
//                        keyValueStoreBlockingStub.append(KVStore.AppendRequest.newBuilder().setKey(reducerKeyIdentifier+reducerNo).setValue(intermediateKeyIdentifier+e.getKey()).build());
//                    }
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

    public String readFromFile(File file) {
        List<String> filecontents = new ArrayList();
        try {
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    throw new IOException("Cannot create file KVStore.txt");
                }
            }
            BufferedReader br = new BufferedReader(new FileReader(file.getAbsoluteFile()));
            String line;


            while ((line = br.readLine()) != null) {
                String splitInput = line;
                filecontents.add(splitInput);
            }
            br.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filecontents.toString();
    }

    public static void writeToFile(ConcurrentMap<String,List<Integer>> output, String file){
        try{
            File outputFile = new File(file);
            if (!outputFile.exists()) {
                if (!outputFile.createNewFile()) {
                    throw new IOException("Cannot create file KVStore.txt");
                }
            }

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

