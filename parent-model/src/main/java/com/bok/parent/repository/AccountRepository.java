package com.bok.parent.repository;

import com.bok.parent.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByCredentials_Email(String email);

    Optional<Account> findByCredentials_EmailAndEnabledIsTrue(String email);

    Boolean existsByCredentials_Email(String email);

    @Query("SELECT a.id FROM Account a WHERE a.credentials.email=:email")
    Long findAccountIdByEmail(@Param("email") String email);

}
