package com.bok.parent.helper;

import com.bok.parent.dto.RegisterAccount;
import com.bok.parent.exception.EmailAlreadyExistsException;
import com.bok.parent.message.KryptoAccountCreationMessage;
import com.bok.parent.model.Account;
import com.bok.parent.repository.AccountRepository;
import com.bok.parent.utils.CryptoUtils;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;


@Component
@Slf4j
public class AccountHelper {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CryptoUtils cryptoUtils;

    @Autowired
    MessageHelper messageHelper;

    public Account register(RegisterAccount registerAccount) {
        Preconditions.checkArgument(Objects.nonNull(registerAccount.password));
        Preconditions.checkArgument(Objects.nonNull(registerAccount.email));
        Preconditions.checkArgument(Objects.nonNull(registerAccount.name));
        Preconditions.checkArgument(Objects.nonNull(registerAccount.surname));

        if (accountRepository.existsByEmail(registerAccount.email)) {
            throw new EmailAlreadyExistsException("Account already registered.");
        }
        Account account = new Account();
        account.setEmail(registerAccount.email);
        account.setPassword(cryptoUtils.encryptPassword(registerAccount.password));
        account.setEnabled(true);
        account.setRole(Account.Role.USER);
        account = accountRepository.save(account);
        notifyServices(account.getId(), registerAccount);
        return account;
    }

    public Optional<Account> findByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    public Long findIdByEmail(String email) {
        return accountRepository.findIdByEmail(email);
    }

    private void notifyServices(Long accountId, RegisterAccount registerAccount) {
        KryptoAccountCreationMessage kryptoMessage = new KryptoAccountCreationMessage();
        kryptoMessage.accountId = accountId;
        kryptoMessage.email = registerAccount.email;
        kryptoMessage.name = registerAccount.name;
        kryptoMessage.surname = registerAccount.surname;

        messageHelper.send(kryptoMessage);
    }
}
