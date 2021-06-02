package com.bok.parent.be.grpc.client;

import com.bok.krypto.integration.grpc.KryptoGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Component
public class KryptoGrpcClient {

    @GrpcClient("krypto")
    KryptoGrpc.KryptoBlockingStub kryptoBlockingStub;

    @GrpcClient("krypto")
    KryptoGrpc.KryptoFutureStub kryptoFutureStub;
}
