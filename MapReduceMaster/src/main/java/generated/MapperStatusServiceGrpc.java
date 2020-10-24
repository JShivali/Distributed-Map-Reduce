package generated;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.15.0)",
    comments = "Source: master.proto")
public final class MapperStatusServiceGrpc {

  private MapperStatusServiceGrpc() {}

  public static final String SERVICE_NAME = "MapperStatusService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<generated.Master.GetMapperStatusRequest,
      generated.Master.GetMapperStatusResponse> getGetMapperStatusMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getMapperStatus",
      requestType = generated.Master.GetMapperStatusRequest.class,
      responseType = generated.Master.GetMapperStatusResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<generated.Master.GetMapperStatusRequest,
      generated.Master.GetMapperStatusResponse> getGetMapperStatusMethod() {
    io.grpc.MethodDescriptor<generated.Master.GetMapperStatusRequest, generated.Master.GetMapperStatusResponse> getGetMapperStatusMethod;
    if ((getGetMapperStatusMethod = MapperStatusServiceGrpc.getGetMapperStatusMethod) == null) {
      synchronized (MapperStatusServiceGrpc.class) {
        if ((getGetMapperStatusMethod = MapperStatusServiceGrpc.getGetMapperStatusMethod) == null) {
          MapperStatusServiceGrpc.getGetMapperStatusMethod = getGetMapperStatusMethod = 
              io.grpc.MethodDescriptor.<generated.Master.GetMapperStatusRequest, generated.Master.GetMapperStatusResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "MapperStatusService", "getMapperStatus"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  generated.Master.GetMapperStatusRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  generated.Master.GetMapperStatusResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new MapperStatusServiceMethodDescriptorSupplier("getMapperStatus"))
                  .build();
          }
        }
     }
     return getGetMapperStatusMethod;
  }

  private static volatile io.grpc.MethodDescriptor<generated.Master.SetMapperStatusRequest,
      generated.Master.SetMapperStatusResponse> getSetMapperStatusMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "setMapperStatus",
      requestType = generated.Master.SetMapperStatusRequest.class,
      responseType = generated.Master.SetMapperStatusResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<generated.Master.SetMapperStatusRequest,
      generated.Master.SetMapperStatusResponse> getSetMapperStatusMethod() {
    io.grpc.MethodDescriptor<generated.Master.SetMapperStatusRequest, generated.Master.SetMapperStatusResponse> getSetMapperStatusMethod;
    if ((getSetMapperStatusMethod = MapperStatusServiceGrpc.getSetMapperStatusMethod) == null) {
      synchronized (MapperStatusServiceGrpc.class) {
        if ((getSetMapperStatusMethod = MapperStatusServiceGrpc.getSetMapperStatusMethod) == null) {
          MapperStatusServiceGrpc.getSetMapperStatusMethod = getSetMapperStatusMethod = 
              io.grpc.MethodDescriptor.<generated.Master.SetMapperStatusRequest, generated.Master.SetMapperStatusResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "MapperStatusService", "setMapperStatus"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  generated.Master.SetMapperStatusRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  generated.Master.SetMapperStatusResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new MapperStatusServiceMethodDescriptorSupplier("setMapperStatus"))
                  .build();
          }
        }
     }
     return getSetMapperStatusMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static MapperStatusServiceStub newStub(io.grpc.Channel channel) {
    return new MapperStatusServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static MapperStatusServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new MapperStatusServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static MapperStatusServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new MapperStatusServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class MapperStatusServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void getMapperStatus(generated.Master.GetMapperStatusRequest request,
        io.grpc.stub.StreamObserver<generated.Master.GetMapperStatusResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetMapperStatusMethod(), responseObserver);
    }

    /**
     */
    public void setMapperStatus(generated.Master.SetMapperStatusRequest request,
        io.grpc.stub.StreamObserver<generated.Master.SetMapperStatusResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getSetMapperStatusMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGetMapperStatusMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                generated.Master.GetMapperStatusRequest,
                generated.Master.GetMapperStatusResponse>(
                  this, METHODID_GET_MAPPER_STATUS)))
          .addMethod(
            getSetMapperStatusMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                generated.Master.SetMapperStatusRequest,
                generated.Master.SetMapperStatusResponse>(
                  this, METHODID_SET_MAPPER_STATUS)))
          .build();
    }
  }

  /**
   */
  public static final class MapperStatusServiceStub extends io.grpc.stub.AbstractStub<MapperStatusServiceStub> {
    private MapperStatusServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private MapperStatusServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MapperStatusServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new MapperStatusServiceStub(channel, callOptions);
    }

    /**
     */
    public void getMapperStatus(generated.Master.GetMapperStatusRequest request,
        io.grpc.stub.StreamObserver<generated.Master.GetMapperStatusResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetMapperStatusMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void setMapperStatus(generated.Master.SetMapperStatusRequest request,
        io.grpc.stub.StreamObserver<generated.Master.SetMapperStatusResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSetMapperStatusMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class MapperStatusServiceBlockingStub extends io.grpc.stub.AbstractStub<MapperStatusServiceBlockingStub> {
    private MapperStatusServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private MapperStatusServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MapperStatusServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new MapperStatusServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public generated.Master.GetMapperStatusResponse getMapperStatus(generated.Master.GetMapperStatusRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetMapperStatusMethod(), getCallOptions(), request);
    }

    /**
     */
    public generated.Master.SetMapperStatusResponse setMapperStatus(generated.Master.SetMapperStatusRequest request) {
      return blockingUnaryCall(
          getChannel(), getSetMapperStatusMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class MapperStatusServiceFutureStub extends io.grpc.stub.AbstractStub<MapperStatusServiceFutureStub> {
    private MapperStatusServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private MapperStatusServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MapperStatusServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new MapperStatusServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<generated.Master.GetMapperStatusResponse> getMapperStatus(
        generated.Master.GetMapperStatusRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetMapperStatusMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<generated.Master.SetMapperStatusResponse> setMapperStatus(
        generated.Master.SetMapperStatusRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSetMapperStatusMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_MAPPER_STATUS = 0;
  private static final int METHODID_SET_MAPPER_STATUS = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final MapperStatusServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(MapperStatusServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_MAPPER_STATUS:
          serviceImpl.getMapperStatus((generated.Master.GetMapperStatusRequest) request,
              (io.grpc.stub.StreamObserver<generated.Master.GetMapperStatusResponse>) responseObserver);
          break;
        case METHODID_SET_MAPPER_STATUS:
          serviceImpl.setMapperStatus((generated.Master.SetMapperStatusRequest) request,
              (io.grpc.stub.StreamObserver<generated.Master.SetMapperStatusResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class MapperStatusServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    MapperStatusServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return generated.Master.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("MapperStatusService");
    }
  }

  private static final class MapperStatusServiceFileDescriptorSupplier
      extends MapperStatusServiceBaseDescriptorSupplier {
    MapperStatusServiceFileDescriptorSupplier() {}
  }

  private static final class MapperStatusServiceMethodDescriptorSupplier
      extends MapperStatusServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    MapperStatusServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (MapperStatusServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new MapperStatusServiceFileDescriptorSupplier())
              .addMethod(getGetMapperStatusMethod())
              .addMethod(getSetMapperStatusMethod())
              .build();
        }
      }
    }
    return result;
  }
}
