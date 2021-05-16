package com.bok.parent.repository;

import com.bok.parent.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByTokenString(String token);

    Integer deleteByExpiredIsTrue();
}
