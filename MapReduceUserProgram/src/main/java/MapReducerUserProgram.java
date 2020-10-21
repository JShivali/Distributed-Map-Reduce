import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.auth.http.HttpCredentialsAdapter;
import generated.Master;
import generated.StartMapReduceServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.api.services.compute.ComputeScopes;

import static generated.StartMapReduceServiceGrpc.newBlockingStub;

public class MapReducerUserProgram {
    static Logger log = Logger.getLogger(MapReducerUserProgram.class.getName());
    private static final String APPLICATION_NAME = "";

    /** Set PROJECT_ID to your Project ID from the Overview pane in the Developers console. */
    private static final String PROJECT_ID = "YOUR_PROJECT_ID";

    /** Set Compute Engine zone. */
    private static final String ZONE_NAME = "us-central1-f";

    /** Set the name of the sample VM instance to be created. */
    private static final String SAMPLE_INSTANCE_NAME = "my-sample-instance";

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


    public static void main(String[] args) {

        ConfigParams prop = null;
        try {
            prop = new ReadConfig().getPropValues();
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

        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }


        ManagedChannel channel = ManagedChannelBuilder.forAddress(prop.getMasterIP(),prop.getMasterPort()).usePlaintext().build();

        // stubs - generate from proto

        //read IPS from gcloud




        StartMapReduceServiceGrpc.StartMapReduceServiceBlockingStub startMapReduceServiceBlockingStub = newBlockingStub(channel);
        Master.MapReduceInputParams input= Master.MapReduceInputParams.newBuilder().setKvstorePort(prop.getkVStorePort())
                                            .setKVStoreIPAddress(prop.getkVStoreIp())
                                            .setMapperCount(prop.getMapperCount())
                                            .setFileLocation(prop.getFileLocation())
                                            .setReducerCount(prop.getReducerCount())
                                            .setMasterIPAddress(prop.getMasterIP())
                                            .setMasterPort(prop.getMasterPort())
                                            .setMapperFunction(prop.getMapperFunction())
                                            .setReducerFunction(prop.getReducerFunction()).build();
        startMapReduceServiceBlockingStub.mapReduce(input);

    }

}
