//
//import com.google.api.client.http.HttpTransport;
//import com.google.auth.oauth2.GoogleCredentials;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.compute.Compute;
import com.google.api.services.compute.ComputeScopes;
import generated.KVStore;
import generated.KVStoreServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.sql.Blob;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class WordCountMapper {

    private static final String intermediateKeyIdentifier = "intermediate_";
    private static final String reducerKeyIdentifier = "reducer-";
    static Logger log = Logger.getLogger(WordCountMapper.class);

    public static void main(String[] args) {

        String mapperNo= args[0];
        String kvStoreIp= args[1];
        String outputfile=args[2];
        String inputFileLocation= args[3];
        int reducerCount= Integer.parseInt(args[4]);


        log.debug("*********** ENTERED MAPPERS PROCESS FOR MAPPER NUMBER  ************ "+mapperNo);
        ManagedChannel channel = ManagedChannelBuilder.forAddress(kvStoreIp, 9091).usePlaintext().build();
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

//---


       // String[] wordlist=wordsList.replaceAll("\\[", "").replaceAll("\\]", "").toLowerCase().split(",");
            for(String word: wordsList) {
                System.out.println(inputFileLocation+"/"+"Intermediate_"+word.split("_")[1].trim());
                //writeToFile(inputFileLocation+"/"+"Intermediate_"+word.split("_")[1].trim());

                keyValueStoreBlockingStub.append(KVStore.AppendRequest.newBuilder().setKey("Intermediate_"+word.split("_")[1].trim()).setValue("1").build());
            }



        for(String word:wordsList){
            //keyValueStoreBlockingStub.append(KVStore.AppendRequest.newBuilder().setKey(intermediateKeyIdentifier+word).setValue(e.getValue().toString()).build());
            // find which reducer would get the key using hash finction and add entry R_no : key into the KV store
            // create intermeduiate files and write to it
            int hashval = Math.abs(word.hashCode());
            int reducerNo= hashval%reducerCount;
            keyValueStoreBlockingStub.append(KVStore.AppendRequest.newBuilder().setKey(reducerKeyIdentifier+reducerNo).setValue(intermediateKeyIdentifier+word.split("_")[1].trim()).build());
        }

        keyValueStoreBlockingStub.set(KVStore.SetRequest.newBuilder().setKey(mapperNo+"_status").setValue("completed").build());
        log.debug("Mapper for "+ mapperNo +" finished at : "+ new Date());
        System.out.println("Mapper for "+ mapperNo +" finished at : "+ new Date());
        System.exit(0);
    }

    public static void writeToFile( String file){
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
                    bw.write("1");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String readFromFile(File file) {
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

//    public static void downloadObject(
//            String projectId, String bucketName,String fileName) throws IOException {
//        try {
//            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
//        } catch (GeneralSecurityException e) {
//            e.printStackTrace();
//        }
//        GoogleCredentials credential = GoogleCredentials.getApplicationDefault();
//        if (credential.createScopedRequired()) {
//            List<String> scopes = new ArrayList<>();
//            // Set Google Cloud Storage scope to Full Control.
//            scopes.add(ComputeScopes.DEVSTORAGE_FULL_CONTROL);
//            // Set Google Compute Engine scope to Read-write.
//            scopes.add(ComputeScopes.COMPUTE);
//            credential = credential.createScoped(scopes);
//        }
//        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credential);
//        Compute compute = new Compute.Builder(
//                httpTransport, JSON_FACTORY, null).setApplicationName(APPLICATION_NAME)
//                .setHttpRequestInitializer(requestInitializer).build();
//
//        Storage storage = StorageOptions.newBuilder().setCredentials(credential).setProjectId(projectId).build().getService();
//        storage.get(bucketName, "Input/"+fileName).downloadTo(Paths.get("/usr/mapperData"));
//
//    }


}
