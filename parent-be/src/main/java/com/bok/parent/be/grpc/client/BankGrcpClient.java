package com.bok.parent.be.grpc.client;

import com.bok.bank.integration.grpc.AccountCreationCheckRequest;
import com.bok.bank.integration.grpc.BankGrpc;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

@Slf4j
@Component
public class BankGrcpClient {

    @GrpcClient("bank")
    BankGrpc.BankBlockingStub bankBlockingStub;

    @GrpcClient("bank")
    BankGrpc.BankFutureStub bankFutureStub;


    public boolean checkCreation(String fiscalCode, String vatNumber, boolean business) {
        AccountCreationCheckRequest.Builder requestBuilder = AccountCreationCheckRequest.newBuilder();
        requestBuilder.setFiscalCode(fiscalCode);
        if (!isNull(vatNumber)) {
            requestBuilder.setVatNumber(vatNumber);
        }
        requestBuilder.setBusiness(business);
        try {
            return bankBlockingStub.accountCreationCheck(requestBuilder.build()).getPermitted();
        } catch (FeignException ex) {
            log.error("Error while communicating with bank");
        } catch (Exception e) {
            log.error("Error in request to bank: {} ", e.getMessage());
        }
        return Boolean.TRUE;
    }

}
