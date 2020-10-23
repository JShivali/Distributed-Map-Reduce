import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MasterInitiator {
    public static void main(String[] args) throws IOException, InterruptedException {

        ConcurrentMap<String, String> mappersMap = new ConcurrentHashMap<String, String>();     // mapper identity, state
        ConcurrentMap<String, String> reducersMap = new ConcurrentHashMap<String, String>();    //reducer ip, state
        ConcurrentMap<String, String> tasks = new ConcurrentHashMap<String, String>();
        Logger log = Logger.getLogger(MasterInitiator.class);

        log.debug("***** MASTER IS STARTING **** ");

        Server server = ServerBuilder.forPort(9090)
                .addService(new StartMRService()).build();

        server.start();
        log.debug("***** MASTER STARTED **** ");
        System.out.println("Master started");

        server.awaitTermination();
    }
}
