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
    comments = "Source: KVStore.proto")
public final class KVStoreServiceGrpc {

  private KVStoreServiceGrpc() {}

  public static final String SERVICE_NAME = "KVStoreService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<generated.KVStore.SetRequest,
      generated.KVStore.SetResponse> getSetMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "set",
      requestType = generated.KVStore.SetRequest.class,
      responseType = generated.KVStore.SetResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<generated.KVStore.SetRequest,
      generated.KVStore.SetResponse> getSetMethod() {
    io.grpc.MethodDescriptor<generated.KVStore.SetRequest, generated.KVStore.SetResponse> getSetMethod;
    if ((getSetMethod = KVStoreServiceGrpc.getSetMethod) == null) {
      synchronized (KVStoreServiceGrpc.class) {
        if ((getSetMethod = KVStoreServiceGrpc.getSetMethod) == null) {
          KVStoreServiceGrpc.getSetMethod = getSetMethod = 
              io.grpc.MethodDescriptor.<generated.KVStore.SetRequest, generated.KVStore.SetResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "KVStoreService", "set"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  generated.KVStore.SetRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  generated.KVStore.SetResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new KVStoreServiceMethodDescriptorSupplier("set"))
                  .build();
          }
        }
     }
     return getSetMethod;
  }

  private static volatile io.grpc.MethodDescriptor<generated.KVStore.GetRequest,
      generated.KVStore.GetResponse> getGetMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "get",
      requestType = generated.KVStore.GetRequest.class,
      responseType = generated.KVStore.GetResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<generated.KVStore.GetRequest,
      generated.KVStore.GetResponse> getGetMethod() {
    io.grpc.MethodDescriptor<generated.KVStore.GetRequest, generated.KVStore.GetResponse> getGetMethod;
    if ((getGetMethod = KVStoreServiceGrpc.getGetMethod) == null) {
      synchronized (KVStoreServiceGrpc.class) {
        if ((getGetMethod = KVStoreServiceGrpc.getGetMethod) == null) {
          KVStoreServiceGrpc.getGetMethod = getGetMethod = 
              io.grpc.MethodDescriptor.<generated.KVStore.GetRequest, generated.KVStore.GetResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "KVStoreService", "get"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  generated.KVStore.GetRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  generated.KVStore.GetResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new KVStoreServiceMethodDescriptorSupplier("get"))
                  .build();
          }
        }
     }
     return getGetMethod;
  }

  private static volatile io.grpc.MethodDescriptor<generated.KVStore.AppendRequest,
      generated.KVStore.AppendResponse> getAppendMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "append",
      requestType = generated.KVStore.AppendRequest.class,
      responseType = generated.KVStore.AppendResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<generated.KVStore.AppendRequest,
      generated.KVStore.AppendResponse> getAppendMethod() {
    io.grpc.MethodDescriptor<generated.KVStore.AppendRequest, generated.KVStore.AppendResponse> getAppendMethod;
    if ((getAppendMethod = KVStoreServiceGrpc.getAppendMethod) == null) {
      synchronized (KVStoreServiceGrpc.class) {
        if ((getAppendMethod = KVStoreServiceGrpc.getAppendMethod) == null) {
          KVStoreServiceGrpc.getAppendMethod = getAppendMethod = 
              io.grpc.MethodDescriptor.<generated.KVStore.AppendRequest, generated.KVStore.AppendResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "KVStoreService", "append"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  generated.KVStore.AppendRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  generated.KVStore.AppendResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new KVStoreServiceMethodDescriptorSupplier("append"))
                  .build();
          }
        }
     }
     return getAppendMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static KVStoreServiceStub newStub(io.grpc.Channel channel) {
    return new KVStoreServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static KVStoreServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new KVStoreServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static KVStoreServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new KVStoreServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class KVStoreServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void set(generated.KVStore.SetRequest request,
        io.grpc.stub.StreamObserver<generated.KVStore.SetResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getSetMethod(), responseObserver);
    }

    /**
     */
    public void get(generated.KVStore.GetRequest request,
        io.grpc.stub.StreamObserver<generated.KVStore.GetResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetMethod(), responseObserver);
    }

    /**
     */
    public void append(generated.KVStore.AppendRequest request,
        io.grpc.stub.StreamObserver<generated.KVStore.AppendResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getAppendMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getSetMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                generated.KVStore.SetRequest,
                generated.KVStore.SetResponse>(
                  this, METHODID_SET)))
          .addMethod(
            getGetMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                generated.KVStore.GetRequest,
                generated.KVStore.GetResponse>(
                  this, METHODID_GET)))
          .addMethod(
            getAppendMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                generated.KVStore.AppendRequest,
                generated.KVStore.AppendResponse>(
                  this, METHODID_APPEND)))
          .build();
    }
  }

  /**
   */
  public static final class KVStoreServiceStub extends io.grpc.stub.AbstractStub<KVStoreServiceStub> {
    private KVStoreServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private KVStoreServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected KVStoreServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new KVStoreServiceStub(channel, callOptions);
    }

    /**
     */
    public void set(generated.KVStore.SetRequest request,
        io.grpc.stub.StreamObserver<generated.KVStore.SetResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSetMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void get(generated.KVStore.GetRequest request,
        io.grpc.stub.StreamObserver<generated.KVStore.GetResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void append(generated.KVStore.AppendRequest request,
        io.grpc.stub.StreamObserver<generated.KVStore.AppendResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getAppendMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class KVStoreServiceBlockingStub extends io.grpc.stub.AbstractStub<KVStoreServiceBlockingStub> {
    private KVStoreServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private KVStoreServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected KVStoreServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new KVStoreServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public generated.KVStore.SetResponse set(generated.KVStore.SetRequest request) {
      return blockingUnaryCall(
          getChannel(), getSetMethod(), getCallOptions(), request);
    }

    /**
     */
    public generated.KVStore.GetResponse get(generated.KVStore.GetRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetMethod(), getCallOptions(), request);
    }

    /**
     */
    public generated.KVStore.AppendResponse append(generated.KVStore.AppendRequest request) {
      return blockingUnaryCall(
          getChannel(), getAppendMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class KVStoreServiceFutureStub extends io.grpc.stub.AbstractStub<KVStoreServiceFutureStub> {
    private KVStoreServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private KVStoreServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected KVStoreServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new KVStoreServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<generated.KVStore.SetResponse> set(
        generated.KVStore.SetRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSetMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<generated.KVStore.GetResponse> get(
        generated.KVStore.GetRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<generated.KVStore.AppendResponse> append(
        generated.KVStore.AppendRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getAppendMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_SET = 0;
  private static final int METHODID_GET = 1;
  private static final int METHODID_APPEND = 2;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final KVStoreServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(KVStoreServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SET:
          serviceImpl.set((generated.KVStore.SetRequest) request,
              (io.grpc.stub.StreamObserver<generated.KVStore.SetResponse>) responseObserver);
          break;
        case METHODID_GET:
          serviceImpl.get((generated.KVStore.GetRequest) request,
              (io.grpc.stub.StreamObserver<generated.KVStore.GetResponse>) responseObserver);
          break;
        case METHODID_APPEND:
          serviceImpl.append((generated.KVStore.AppendRequest) request,
              (io.grpc.stub.StreamObserver<generated.KVStore.AppendResponse>) responseObserver);
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

  private static abstract class KVStoreServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    KVStoreServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return generated.KVStore.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("KVStoreService");
    }
  }

  private static final class KVStoreServiceFileDescriptorSupplier
      extends KVStoreServiceBaseDescriptorSupplier {
    KVStoreServiceFileDescriptorSupplier() {}
  }

  private static final class KVStoreServiceMethodDescriptorSupplier
      extends KVStoreServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    KVStoreServiceMethodDescriptorSupplier(String methodName) {
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
      synchronized (KVStoreServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new KVStoreServiceFileDescriptorSupplier())
              .addMethod(getSetMethod())
              .addMethod(getGetMethod())
              .addMethod(getAppendMethod())
              .build();
        }
      }
    }
    return result;
  }
}
