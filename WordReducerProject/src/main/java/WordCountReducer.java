
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.compute.Compute;
import com.google.api.services.compute.ComputeScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import generated.KVStore;
import generated.KVStoreServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class WordCountReducer {


    static String outputFile="WordCountOutput";
    static String outputFileName="WordCountOutput";
    private static HttpTransport httpTransport;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String APPLICATION_NAME = "WordCountReducer";
    static List<String> splitsListAfterStorage= new ArrayList<>();
    static String PROJECT_ID="cloud-map-reduce";
    static String PROJECT_BUCKET="cloud-map-red-bucket";
    static String INTERMEDIATEKEY="Intermediate_";

    static ConcurrentMap<String,Integer> reducerData = new ConcurrentHashMap<>();

    static Logger log = Logger.getLogger(WordCountReducer.class);
    public static void main(String[] args) {

        log.debug("Reducer  "+ args[0]+" started at : "+ new Date());
        System.out.println("Reducer  "+ args[0]+" started at : "+ new Date());

        String reducerNo=args[0];
        String kvStoreIp= args[1];
        String outputfile = args[2];
        String inputFileLocation=args[3];
        outputFile=outputfile+"/"+outputFile+reducerNo;
        outputFileName="WordCountOutput"+reducerNo;

        log.debug("Values received are "+ reducerNo+" "+kvStoreIp+" "+outputfile+" "+inputFileLocation+" "+outputFile);


        ManagedChannel channel = ManagedChannelBuilder.forAddress(kvStoreIp, 9091).usePlaintext().build();
        KVStoreServiceGrpc.KVStoreServiceBlockingStub keyValueStoreBlockingStub =  KVStoreServiceGrpc.newBlockingStub(channel);
        String val= keyValueStoreBlockingStub.get(KVStore.GetRequest.newBuilder().setKey(reducerNo).build()).getValue();
        String[] listOfIntermediateKeys = val.split(",");

        keyValueStoreBlockingStub.set(KVStore.SetRequest.newBuilder().setKey(reducerNo+"_status").setValue("inprogress").build());

        List<String> listOfIntermediateFiles = new ArrayList<>();
        log.debug("********* INTERMEDIATE KEYS ARE ********"+listOfIntermediateFiles.toString());
        for(String intermediateKey: listOfIntermediateKeys){
            intermediateKey=intermediateKey.trim();
            //create list of intermdeiate files for this reducer and pass through the command line.
            listOfIntermediateFiles.add(intermediateKey);

//                String[] wordOccurances = keyValueStoreBlockingStub.get(KVStore.GetRequest.newBuilder().setKey(intermediateKey).build()).getValue().replaceAll("\\[", "").replaceAll("\\]","").split(",");    //search with intermdeiate key
//                wordsToReduce.put(word,Arrays.asList(wordOccurances));
//                writeToFile(wordsToReduce,"E:\\CloudA2Output\\Red");

        }

        String[] intermdiateKeysList =listOfIntermediateFiles.toArray(new String[listOfIntermediateFiles.size()]);



        reduceInputs(inputFileLocation,intermdiateKeysList,keyValueStoreBlockingStub);

        // read intermediate files list. For each file find the file and add the word and count(contenrts of intermdiate_word) to map and write to output file
        log.debug("Reducer for val "+ args[0]+" finished at : "+ new Date());
        System.out.println("Reducer "+ args[0]+" finished at : "+ new Date());
        keyValueStoreBlockingStub.set(KVStore.SetRequest.newBuilder().setKey(reducerNo+"_status").setValue("completed").build());
        System.exit(0);
    }

    public static void reduceInputs(String inputFileLocation,String[] intermdiateKeysList,KVStoreServiceGrpc.KVStoreServiceBlockingStub keyValueStoreBlockingStub) {
        // Split inputs
        log.debug("********* STARTING REDUCING INPUTS ********"+intermdiateKeysList);
        ConcurrentMap<String,Integer> map = new ConcurrentHashMap<>();
        for(String intermediateKey:intermdiateKeysList){
            //readFromFile(new File(inputFileLocation+"/"+intermediateKey.trim()),intermediateKey.split("_")[1]);
            String word = intermediateKey.split("_")[1];
            log.debug("*********** VALUE FETCHING IS *********"+word);
            String freq =keyValueStoreBlockingStub.get(KVStore.GetRequest.newBuilder().setKey(INTERMEDIATEKEY+word).build()).getValue();
            log.debug("************ VALUE FOR frequency of word is"+freq.split(",").length);

            reducerData.put(word,freq.split(",").length);


        }

        writeToFile(reducerData, outputFile);
        try {
            uploadObject(PROJECT_ID,PROJECT_BUCKET,outputFileName,outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println("The aggregated values are: " + Arrays.asList(reducerData));
        log.debug("The aggregated values are: " + Arrays.asList(map));

    }

    // Write output to file specified by use
    public static void writeToFile(ConcurrentMap<String,Integer> output, String file){
        try{
            File outputFile = new File(file);
            BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile.getAbsoluteFile(), true));
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
            String line;
            while ((line = br.readLine()) != null) {
                String input = line;
                try {
                    reducerData.put(word, input.length());
                } catch (Exception e) {
                    System.out.println( "buffered reader exception");
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static List<String> uploadObject(
            String projectId, String bucketName, String objectName, String filePath) throws IOException {
        // The ID of your GCP project
        // String projectId = "your-project-id";

        // The ID of your GCS bucket
        // String bucketName = "your-unique-bucket-name";

        // The ID of your GCS object
        // String objectName = "your-object-name";

        // The path to your file to upload
        // String filePath = "path/to/your/file"

        log.debug(" ***** REDUCEr  UPLOADING REDUCED FILES TO BUCKET *****");

        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        GoogleCredentials credential = GoogleCredentials.getApplicationDefault();
        if (credential.createScopedRequired()) {
            List<String> scopes = new ArrayList<>();
            // Set Google Cloud Storage scope to Full Control.
            scopes.add(ComputeScopes.DEVSTORAGE_FULL_CONTROL);
            // Set Google Compute Engine scope to Read-write.
            scopes.add(ComputeScopes.COMPUTE);
            credential = credential.createScoped(scopes);
        }
        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credential);
        Compute compute = new Compute.Builder(
                httpTransport, JSON_FACTORY, null).setApplicationName(APPLICATION_NAME)
                .setHttpRequestInitializer(requestInitializer).build();

        Storage storage = StorageOptions.newBuilder().setCredentials(credential).setProjectId(projectId).build().getService();
        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        splitsListAfterStorage.add(objectName);
        storage.create(blobInfo, Files.readAllBytes(Paths.get(filePath)));

        log.debug(
                "File " + filePath + " uploaded to bucket " + bucketName + " as " + objectName);
        log.debug(" *****  REDUCED FILES TO BUCKET FINISHED UPLOADING INPUT FILES TO GOOGLE STORAGE *****");

        return splitsListAfterStorage;
    }


}

