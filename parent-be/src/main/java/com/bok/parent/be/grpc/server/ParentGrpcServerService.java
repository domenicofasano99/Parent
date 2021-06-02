package com.bok.parent.be.grpc.server;

import com.bok.parent.be.helper.AccountHelper;
import com.bok.parent.integration.grpc.EmailRequest;
import com.bok.parent.integration.grpc.EmailResponse;
import com.bok.parent.integration.grpc.ParentGrpc;
import com.bok.parent.model.Account;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class ParentGrpcServerService extends ParentGrpc.ParentImplBase {

    @Autowired
    AccountHelper accountHelper;

    @Override
    public void getEmail(EmailRequest request, StreamObserver<EmailResponse> responseObserver) {
        Account a = accountHelper.findById(request.getAccountId());

        EmailResponse.Builder responseBuilder = EmailResponse.newBuilder();

        responseObserver.onNext(responseBuilder.setEmail(a.getCredentials().getEmail()).build());
        responseObserver.onCompleted();
    }
}