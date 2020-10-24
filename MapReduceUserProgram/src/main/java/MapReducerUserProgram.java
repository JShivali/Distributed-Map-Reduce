import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.compute.Compute;
import com.google.api.services.compute.model.Instance;
import com.google.api.services.compute.model.InstanceList;
import com.google.api.services.compute.model.NetworkInterface;
import com.google.auth.http.HttpCredentialsAdapter;
import generated.Master;
import generated.StartMapReduceServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.api.services.compute.ComputeScopes;
import sun.misc.GC;

import static generated.StartMapReduceServiceGrpc.newBlockingStub;

public class MapReducerUserProgram {

    private static final String APPLICATION_NAME = "";

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
    static ConfigParams prop = null;
    static Logger logger = Logger.getLogger(MapReducerUserProgram.class.getName());
    private static ExecutorService executorService = Executors.newFixedThreadPool(50);

    static List<String> clusterVMS= new ArrayList<>();

    public static void main(String[] args) {


        logger.debug(" ***** MAPREDUCEUSER PROGRAM STARTED *****");

//# mappercount redcount inputloc mapperfnloc reducerfnloc outputloc
        int mappercount = Integer.parseInt(args[0]);
        int reducercount = Integer.parseInt(args[1]);
        String inputFileLocation =args[2];
        String mapperFunctionLoc= args[3];
        String reducerFunctionLoc= args[4];
        String outputLocation= args[5];

        logger.debug(" ***** MAPREDUCEUSER PROGRAM INPUT IS *****");
        logger.debug(" ***** INPUT FILE *****"+inputFileLocation);
        logger.debug(" ***** MAPPER FUNCTION *****"+mapperFunctionLoc);
        logger.debug(" ***** REDUCER FUNCTION *****"+reducerFunctionLoc);
        logger.debug(" ***** OUTPUT LOCATION *****"+outputLocation);
        logger.debug(" ***** REDUCER COUNT *****"+reducercount);
        logger.debug(" ***** MAPPER COUNT *****"+mappercount);
        logger.debug(" ********************************************");

        try {
            prop = new ReadConfig().getPropValues();

            // GCP connection logic
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
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

            logger.debug(" ***** MAPREDUCEUSER PROGRAM COMPUTE OBJECT CREATED *****");

            // set inputs to prop
            prop.setMapperCount(mappercount);
            prop.setReducerCount(reducercount);
            prop.setMapperFunction(mapperFunctionLoc);
            prop.setReducerFunction(reducerFunctionLoc);
            prop.setOutputFile(outputLocation);
            prop.setFileLocation(inputFileLocation);
            initCluster(compute,PROJECT_ID);
            System.out.println(compute.instances().get(PROJECT_ID,ZONE_NAME,"master-instance"));


        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }


        ManagedChannel channel = ManagedChannelBuilder.forAddress(prop.getMasterIP(),prop.getMasterPort()).usePlaintext().build();

        // stubs - generate from proto

        //read IPS from gcloud

        logger.debug(" ***** MAPREDUCEUSER PROGRAM HITTING STARTMAPREDUCE *****");
        StartMapReduceServiceGrpc.StartMapReduceServiceBlockingStub startMapReduceServiceBlockingStub = newBlockingStub(channel);
        System.out.println("Before sending the IPS are"+ prop.getkVStoreIp()+ " "+ prop.getMasterIP());
        Master.MapReduceInputParams input= Master.MapReduceInputParams.newBuilder().setKvstorePort(prop.getkVStorePort())
                                            .setKVStoreIPAddress(prop.getkVStoreIp())
                                            .setMapperCount(prop.getMapperCount())
                                            .setFileLocation(prop.getFileLocation())
                                            .setReducerCount(prop.getReducerCount())
                                            .setMasterIPAddress(prop.getMasterIP())
                                            .setMasterPort(prop.getMasterPort())
                                            .setMapperFunction(prop.getMapperFunction())
                                            .setReducerFunction(prop.getReducerFunction())
                                            .setOutputFile(prop.getOutputFile()).build();
        Master.MapReduceResponse mapReduceResponse =startMapReduceServiceBlockingStub.mapReduce(input);
        if(mapReduceResponse.getResponseCode()==0 && mapReduceResponse.getResponseMessage().equals("JobCompleted")) {
            //call destroy cluster
            logger.debug("****************** MAP REDUCE JOB HAS BEEN COMPLETED ********************");
            logger.debug("****************** DESTROYING CLUSTER ********************");
            destroyCluster();
        }
    }
    public static void initCluster(Compute compute, String projectId) throws IOException {
     logger.debug("================== Listing Compute Engine Instances ==================");
    Compute.Instances.List instances = compute.instances().list(projectId, ZONE_NAME);
    InstanceList list = instances.execute();
    if (list.getItems() == null) {
        logger.debug("No instances found. Sign in to the Google APIs Console and create "
                + "an instance at: code.google.com/apis/console");
    } else {
        for (Instance instance : list.getItems()) {
//            instance.getNetworkInterfaces().get(0).getNetworkIP();
            String instanceName=instance.getName();
            List<NetworkInterface> networkInterfaces=instance.getNetworkInterfaces();
            logger.debug(networkInterfaces.get(0).getAccessConfigs().get(0).getNatIP());
           // System.out.println(instance.toPrettyString());
            if(instanceName.equals(prop.getMasterVMName())){
                logger.debug("Setting master IP");
                prop.setMasterIP(networkInterfaces.get(0).getAccessConfigs().get(0).getNatIP());
            }
            else if(instanceName.equals(prop.getKvstoreVMName())){
                logger.debug("Setting KVStore IP");
                prop.setkVStoreIp(networkInterfaces.get(0).getAccessConfigs().get(0).getNatIP());
            }
            clusterVMS.add(instanceName);
        }

    }
}

    public static void destroyCluster(){


        logger.debug("****************** CLUSTER DESTROYED ********************");

        clusterVMS.forEach((instanceName)->{
            logger.debug("******* DESTROYING CLUSTER VM ****** "+instanceName);
            Future<Integer> future =  executorService.submit(() ->
                new GCloudComputeOperations(instanceName).deleteInstance(instanceName));
        });


        logger.debug("****************** PLEASE CHECK THE BUCKET FOR REDUCER OUTPUTS ********************");

    }

}
