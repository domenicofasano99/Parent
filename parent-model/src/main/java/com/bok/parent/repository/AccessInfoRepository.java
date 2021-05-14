package com.bok.parent.repository;

import com.bok.parent.model.AccessInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccessInfoRepository extends JpaRepository<AccessInfo, Long> {

    @Query("select f from AccessInfo f where f.timestamp = (select max(ff.timestamp) from AccessInfo ff where ff.account.email = f.account.email)")
    Optional<AccessInfo> findLastAccessInfoByEmail(String email);
}
