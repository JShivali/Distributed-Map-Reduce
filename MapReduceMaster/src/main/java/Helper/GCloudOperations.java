package Helper;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.compute.Compute;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class GCloudOperations {



    public Compute createComputeObj() throws GeneralSecurityException, IOException {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        GoogleCredential credential = GoogleCredential.getApplicationDefault();

        // Create compute engine object for listing instances
        Compute compute = new Compute.Builder(
                httpTransport, JSON_FACTORY, null).setApplicationName(APPLICATION_NAME)
                .setHttpRequestInitializer(credential).build();

        return new Compute.Builder(httpTransport, Constants.JSON_FACTORY, credential)
                .setApplicationName(Constants.APPLICATION_NAME)
                .build();

    }
}
