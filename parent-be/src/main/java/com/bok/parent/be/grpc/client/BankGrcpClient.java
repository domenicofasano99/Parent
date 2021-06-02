package com.bok.parent.be.grpc.client;

import com.bok.bank.integration.grpc.BankGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Component
public class BankGrcpClient {

    @GrpcClient("bank")
    BankGrpc.BankBlockingStub bankBlockingStub;

    @GrpcClient("bank")
    BankGrpc.BankFutureStub bankFutureStub;

}
