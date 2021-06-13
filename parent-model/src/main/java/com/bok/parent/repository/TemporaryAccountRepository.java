package com.bok.parent.repository;

import com.bok.parent.model.TemporaryAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface TemporaryAccountRepository extends JpaRepository<TemporaryAccount, Long> {
    Optional<TemporaryAccount> findByConfirmationToken(UUID confirmationToken);

    Integer deleteByEmail(String email);

    boolean existsByEmail(String email);

    Optional<TemporaryAccount> findByEmail(String email);

}
