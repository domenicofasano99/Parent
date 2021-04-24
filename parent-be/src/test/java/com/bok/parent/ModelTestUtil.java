package com.bok.parent;

import com.bok.parent.helper.AccountHelper;
import com.bok.parent.model.Account;
import com.bok.parent.repository.AccountRepository;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModelTestUtil {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountHelper accountHelper;

    public Account enableAccount(Account account){
        account.setEnabled(true);
        return accountRepository.save(account);
    }
}
