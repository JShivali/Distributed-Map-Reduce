import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.services.compute.Compute;
import com.google.api.services.compute.model.*;
import org.apache.log4j.Logger;


import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GCloudComputeOperations {

    static Logger log = Logger.getLogger(GCloudComputeOperations.class);

    public GCloudComputeOperations(String instanceName) {
        this.instanceName = instanceName;
    }

    private String instanceName;
    Compute compute;

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }



    public InstanceList getInstancesList(Compute compute) throws IOException {
        log.debug("***** GCLOUDCOMPUTEOPERATIONS - GETINSTANCELIST() ******");
        return compute.instances().list(Constants.PROJECT_ID, Constants.ZONE_NAME).execute();
    }

    public String getIpAddressOfInstance() throws GeneralSecurityException, IOException {

        log.debug("***** GCLOUDCOMPUTEOPERATIONS - getIpAddressOfInstance() ******");
        Compute compute = createCompute();

        InstanceList instanceList = getInstancesList(compute);
        Optional<Instance> optionalInstance = instanceList.getItems().stream()
                .filter(instance -> instance.getName().equalsIgnoreCase(instanceName)).findAny();

        return optionalInstance.map(instance ->
                instance.getNetworkInterfaces().get(0).getAccessConfigs().get(0).getNatIP())
                .orElse(null);
    }


    private Compute createCompute() throws GeneralSecurityException, IOException {
        log.debug("***** GCLOUDCOMPUTEOPERATIONS - createCompute() ******");
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        // Use Google Application Default Credentials to authenticate
        GoogleCredential credential = GoogleCredential.getApplicationDefault();
        // Create Compute Object
        return new Compute.Builder(httpTransport, Constants.JSON_FACTORY, credential)
                .setApplicationName(Constants.APPLICATION_NAME)
                .build();
    }

    public int startInstance(Metadata metadata/*, boolean isPreemptable*/) {
        try {

            log.debug("****** Metadat is "+metadata.toPrettyString());
            Compute compute = createCompute();
            // List out instances, looking for the one created by this sample app.
            boolean foundOurInstance = checkForInstance(compute);

            Operation op;
            if (foundOurInstance) {
                op = deleteInstance(compute, instanceName);
                waitForOperationCompletion(compute, op);
            }

            op = startInstance(/*compute*/ metadata,instanceName);
            waitForOperationCompletion(compute, op);
            return 0;
        } catch (Throwable e) {
            log.error(e.getMessage());
            return -1;
        }
    }

    public Operation startInstance( Metadata metadata,String instanceName) throws IOException {
        log.info("***** STARTING NEW INSTANCE ********");
        try {
             compute =createCompute();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

        // Create VM Instance object with the required properties.
        Instance instance = new Instance();

        log.info("***** STARTING NEW INSTANCE MACHINE ATTRIBUTES ********");
        instance.setName(instanceName);
        instance.setKind("compute#instance");
        instance.setCanIpForward(false);
        instance.setZone("projects/" + Constants.PROJECT_ID + "/zones/" + Constants.ZONE_NAME);
        instance.setMachineType("projects/" + Constants.PROJECT_ID + "/zones/" + Constants.ZONE_NAME + "/machineTypes/e2-micro");
        instance.setDeletionProtection(false);
        instance.setMetadata(metadata);





        log.info("***** STARTING NEW INSTANCE DISK ATTRIBUTES ********");
        List<AttachedDisk> disk = new ArrayList<>();
        AttachedDisk attachedDisk = new AttachedDisk();
        AttachedDiskInitializeParams attachedDiskInitializeParams = new AttachedDiskInitializeParams();
        attachedDiskInitializeParams.setSourceImage(Constants.SOURCE_IMAGE_PATH);
        attachedDiskInitializeParams.setDiskType("projects/" + Constants.PROJECT_ID + "/zones/" + Constants.ZONE_NAME + "/diskTypes/pd-standard");
        attachedDiskInitializeParams.setDiskSizeGb((long) 10);
        attachedDisk.setInitializeParams(attachedDiskInitializeParams);
        attachedDisk.setAutoDelete(true);
        attachedDisk.setBoot(true);
        attachedDisk.setDeviceName(instanceName);
        attachedDisk.setKind("compute#attachedDisk");
        attachedDisk.setMode("READ_WRITE");
        attachedDisk.setType("PERSISTENT");
        disk.add(attachedDisk);
        instance.setDisks(disk);

        log.info("***** STARTING NEW INSTANCE NETWORK PROPERTIES ********");
        List<NetworkInterface> networkInterfaces = new ArrayList<>();
        NetworkInterface networkInterface = new NetworkInterface();
        networkInterface.setKind("compute#networkInterface");
        String region = Constants.ZONE_NAME.split("-")[0] + "-" + Constants.ZONE_NAME.split("-")[1];
        networkInterface.setSubnetwork("projects/" + Constants.PROJECT_ID + "/regions/" + region + "/subnetworks/default");
        List<AccessConfig> accessConfigs = new ArrayList<>();
        AccessConfig accessConfig = new AccessConfig();
        accessConfig.setKind("compute#accessConfig");
        accessConfig.setName("External NAT");
        accessConfig.setType("ONE_TO_ONE_NAT");
        accessConfig.setNetworkTier("PREMIUM");
        accessConfigs.add(accessConfig);
        networkInterface.setAccessConfigs(accessConfigs);
        networkInterfaces.add(networkInterface);
        instance.setNetworkInterfaces(networkInterfaces);


        log.info("***** STARTING NEW INSTANCE SCOPES / AUTHENTICATION ********");
        List<ServiceAccount> serviceAccounts = new ArrayList<>();
        ServiceAccount serviceAccount = new ServiceAccount();
        serviceAccount.setEmail("269373890514-compute@developer.gserviceaccount.com");
        List<String> scopes = new ArrayList<>();
        scopes.add("https://www.googleapis.com/auth/cloud-platform");
        scopes.add("https://www.googleapis.com/auth/compute");
        scopes.add("https://www.googleapis.com/auth/devstorage.full_control");
        serviceAccount.setScopes(scopes);
        serviceAccounts.add(serviceAccount);
        instance.setServiceAccounts(serviceAccounts);

        if (metadata != null) {
            instance.setMetadata(metadata);
        }

        log.info(instance.toPrettyString());
        Compute.Instances.Insert insert = compute.instances().insert(Constants.PROJECT_ID, Constants.ZONE_NAME, instance);
        return insert.execute();
    }

    public int deleteInstance() {
        try {
            Compute compute = createCompute();
            boolean foundOurInstance = checkForInstance(compute);

            Operation op;
            if (foundOurInstance) {
                op = deleteInstance(compute, instanceName);
                waitForOperationCompletion(compute, op);
            }
            return 0;
        } catch (Throwable e) {
            log.error(e.getMessage());
            return -1;
        }
    }
    private static Operation deleteInstance(Compute compute, String instanceName) throws Exception {
        log.debug("***** GCLOUDCOMPUTEOPERATIONS - deleteInstance() - {} ******"+instanceName);
        Compute.Instances.Delete delete =
                compute.instances().delete(Constants.PROJECT_ID, Constants.ZONE_NAME, instanceName);
        return delete.execute();
    }


    private void waitForOperationCompletion(Compute compute, Operation op) throws Exception {

        // Call Compute Engine API operation and poll for operation completion status
        log.debug("Waiting for operation completion...");
        Operation.Error error = blockUntilComplete(compute, op);
        if (error == null) {
            log.info("Success!");
        } else {
            log.error(error.toPrettyString());
        }
    }
    private Operation.Error blockUntilComplete(Compute compute, Operation operation) throws Exception {
        long start = System.currentTimeMillis();
        final long pollInterval = 5 * 1000;
        String zone = operation.getZone();  // null for global/regional operations
        if (zone != null) {
            String[] bits = zone.split("/");
            zone = bits[bits.length - 1];
        }
        String status = operation.getStatus();
        String opId = operation.getName();
        while (operation != null && !status.equals("DONE")) {
            Thread.sleep(pollInterval);
            long elapsed = System.currentTimeMillis() - start;
            if (elapsed >= Constants.OPERATION_TIMEOUT_MILLIS) {
                throw new InterruptedException("Timed out waiting for operation to complete");
            }
            log.info("waiting...");
            if (zone != null) {
                Compute.ZoneOperations.Get get = compute.zoneOperations().get(Constants.PROJECT_ID, zone, opId);
                operation = get.execute();
            } else {
                Compute.GlobalOperations.Get get = compute.globalOperations().get(Constants.PROJECT_ID, opId);
                operation = get.execute();
            }
            if (operation != null) {
                status = operation.getStatus();
            }
        }
        return operation == null ? null : operation.getError();
    }

    private boolean checkForInstance(Compute compute) throws IOException {
        InstanceList instanceList = getInstancesList(compute);
        if (instanceList.getItems() == null || instanceList.getItems().size() < 1) {
            log.info("No instances found. Sign in to the Google Developers Console and create "
                    + "an instance at: https://console.developers.google.com/");
        }
        return  instanceList.getItems().stream().anyMatch((instance -> instance.getName().equals(instanceName)));
    }
}
