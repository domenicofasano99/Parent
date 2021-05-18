package com.bok.parent.service.bank;

import com.bok.bank.integration.dto.AccountInfoDTO;
import com.bok.bank.integration.dto.BankCheckRequestDTO;
import com.bok.bank.integration.service.AccountController;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "BankClient", url = "http://bank:8080/")
public interface BankClient extends AccountController {

    @Override
    @PostMapping("/checkCreation")
    Boolean checkCreation(@RequestBody BankCheckRequestDTO bankCheckRequestDTO);

    @Override
    @GetMapping("/profileInfo")
    AccountInfoDTO profileInfo(Long accountId);
}