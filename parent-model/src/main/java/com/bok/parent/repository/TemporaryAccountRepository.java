package com.bok.parent.repository;

import com.bok.parent.model.TemporaryAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TemporaryAccountRepository extends JpaRepository<TemporaryAccount, Long> {
    Optional<TemporaryAccount> findByConfirmationToken(String confirmationToken);

    Integer deleteByEmail(String email);

    boolean existsByEmail(String email);

    Optional<TemporaryAccount> findByEmail(String email);

}
