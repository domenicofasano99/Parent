package com.bok.parent.integration.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 *
 */
@javax.annotation.Generated(
        value = "by gRPC proto compiler (version 1.15.0)",
        comments = "Source: parent.proto")
public final class ParentGrpc {

    public static final String SERVICE_NAME = "Parent";
    private static final int METHODID_GET_EMAIL = 0;
    // Static method descriptors that strictly reflect the proto.
    private static volatile io.grpc.MethodDescriptor<com.bok.parent.integration.grpc.EmailRequest,
            com.bok.parent.integration.grpc.EmailResponse> getGetEmailMethod;
    private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

    private ParentGrpc() {
    }

    @io.grpc.stub.annotations.RpcMethod(
            fullMethodName = SERVICE_NAME + '/' + "GetEmail",
            requestType = com.bok.parent.integration.grpc.EmailRequest.class,
            responseType = com.bok.parent.integration.grpc.EmailResponse.class,
            methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
    public static io.grpc.MethodDescriptor<com.bok.parent.integration.grpc.EmailRequest,
            com.bok.parent.integration.grpc.EmailResponse> getGetEmailMethod() {
        io.grpc.MethodDescriptor<com.bok.parent.integration.grpc.EmailRequest, com.bok.parent.integration.grpc.EmailResponse> getGetEmailMethod;
        if ((getGetEmailMethod = ParentGrpc.getGetEmailMethod) == null) {
            synchronized (ParentGrpc.class) {
                if ((getGetEmailMethod = ParentGrpc.getGetEmailMethod) == null) {
                    ParentGrpc.getGetEmailMethod = getGetEmailMethod =
                            io.grpc.MethodDescriptor.<com.bok.parent.integration.grpc.EmailRequest, com.bok.parent.integration.grpc.EmailResponse>newBuilder()
                                    .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                                    .setFullMethodName(generateFullMethodName(
                                            "Parent", "GetEmail"))
                                    .setSampledToLocalTracing(true)
                                    .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                                            com.bok.parent.integration.grpc.EmailRequest.getDefaultInstance()))
                                    .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                                            com.bok.parent.integration.grpc.EmailResponse.getDefaultInstance()))
                                    .setSchemaDescriptor(new ParentMethodDescriptorSupplier("GetEmail"))
                                    .build();
                }
            }
        }
        return getGetEmailMethod;
    }

    /**
     * Creates a new async stub that supports all call types for the service
     */
    public static ParentStub newStub(io.grpc.Channel channel) {
        return new ParentStub(channel);
    }

    /**
     * Creates a new blocking-style stub that supports unary and streaming output calls on the service
     */
    public static ParentBlockingStub newBlockingStub(
            io.grpc.Channel channel) {
        return new ParentBlockingStub(channel);
    }

    /**
     * Creates a new ListenableFuture-style stub that supports unary calls on the service
     */
    public static ParentFutureStub newFutureStub(
            io.grpc.Channel channel) {
        return new ParentFutureStub(channel);
    }

    public static io.grpc.ServiceDescriptor getServiceDescriptor() {
        io.grpc.ServiceDescriptor result = serviceDescriptor;
        if (result == null) {
            synchronized (ParentGrpc.class) {
                result = serviceDescriptor;
                if (result == null) {
                    serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
                            .setSchemaDescriptor(new ParentFileDescriptorSupplier())
                            .addMethod(getGetEmailMethod())
                            .build();
                }
            }
        }
        return result;
    }

    /**
     *
     */
    public static abstract class ParentImplBase implements io.grpc.BindableService {

        /**
         *
         */
        public void getEmail(com.bok.parent.integration.grpc.EmailRequest request,
                             io.grpc.stub.StreamObserver<com.bok.parent.integration.grpc.EmailResponse> responseObserver) {
            asyncUnimplementedUnaryCall(getGetEmailMethod(), responseObserver);
        }

        @java.lang.Override
        public final io.grpc.ServerServiceDefinition bindService() {
            return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
                    .addMethod(
                            getGetEmailMethod(),
                            asyncUnaryCall(
                                    new MethodHandlers<
                                            com.bok.parent.integration.grpc.EmailRequest,
                                            com.bok.parent.integration.grpc.EmailResponse>(
                                            this, METHODID_GET_EMAIL)))
                    .build();
        }
    }

    /**
     *
     */
    public static final class ParentStub extends io.grpc.stub.AbstractStub<ParentStub> {
        private ParentStub(io.grpc.Channel channel) {
            super(channel);
        }

        private ParentStub(io.grpc.Channel channel,
                           io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @java.lang.Override
        protected ParentStub build(io.grpc.Channel channel,
                                   io.grpc.CallOptions callOptions) {
            return new ParentStub(channel, callOptions);
        }

        /**
         *
         */
        public void getEmail(com.bok.parent.integration.grpc.EmailRequest request,
                             io.grpc.stub.StreamObserver<com.bok.parent.integration.grpc.EmailResponse> responseObserver) {
            asyncUnaryCall(
                    getChannel().newCall(getGetEmailMethod(), getCallOptions()), request, responseObserver);
        }
    }

    /**
     *
     */
    public static final class ParentBlockingStub extends io.grpc.stub.AbstractStub<ParentBlockingStub> {
        private ParentBlockingStub(io.grpc.Channel channel) {
            super(channel);
        }

        private ParentBlockingStub(io.grpc.Channel channel,
                                   io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @java.lang.Override
        protected ParentBlockingStub build(io.grpc.Channel channel,
                                           io.grpc.CallOptions callOptions) {
            return new ParentBlockingStub(channel, callOptions);
        }

        /**
         *
         */
        public com.bok.parent.integration.grpc.EmailResponse getEmail(com.bok.parent.integration.grpc.EmailRequest request) {
            return blockingUnaryCall(
                    getChannel(), getGetEmailMethod(), getCallOptions(), request);
        }
    }

    /**
     *
     */
    public static final class ParentFutureStub extends io.grpc.stub.AbstractStub<ParentFutureStub> {
        private ParentFutureStub(io.grpc.Channel channel) {
            super(channel);
        }

        private ParentFutureStub(io.grpc.Channel channel,
                                 io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @java.lang.Override
        protected ParentFutureStub build(io.grpc.Channel channel,
                                         io.grpc.CallOptions callOptions) {
            return new ParentFutureStub(channel, callOptions);
        }

        /**
         *
         */
        public com.google.common.util.concurrent.ListenableFuture<com.bok.parent.integration.grpc.EmailResponse> getEmail(
                com.bok.parent.integration.grpc.EmailRequest request) {
            return futureUnaryCall(
                    getChannel().newCall(getGetEmailMethod(), getCallOptions()), request);
        }
    }

    private static final class MethodHandlers<Req, Resp> implements
            io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
        private final ParentImplBase serviceImpl;
        private final int methodId;

        MethodHandlers(ParentImplBase serviceImpl, int methodId) {
            this.serviceImpl = serviceImpl;
            this.methodId = methodId;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("unchecked")
        public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch (methodId) {
                case METHODID_GET_EMAIL:
                    serviceImpl.getEmail((com.bok.parent.integration.grpc.EmailRequest) request,
                            (io.grpc.stub.StreamObserver<com.bok.parent.integration.grpc.EmailResponse>) responseObserver);
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

    private static abstract class ParentBaseDescriptorSupplier
            implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
        ParentBaseDescriptorSupplier() {
        }

        @java.lang.Override
        public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
            return com.bok.parent.integration.grpc.ParentProto.getDescriptor();
        }

        @java.lang.Override
        public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
            return getFileDescriptor().findServiceByName("Parent");
        }
    }

    private static final class ParentFileDescriptorSupplier
            extends ParentBaseDescriptorSupplier {
        ParentFileDescriptorSupplier() {
        }
    }

    private static final class ParentMethodDescriptorSupplier
            extends ParentBaseDescriptorSupplier
            implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
        private final String methodName;

        ParentMethodDescriptorSupplier(String methodName) {
            this.methodName = methodName;
        }

        @java.lang.Override
        public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
            return getServiceDescriptor().findMethodByName(methodName);
        }
    }
}
