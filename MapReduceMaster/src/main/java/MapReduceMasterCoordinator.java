import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.compute.Compute;
import com.google.api.services.compute.ComputeScopes;
import com.google.api.services.compute.model.Metadata;
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
import java.util.concurrent.*;
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
    private static final String mapperKeyIdentifier = "mapper-";
    private static final String reducerKeyIdentifier = "reducer-";
    private ExecutorService executorService = Executors.newFixedThreadPool(50);

    private ConcurrentMap<String,String>  mapperTaskStatus;
    private ConcurrentMap<String,String>  reducerTaskStatus;
    static int counter=0;

    static Logger log = Logger.getLogger(MapReduceMasterCoordinator.class);

    private static final String APPLICATION_NAME = "MasterApp";

    /** Set PROJECT_ID to your Project ID from the Overview pane in the Developers console. */
    private static final String PROJECT_ID = "cloud-map-reduce";

    /** Set Compute Engine zone. */
    private static final String ZONE_NAME = "us-central1-a";

    /** Set the name of the sample VM instance to be created. */
    private static final String SAMPLE_INSTANCE_NAME = "userprog-instance";

    private static final String SOURCE_IMAGE_PREFIX =
            "https://www.googleapis.com/compute/v1/projects/";

    private static final String SOURCE_IMAGE_PATH =
            "ubuntu-os-cloud/global/images/ubuntu-2004-focal-v20200529";

    /** Set the Network configuration values of the sample VM instance to be created. */
    private static final String NETWORK_INTERFACE_CONFIG = "ONE_TO_ONE_NAT";

    private static final String NETWORK_ACCESS_CONFIG = "External NAT";

    /** Set the time out limit for operation calls to the Compute Engine API. */
    private static final long OPERATION_TIMEOUT_MILLIS = 60 * 1000;

    /** Global instance of the HTTP transport. */
    private static HttpTransport httpTransport;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    static List<String> splitsListAfterStorage= new ArrayList<>();


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
        log.debug(" ***** MAP REDUCE STARTED *****");

        ManagedChannel channel = ManagedChannelBuilder.forAddress(kVStoreIp, kVStorePort).usePlaintext().build();
        KVStoreServiceGrpc.KVStoreServiceBlockingStub blockingStub = KVStoreServiceGrpc.newBlockingStub(channel);

        log.debug(" ***** MAP REDUCE MASTER CREATING SPLITS *****");
        // Create splits
        List<String> inputFiles=null;
        inputFiles=Stream.of(new File(fileLocation).listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toList());
        List<String> totalKeys = new ArrayList();
        List<List<String>> splittedKey = new ArrayList<>();
        for(String file: inputFiles) {
            List<String[]> splits=createSplits(fileLocation+"/"+file);
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
        /*********** Write input split to KV STORE ***************/


        log.debug(" ***** MAP REDUCE MASTER SPLITS CREATION DONE *****");
            distributeKeysChunksToMappers(keys,mapperCount, blockingStub, mapperFunction);
    }

    public List<String[]> createSplits(String fileLocation){
        log.debug(" ***** MAP REDUCE MASTER CREATING SPLITS PLEASE WAIT *****");
        List<String[]> splits=null;
        try {
                splits = Files.lines(Paths.get(fileLocation)).map(l->l.split(" ")).collect(Collectors.toList());
                for(String s[]: splits){
                    log.debug(Arrays.toString(s));
                }
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.debug("*********** CREATED SPLITS **************" + splits);
        return splits;
    }


    public List<String> storeInputSplits(List<String[]> splits,String fileName, KVStoreServiceGrpc.KVStoreServiceBlockingStub blockingStub){
        log.debug(" ***** MAP REDUCE MASTER STORING INPUTS IN BUCKET *****");
        int count=0;
        List<String> split=null;
        for(String s[]:splits){

            writeToFile(Arrays.toString(s),fileLocation+"/"+inputKeyAppender+fileName+"_"+count);
            //write to gbucket
            blockingStub.set(KVStore.SetRequest.newBuilder().setKey(inputKeyAppender+fileName+"_"+count).setValue(Arrays.toString(s)).build());
            try {
                split=uploadObject("cloud-map-reduce","cloud-map-red-bucket","Input/"+inputKeyAppender+fileName+"_"+count,fileLocation+"/"+inputKeyAppender+fileName+"_"+count);
            } catch (IOException e) {
                e.printStackTrace();
            }
            count++;
        }
        return split;
    }

    public void distributeKeysChunksToMappers(List<String> inputSplitKeys, int mapperCount,KVStoreServiceGrpc.KVStoreServiceBlockingStub blockingStub, String mapperFunction) {
        log.debug(" ***** MAP REDUCE MASTER DISTRIBUTING KEY CHUNKS TO MAPPERS *****");
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
                mapperData.replace(mapperKeyIdentifier+counter, mapperData.get(mapperKeyIdentifier+counter) + ", " + inputSplitKey);
            }
            counter++;
        }

        int i=0;

        log.debug(" ***** BEFORE WRITING TO KV STORE *****"+ mapperData.toString());

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

        //set file location to storage location

       //new Thread(new MapperProcessGenerator(mapperFunction,mapperKey, mapperTaskStatus,reducerCount, kVStoreIp,kVStorePort,outputFile,fileLocation)).start();

//        String command = "python /c start python /usr/app6/Distributed-Map-Reduce/Scripts/create_instance.py";
//        Process p = Runtime.getRuntime().exec(command +  );

        log.debug("******* creating mapper tasks ******");
        Metadata metadata = getMetaData(mapperFunction,Integer.toString(reducerCount),kVStoreIp, mapperKey,masterIP, outputFile,fileLocation);
        createTask(mapperKey,metadata);



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

    public void createTask(String instanceName,Metadata metadata){
        log.debug("******* creating mapper tasks for {} ******"+instanceName);
        Future<Integer> future =  executorService.submit(() ->
                new GCloudComputeOperations(instanceName).startInstance(metadata));
    }

    private Metadata getMetaData( String functionName, String reducerCount, String kvStoreDetails, String mapperName, String masterDetails, String outputFileLoc, String inputfileLoc) {

        Metadata metadata = new Metadata();
        List<Metadata.Items> itemsList = new ArrayList<>();
        itemsList.add(getItem(Constants.MAPPER_STARTUP_SCRIPT_KEY, Constants.MAPPER_STARTUP_SCRIPT_VALUE));
        itemsList.add(getItem(Constants.MAPPER_FUNCTION, functionName));
        itemsList.add(getItem(Constants.KV_STORE_IP, kvStoreDetails));
        itemsList.add(getItem(Constants.MASTER_IP, masterDetails));
        itemsList.add(getItem(Constants.OUTPUT_FILE_LOC,outputFileLoc));
        itemsList.add(getItem(Constants.INPUT_FILE_LOC,inputfileLoc));
        itemsList.add(getItem(Constants.REDUCER_COUNT,reducerCount));
        itemsList.add(getItem("mapperName",mapperName));
        metadata.setItems(itemsList);
        return metadata;
    }
    private Metadata.Items getItem(String key, String value) {
        Metadata.Items item = new Metadata.Items();
        item.setKey(key);
        item.setValue(value);

        return item;
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

        log.debug(" ***** MAP REDUCE MASTER UPLOADING INPUT FILES TO BUCKET *****");

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
        splitsListAfterStorage.add(objectName.split("/")[1]);
        storage.create(blobInfo, Files.readAllBytes(Paths.get(filePath)));

        log.debug(
                "File " + filePath + " uploaded to bucket " + bucketName + " as " + objectName);
        log.debug(" ***** MAP REDUCE MASTER FINISHED UPLOADING INPUT FILES TO GOOGLE STORAGE *****");

        return splitsListAfterStorage;
    }


}
