package com.bok.parent.repository;

import com.bok.parent.model.TemporaryUserData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemporaryUserDataRepository extends JpaRepository<TemporaryUserData, Long> {

    TemporaryUserData findByAccount_Id(Long accountId);
}
