package com.bok.parent.be.service.bank;

import com.bok.parent.be.grpc.client.BankGrcpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BankService {

    @Autowired
    BankGrcpClient bankGrcpClient;

    public boolean checkCreation(String fiscalCode, String vatNumber, boolean business) {
        return bankGrcpClient.checkCreation(fiscalCode, vatNumber, business);
    }
}
