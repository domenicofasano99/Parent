package com.bok.parent.repository;

import com.bok.parent.model.AccessInfo;
import com.bok.parent.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface AccessInfoRepository extends JpaRepository<AccessInfo, Long> {

    @Query("select f from AccessInfo f where f.timestamp = (select max(ff.timestamp) from AccessInfo ff where ff.account.credentials.email = f.account.credentials.email) and f.account.credentials.email = :email")
    Optional<AccessInfo> findLastAccessInfoByEmail(String email);


    @Transactional
    Integer deleteByAccount(Account account);

    List<AccessInfo> findByAccount(Account account);
}
