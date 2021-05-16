package com.bok.parent.service.bank;

import com.bok.bank.integration.dto.BankCheckRequestDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//@FeignClient(name = "BankClient", url = "http://bank:8080/")
public interface BankClient {

    @PostMapping("/account/checkCreation")
    Boolean checkCreation(@RequestBody BankCheckRequestDTO checkPaymentAmountRequestDTO);


}