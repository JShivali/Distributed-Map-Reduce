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
public final class StartMapReduceServiceGrpc {

  private StartMapReduceServiceGrpc() {}

  public static final String SERVICE_NAME = "StartMapReduceService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<generated.Master.MapReduceInputParams,
      generated.Master.MapReduceResponse> getMapReduceMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "mapReduce",
      requestType = generated.Master.MapReduceInputParams.class,
      responseType = generated.Master.MapReduceResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<generated.Master.MapReduceInputParams,
      generated.Master.MapReduceResponse> getMapReduceMethod() {
    io.grpc.MethodDescriptor<generated.Master.MapReduceInputParams, generated.Master.MapReduceResponse> getMapReduceMethod;
    if ((getMapReduceMethod = StartMapReduceServiceGrpc.getMapReduceMethod) == null) {
      synchronized (StartMapReduceServiceGrpc.class) {
        if ((getMapReduceMethod = StartMapReduceServiceGrpc.getMapReduceMethod) == null) {
          StartMapReduceServiceGrpc.getMapReduceMethod = getMapReduceMethod = 
              io.grpc.MethodDescriptor.<generated.Master.MapReduceInputParams, generated.Master.MapReduceResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "StartMapReduceService", "mapReduce"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  generated.Master.MapReduceInputParams.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  generated.Master.MapReduceResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new StartMapReduceServiceMethodDescriptorSupplier("mapReduce"))
                  .build();
          }
        }
     }
     return getMapReduceMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static StartMapReduceServiceStub newStub(io.grpc.Channel channel) {
    return new StartMapReduceServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static StartMapReduceServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new StartMapReduceServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static StartMapReduceServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new StartMapReduceServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class StartMapReduceServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void mapReduce(generated.Master.MapReduceInputParams request,
        io.grpc.stub.StreamObserver<generated.Master.MapReduceResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getMapReduceMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getMapReduceMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                generated.Master.MapReduceInputParams,
                generated.Master.MapReduceResponse>(
                  this, METHODID_MAP_REDUCE)))
          .build();
    }
  }

  /**
   */
  public static final class StartMapReduceServiceStub extends io.grpc.stub.AbstractStub<StartMapReduceServiceStub> {
    private StartMapReduceServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private StartMapReduceServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StartMapReduceServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new StartMapReduceServiceStub(channel, callOptions);
    }

    /**
     */
    public void mapReduce(generated.Master.MapReduceInputParams request,
        io.grpc.stub.StreamObserver<generated.Master.MapReduceResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getMapReduceMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class StartMapReduceServiceBlockingStub extends io.grpc.stub.AbstractStub<StartMapReduceServiceBlockingStub> {
    private StartMapReduceServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private StartMapReduceServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StartMapReduceServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new StartMapReduceServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public generated.Master.MapReduceResponse mapReduce(generated.Master.MapReduceInputParams request) {
      return blockingUnaryCall(
          getChannel(), getMapReduceMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class StartMapReduceServiceFutureStub extends io.grpc.stub.AbstractStub<StartMapReduceServiceFutureStub> {
    private StartMapReduceServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private StartMapReduceServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StartMapReduceServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new StartMapReduceServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<generated.Master.MapReduceResponse> mapReduce(
        generated.Master.MapReduceInputParams request) {
      return futureUnaryCall(
          getChannel().newCall(getMapReduceMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_MAP_REDUCE = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final StartMapReduceServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(StartMapReduceServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_MAP_REDUCE:
          serviceImpl.mapReduce((generated.Master.MapReduceInputParams) request,
              (io.grpc.stub.StreamObserver<generated.Master.MapReduceResponse>) responseObserver);
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

  private static abstract class StartMapReduceServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    StartMapReduceServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return generated.Master.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("StartMapReduceService");
    }
  }

  private static final class StartMapReduceServiceFileDescriptorSupplier
      extends StartMapReduceServiceBaseDescriptorSupplier {
    StartMapReduceServiceFileDescriptorSupplier() {}
  }

  private static final class StartMapReduceServiceMethodDescriptorSupplier
      extends StartMapReduceServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    StartMapReduceServiceMethodDescriptorSupplier(String methodName) {
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
      synchronized (StartMapReduceServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new StartMapReduceServiceFileDescriptorSupplier())
              .addMethod(getMapReduceMethod())
              .build();
        }
      }
    }
    return result;
  }
}
