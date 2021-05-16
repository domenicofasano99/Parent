package com.bok.parent.service.bank;

import com.bok.bank.integration.dto.BankCheckRequestDTO;
import org.springframework.stereotype.Service;

@Service
public class BankService {

    //@Autowired
    //BankClient bankClient;

    public Boolean preauthorize(String fiscalCode, String vatNumber, Boolean business) {
        BankCheckRequestDTO request = new BankCheckRequestDTO(fiscalCode, vatNumber, business);
        //return bankClient.checkCreation(request);
        return true;
    }

}
