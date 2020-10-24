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
    public static final long OPERATION_TIMEOUT_MILLIS = 60 * 1000 *2;



    /** Global instance of the HTTP transport. */
    private static HttpTransport httpTransport;

    /** Global instance of the JSON factory. */
    public static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();


}
