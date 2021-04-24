package com.bok.parent.repository;

import com.bok.parent.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByEmail(String email);

    Optional<Account> findByEmailAndEnabledIsTrue(String email);

    Boolean existsByEmail(String email);

    @Query("SELECT a.id FROM Account a WHERE a.email=:email")
    Long findIdByEmail(@Param("email") String email);
}
