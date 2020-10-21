import generated.KVStoreServiceGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KVServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        ConcurrentMap<String,String> kvStoreMap= new ConcurrentHashMap<>();
        Server server=ServerBuilder.forPort(9091).addService(new KVStoreService(kvStoreMap)).build();
        server.start();
        System.out.println("KV server started");
        server.awaitTermination();
    }

}
