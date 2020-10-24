import generated.KVStore;
import generated.MapperStatusServiceGrpc;
import generated.Master;
import io.grpc.stub.StreamObserver;

import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MapperStatusService extends MapperStatusServiceGrpc.MapperStatusServiceImplBase {

    private String mapperName;
    private String mapperStatus;
    ConcurrentMap<String,String> mapperStatusMap = new ConcurrentHashMap<>();


    @Override
    public void getMapperStatus(Master.GetMapperStatusRequest request, StreamObserver<Master.GetMapperStatusResponse> responseObserver) {
        mapperName= request.getMapperName();
        HashSet<String> s=new HashSet<>();
        s.addAll(mapperStatusMap.values());
        if(s.size()==1 && s.contains("completed")){
            responseObserver.onNext(Master.GetMapperStatusResponse.newBuilder().setMapperStatus("completed").build());
            responseObserver.onCompleted();
        }else{
            responseObserver.onNext(Master.GetMapperStatusResponse.newBuilder().setMapperStatus("inprogress").build());
            responseObserver.onCompleted();
        }

    }

    @Override
    public void setMapperStatus(Master.SetMapperStatusRequest request, StreamObserver<Master.SetMapperStatusResponse> responseObserver) {
        mapperName=request.getMapperName();
        mapperStatus=request.getMapperStatus();
        mapperStatusMap.put(mapperName,mapperStatus);
        MapReduceMasterCoordinator.mapperTaskStatus.put(mapperName,mapperStatus);
    }
}
