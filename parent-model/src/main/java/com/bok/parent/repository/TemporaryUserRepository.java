package com.bok.parent.repository;

import com.bok.parent.model.TemporaryUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemporaryUserRepository extends JpaRepository<TemporaryUser, Long> {

    TemporaryUser findByAccount_Id(Long accountId);
}
