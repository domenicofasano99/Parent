package com.bok.parent.repository;

import com.bok.parent.model.AccountConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountConfirmationTokenRepository extends JpaRepository<AccountConfirmationToken, Long> {
    AccountConfirmationToken findByConfirmationToken(String confirmationToken);
}
