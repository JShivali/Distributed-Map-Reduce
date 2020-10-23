import java.io.InputStream;

public class ConfigParams {


    String kVStoreIp ;
    int kVStorePort;
    String masterIP;
    int masterPort;
    String fileLocation;
    int mapperCount;
    int reducerCount;
    String mapperFunction;
    String reducerFunction;
    String outputFile;
    String masterVMName;

    public String getMasterVMName() {
        return masterVMName;
    }

    public void setMasterVMName(String masterVMName) {
        this.masterVMName = masterVMName;
    }

    public String getKvstoreVMName() {
        return kvstoreVMName;
    }

    public void setKvstoreVMName(String kvstoreVMName) {
        this.kvstoreVMName = kvstoreVMName;
    }

    String kvstoreVMName;

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public String getkVStoreIp() {
        return kVStoreIp;
    }

    public void setkVStoreIp(String kVStoreIp) {
        this.kVStoreIp = kVStoreIp;
    }

    public int getkVStorePort() {
        return kVStorePort;
    }

    public void setkVStorePort(int kVStorePort) {
        this.kVStorePort = kVStorePort;
    }

    public String getMasterIP() {
        return masterIP;
    }

    public void setMasterIP(String masterIP) {
        this.masterIP = masterIP;
    }

    public int getMasterPort() {
        return masterPort;
    }

    public void setMasterPort(int masterPort) {
        this.masterPort = masterPort;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public int getMapperCount() {
        return mapperCount;
    }

    public void setMapperCount(int mapperCount) {
        this.mapperCount = mapperCount;
    }

    public int getReducerCount() {
        return reducerCount;
    }

    public void setReducerCount(int reducerCount) {
        this.reducerCount = reducerCount;
    }

    public String getMapperFunction() {
        return mapperFunction;
    }

    public void setMapperFunction(String mapperFunction) {
        this.mapperFunction = mapperFunction;
    }

    public String getReducerFunction() {
        return reducerFunction;
    }

    public void setReducerFunction(String reducerFunction) {
        this.reducerFunction = reducerFunction;
    }


}
