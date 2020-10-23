import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

public class Constants {
    public static final String APPLICATION_NAME = "MasterApp";

    /** Set PROJECT_ID to your Project ID from the Overview pane in the Developers console. */
    public static final String PROJECT_ID = "cloud-map-reduce";

    /** Set Compute Engine zone. */
    public static final String ZONE_NAME = "us-central1-a";

    /** Set the name of the sample VM instance to be created. */

    public static final String SOURCE_IMAGE_PREFIX =
            "https://www.googleapis.com/compute/v1/projects/";

    public static final String SOURCE_IMAGE_PATH =
            "projects/cloud-map-reduce/global/images/mapredbase";

    /** Set the Network configuration values of the sample VM instance to be created. */
    public static final String NETWORK_INTERFACE_CONFIG = "ONE_TO_ONE_NAT";

    public static final String NETWORK_ACCESS_CONFIG = "External NAT";

    /** Set the time out limit for operation calls to the Compute Engine API. */
    public static final long OPERATION_TIMEOUT_MILLIS = 60 * 1000;



    /** Global instance of the HTTP transport. */
    private static HttpTransport httpTransport;

    /** Global instance of the JSON factory. */
    public static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    public static final String MAPPER_STARTUP_SCRIPT_KEY= "startup-script-url";
    public static final String MAPPER_FUNCTION = "mapperfunction";
    public static final String REDUCER_STARTUP_SCRIPT_KEY= "reducerfunction";
    public static final String MAPPER_STARTUP_SCRIPT_VALUE= "gs://cloud-map-red-bucket/start-worker.sh";
    public static final String REDUCER_STARTUP_SCRIPT_VALUE= "/usr/app6/Distributed-Map-Reduce/Scripts/";
    public static final String KV_STORE_IP= "kvstoreip";
    public static final String MASTER_IP= "masterip";
    public static final String OUTPUT_FILE_LOC = "outputFileLoc";
    public static final String INPUT_FILE_LOC = "inputFileLoc";
    public static final String REDUCER_COUNT = "reducercount";

}
