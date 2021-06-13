package com.bok.parent.repository;

import com.bok.parent.model.TemporaryAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TemporaryAccountRepository extends JpaRepository<TemporaryAccount, Long> {
    Optional<TemporaryAccount> findByConfirmationToken(UUID confirmationToken);

    Integer deleteByEmail(String email);

    boolean existsByEmail(String email);

}
