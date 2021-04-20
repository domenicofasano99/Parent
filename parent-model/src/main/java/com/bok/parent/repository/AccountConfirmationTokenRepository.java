package com.bok.parent.repository;

import com.bok.parent.model.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    ConfirmationToken findByConfirmationToken(String confirmationToken);
}
