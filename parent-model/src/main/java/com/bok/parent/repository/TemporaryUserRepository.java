package com.bok.parent.repository;

import com.bok.parent.model.TemporaryUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface TemporaryUserRepository extends JpaRepository<TemporaryUser, Long> {

    TemporaryUser findByAccount_Id(Long accountId);


    @Modifying
    void deleteByAccount_Id(Long id);
}
