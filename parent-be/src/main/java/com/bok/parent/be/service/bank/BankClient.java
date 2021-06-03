package com.bok.parent.be.service.bank;

import com.bok.bank.integration.dto.AccountInfoDTO;
import com.bok.bank.integration.service.AccountController;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "BankClient", url = "http://bank:8080/")
public interface BankClient extends AccountController {

    @Override
    @GetMapping("/profileInfo")
    AccountInfoDTO profileInfo(Long accountId);
}