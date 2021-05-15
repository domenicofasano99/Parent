package com.bok.parent.repository;

import com.bok.parent.model.Account;
import com.bok.parent.model.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Optional;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    ConfirmationToken findByConfirmationToken(String confirmationToken);

    @Transactional
    Integer deleteByAccount(Account account);

    Optional<ConfirmationToken> findByAccount(Account account);
}
