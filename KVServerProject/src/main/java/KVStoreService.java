import generated.KVStore;
import generated.KVStoreServiceGrpc;
import io.grpc.stub.StreamObserver;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class KVStoreService extends KVStoreServiceGrpc.KVStoreServiceImplBase {
    private String key;
    private String value;
    private int size;
    private ConcurrentMap<String,String> kvStoreMap;
    private String path="KVStore.txt";
    private boolean isDataSet=false;
    private String getData;

    public KVStoreService(ConcurrentMap<String,String> kvStoreMap){
        this.kvStoreMap=kvStoreMap;
    }

    @Override
    public void set(KVStore.SetRequest request, StreamObserver<KVStore.SetResponse> responseObserver) {
        key =request.getKey();
        size= request.getSize();
        value=request.getValue();

        // store in map
        try {
            setValueInCache(new File(path),key,value);

        } catch (IOException e) {
            e.printStackTrace();
        }

        if(isDataSet){
            responseObserver.onNext(KVStore.SetResponse.newBuilder().setMessage("STORED").build());
            responseObserver.onCompleted();
        }else{
            responseObserver.onNext(KVStore.SetResponse.newBuilder().setMessage("NOT STORED").build());
            responseObserver.onCompleted();
        }

    }

    @Override
    public void get(KVStore.GetRequest request, StreamObserver<KVStore.GetResponse> responseObserver) {
        key =request.getKey();
        try {
            getValueFromCache(new File(path), key);
        } catch (IOException e) {
            e.printStackTrace();
        }

        responseObserver.onNext(KVStore.GetResponse.newBuilder().setValue(getData).build());
        responseObserver.onCompleted();
    }

    @Override
    public void append(KVStore.AppendRequest request, StreamObserver<KVStore.AppendResponse> responseObserver) {
        String key= request.getKey();
        String value = request.getValue();
        try {
            appendToCache(new File(path),key,value);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(isDataSet){
            responseObserver.onNext(KVStore.AppendResponse.newBuilder().setMessage("STORED").build());
            responseObserver.onCompleted();
        }else{
            responseObserver.onNext(KVStore.AppendResponse.newBuilder().setMessage("NOT STORED").build());
            responseObserver.onCompleted();
        }
    }


    public void appendToCache(File file,String key,String value){
        try{
            // Read data from file to a concurrent map.
            readFromFile(file);
            // Write to the map
            if(kvStoreMap.containsKey(key)) {
                // update
                //kvStoreMap.put(key,kvStoreMap.get(key)+""+value);
                // get the key replace oldvalue with new
               // kvStoreMap.replace(key,kvStoreMap.get(key),kvStoreMap.get(key)+" , "+value);
                kvStoreMap.replace(key,kvStoreMap.get(key), kvStoreMap.get(key)+" , "+value);
            }else{
                kvStoreMap.put(key,value);
            }
            // Write the map back to file

            writeToFile(file,key);
            // Send response to client - STORED
            isDataSet=true;

        }catch(Exception e){
            // send response to client - NOT STORED
            isDataSet=false;
        }

    }

    public  void   setValueInCache(File file, String key, String value) throws IOException {
        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw new IOException("Cannot create file KVStore.txt");
            }
        }

        try{
            // Read data from file to a concurrent map.
            readFromFile(file);
            // Write to the map
            if(kvStoreMap.containsKey(key)){
                // update
                //kvStoreMap.put(key,kvStoreMap.get(key)+""+value);
                kvStoreMap.replace(key,kvStoreMap.get(key),value);
            }else{
                // add value
                kvStoreMap.putIfAbsent(key,value);
            }
            // Write the map back to file

            writeToFile(file,key);
            // Send response to client - STORED
            isDataSet=true;

        }catch(Exception e){
            // send response to client - NOT STORED
            isDataSet=false;
        }

//        try{
//            // Read data from file to a concurrent map.
//            readFromFile(file);
//            // Write to the map
//            if(kvStoreMap.containsKey(key)) {
//                // update
//                if(kvStoreMap.get(key).contains(value)){
//
//                }else{
////                    List<String> a= Arrays.asList(kvStoreMap.get(key).replaceAll("\\[", "").replaceAll("\\]", "").split(","));
////                    a.add(value);
//                    kvStoreMap.replace(key,kvStoreMap.get(key)+" , "+value);
//                }
//            }else{
//                kvStoreMap.put(key,value);
//            }
//            // Write the map back to file
//
//            writeToFile(file,key);
//            // Send response to client - STORED
//            isDataSet=true;
//
//        }catch(Exception e){
//            // send response to client - NOT STORED
//            isDataSet=false;
//        }


    }

    // Read the values from file
    public void readFromFile(File file){
        try {
            BufferedReader br = new BufferedReader(new FileReader(file.getAbsoluteFile()));
            String line;
            while ((line = br.readLine()) != null) {
                String[] splitInput = line.split("-");
                try {
                    kvStoreMap.put(splitInput[0], splitInput[1]);
                } catch (Exception e) {
                    System.out.println( "buffered reader exception");
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Write data from hashmap to file
    public void writeToFile(File file, String key){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile(), false));
            BufferedWriter finalBw = bw;
            kvStoreMap.forEach((k, v) -> {
                try {
                    bw.write(k + "-" + v + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Get value from the Cache(hashmap)
    public   void getValueFromCache(File file, String key) throws IOException {
        // Read data from file to a concurrent map.
        readFromFile(file);


        // Fetch value from hashmap and sent to client
        if(kvStoreMap.keySet().contains(key)){
            getData=kvStoreMap.get(key);
        }else{
            getData="Key not present";
        }
    }

}
