import generated.KVStore;
import generated.Master;
import generated.StartMapReduceServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class StartMRService extends StartMapReduceServiceGrpc.StartMapReduceServiceImplBase {
    ConcurrentMap<String, String> mappersMap = new ConcurrentHashMap<String, String>();     // mapper identity, state
    ConcurrentMap<String, String> reducersMap = new ConcurrentHashMap<String, String>();    //reducer ip, state
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

    Logger log = Logger.getLogger(MasterInitiator.class);
    public StartMRService(){

    }

    InputStream inputStream;



    @Override
    public void mapReduce(Master.MapReduceInputParams request, StreamObserver<Master.MapReduceResponse> responseObserver) {
        // get details from the request and call the MapReduceCoordinator AKA the master node
        log.debug("**** START MAP REDUCE HIT *****");
        kVStoreIp= request.getKVStoreIPAddress();
        kVStorePort= request.getKvstorePort();
        fileLocation = request.getFileLocation();
        mapperCount = request.getMapperCount();
        reducerCount = request.getReducerCount();
        masterIP = request.getMasterIPAddress();
        masterPort = request.getMasterPort();
        mapperFunction=request.getMapperFunction();
        reducerFunction=request.getReducerFunction();
        outputFile= request.getOutputFile();

        MapReduceMasterCoordinator mapReduceMasterCoordinator= new MapReduceMasterCoordinator(kVStoreIp,kVStorePort,fileLocation,mapperCount,reducerCount,masterIP,masterPort,mapperFunction,reducerFunction,outputFile);
        mapReduceMasterCoordinator.mapReduce();

    }
}
