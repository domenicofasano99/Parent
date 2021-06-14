package com.bok.parent.repository;

import com.bok.parent.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    @Transactional
    Integer deleteByExpirationBefore(Instant expiration);

    @Transactional
    Integer deleteByTokenString(String tokenString);

    Optional<Token> findByTokenString(@Param("ts") String token);
}
