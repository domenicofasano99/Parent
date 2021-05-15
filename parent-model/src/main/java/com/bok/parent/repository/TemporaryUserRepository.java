package com.bok.parent.repository;

import com.bok.parent.model.Account;
import com.bok.parent.model.AccountTemporaryDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;
import java.util.Optional;

public interface TemporaryUserRepository extends JpaRepository<AccountTemporaryDetails, Long> {

    Optional<AccountTemporaryDetails> findByAccount(Account account);

    @Modifying
    void deleteByAccount_Id(Long id);

    @Transactional
    Integer deleteByAccount(Account account);

}
