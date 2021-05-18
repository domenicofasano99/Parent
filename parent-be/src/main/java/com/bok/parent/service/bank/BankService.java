package com.bok.parent.service.bank;

import com.bok.bank.integration.dto.AccountInfoDTO;
import com.bok.bank.integration.dto.BankCheckRequestDTO;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BankService {

    @Autowired
    BankClient bankClient;

    public Boolean checkCreation(BankCheckRequestDTO bankCheckRequestDTO) {
        try {
            return bankClient.checkCreation(bankCheckRequestDTO);
        } catch (FeignException ex) {
            log.error("Error while communicating with bank");
        } catch (Exception e) {
            log.error("Error in request to bank: {} ", e.getMessage());
        }
        return Boolean.TRUE;
    }

    public AccountInfoDTO profileInfo(Long accountId) {
        return null;
    }
}
