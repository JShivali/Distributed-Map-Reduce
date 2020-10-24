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
public final class ReducerStatusServiceGrpc {

  private ReducerStatusServiceGrpc() {}

  public static final String SERVICE_NAME = "ReducerStatusService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<generated.Master.GetReducerStatusRequest,
      generated.Master.GetReducerStatusResponse> getGetReducerStatusMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getReducerStatus",
      requestType = generated.Master.GetReducerStatusRequest.class,
      responseType = generated.Master.GetReducerStatusResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<generated.Master.GetReducerStatusRequest,
      generated.Master.GetReducerStatusResponse> getGetReducerStatusMethod() {
    io.grpc.MethodDescriptor<generated.Master.GetReducerStatusRequest, generated.Master.GetReducerStatusResponse> getGetReducerStatusMethod;
    if ((getGetReducerStatusMethod = ReducerStatusServiceGrpc.getGetReducerStatusMethod) == null) {
      synchronized (ReducerStatusServiceGrpc.class) {
        if ((getGetReducerStatusMethod = ReducerStatusServiceGrpc.getGetReducerStatusMethod) == null) {
          ReducerStatusServiceGrpc.getGetReducerStatusMethod = getGetReducerStatusMethod = 
              io.grpc.MethodDescriptor.<generated.Master.GetReducerStatusRequest, generated.Master.GetReducerStatusResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "ReducerStatusService", "getReducerStatus"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  generated.Master.GetReducerStatusRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  generated.Master.GetReducerStatusResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new ReducerStatusServiceMethodDescriptorSupplier("getReducerStatus"))
                  .build();
          }
        }
     }
     return getGetReducerStatusMethod;
  }

  private static volatile io.grpc.MethodDescriptor<generated.Master.SetReducerStatusRequest,
      generated.Master.SetReducerStatusResponse> getSetReducerStatusMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "setReducerStatus",
      requestType = generated.Master.SetReducerStatusRequest.class,
      responseType = generated.Master.SetReducerStatusResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<generated.Master.SetReducerStatusRequest,
      generated.Master.SetReducerStatusResponse> getSetReducerStatusMethod() {
    io.grpc.MethodDescriptor<generated.Master.SetReducerStatusRequest, generated.Master.SetReducerStatusResponse> getSetReducerStatusMethod;
    if ((getSetReducerStatusMethod = ReducerStatusServiceGrpc.getSetReducerStatusMethod) == null) {
      synchronized (ReducerStatusServiceGrpc.class) {
        if ((getSetReducerStatusMethod = ReducerStatusServiceGrpc.getSetReducerStatusMethod) == null) {
          ReducerStatusServiceGrpc.getSetReducerStatusMethod = getSetReducerStatusMethod = 
              io.grpc.MethodDescriptor.<generated.Master.SetReducerStatusRequest, generated.Master.SetReducerStatusResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "ReducerStatusService", "setReducerStatus"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  generated.Master.SetReducerStatusRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  generated.Master.SetReducerStatusResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new ReducerStatusServiceMethodDescriptorSupplier("setReducerStatus"))
                  .build();
          }
        }
     }
     return getSetReducerStatusMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ReducerStatusServiceStub newStub(io.grpc.Channel channel) {
    return new ReducerStatusServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ReducerStatusServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new ReducerStatusServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ReducerStatusServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new ReducerStatusServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class ReducerStatusServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void getReducerStatus(generated.Master.GetReducerStatusRequest request,
        io.grpc.stub.StreamObserver<generated.Master.GetReducerStatusResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetReducerStatusMethod(), responseObserver);
    }

    /**
     */
    public void setReducerStatus(generated.Master.SetReducerStatusRequest request,
        io.grpc.stub.StreamObserver<generated.Master.SetReducerStatusResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getSetReducerStatusMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGetReducerStatusMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                generated.Master.GetReducerStatusRequest,
                generated.Master.GetReducerStatusResponse>(
                  this, METHODID_GET_REDUCER_STATUS)))
          .addMethod(
            getSetReducerStatusMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                generated.Master.SetReducerStatusRequest,
                generated.Master.SetReducerStatusResponse>(
                  this, METHODID_SET_REDUCER_STATUS)))
          .build();
    }
  }

  /**
   */
  public static final class ReducerStatusServiceStub extends io.grpc.stub.AbstractStub<ReducerStatusServiceStub> {
    private ReducerStatusServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ReducerStatusServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ReducerStatusServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ReducerStatusServiceStub(channel, callOptions);
    }

    /**
     */
    public void getReducerStatus(generated.Master.GetReducerStatusRequest request,
        io.grpc.stub.StreamObserver<generated.Master.GetReducerStatusResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetReducerStatusMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void setReducerStatus(generated.Master.SetReducerStatusRequest request,
        io.grpc.stub.StreamObserver<generated.Master.SetReducerStatusResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSetReducerStatusMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class ReducerStatusServiceBlockingStub extends io.grpc.stub.AbstractStub<ReducerStatusServiceBlockingStub> {
    private ReducerStatusServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ReducerStatusServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ReducerStatusServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ReducerStatusServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public generated.Master.GetReducerStatusResponse getReducerStatus(generated.Master.GetReducerStatusRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetReducerStatusMethod(), getCallOptions(), request);
    }

    /**
     */
    public generated.Master.SetReducerStatusResponse setReducerStatus(generated.Master.SetReducerStatusRequest request) {
      return blockingUnaryCall(
          getChannel(), getSetReducerStatusMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class ReducerStatusServiceFutureStub extends io.grpc.stub.AbstractStub<ReducerStatusServiceFutureStub> {
    private ReducerStatusServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ReducerStatusServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ReducerStatusServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ReducerStatusServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<generated.Master.GetReducerStatusResponse> getReducerStatus(
        generated.Master.GetReducerStatusRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetReducerStatusMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<generated.Master.SetReducerStatusResponse> setReducerStatus(
        generated.Master.SetReducerStatusRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSetReducerStatusMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_REDUCER_STATUS = 0;
  private static final int METHODID_SET_REDUCER_STATUS = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final ReducerStatusServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(ReducerStatusServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_REDUCER_STATUS:
          serviceImpl.getReducerStatus((generated.Master.GetReducerStatusRequest) request,
              (io.grpc.stub.StreamObserver<generated.Master.GetReducerStatusResponse>) responseObserver);
          break;
        case METHODID_SET_REDUCER_STATUS:
          serviceImpl.setReducerStatus((generated.Master.SetReducerStatusRequest) request,
              (io.grpc.stub.StreamObserver<generated.Master.SetReducerStatusResponse>) responseObserver);
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

  private static abstract class ReducerStatusServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ReducerStatusServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return generated.Master.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ReducerStatusService");
    }
  }

  private static final class ReducerStatusServiceFileDescriptorSupplier
      extends ReducerStatusServiceBaseDescriptorSupplier {
    ReducerStatusServiceFileDescriptorSupplier() {}
  }

  private static final class ReducerStatusServiceMethodDescriptorSupplier
      extends ReducerStatusServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    ReducerStatusServiceMethodDescriptorSupplier(String methodName) {
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
      synchronized (ReducerStatusServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ReducerStatusServiceFileDescriptorSupplier())
              .addMethod(getGetReducerStatusMethod())
              .addMethod(getSetReducerStatusMethod())
              .build();
        }
      }
    }
    return result;
  }
}
