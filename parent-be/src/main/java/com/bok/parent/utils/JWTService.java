package com.bok.parent.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bok.parent.exception.TokenAuthenticationException;
import com.bok.parent.helper.AccountHelper;
import com.bok.parent.helper.TokenHelper;
import com.bok.parent.model.Account;
import com.bok.parent.model.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Random;

@Slf4j
@Service
public class JWTService {

    private final Algorithm algorithm;
    private final int defaultExpiration;

    @Autowired
    CryptoUtils cryptoUtils;

    @Autowired
    TokenHelper tokenHelper;

    @Autowired
    AccountHelper accountHelper;

    private final Random random = new Random();


    public JWTService(
            @Value("${jwt.security.secret}") String secret,
            @Value("${jwt.security.expiration}") int defaultExpirationSeconds) {
        this.algorithm = Algorithm.HMAC256(secret);
        this.defaultExpiration = defaultExpirationSeconds;
    }

    public Token create(Account account) {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plusSeconds(defaultExpiration);

        String tokenString;
        tokenString = cryptoUtils.encryptToken(JWT.create()
                .withIssuedAt(Date.from(issuedAt))
                .withExpiresAt(Date.from(expiresAt))
                .withSubject(account.getId().toString())
                .withClaim("randInt", random.nextInt())
                .sign(algorithm));
        return new Token(tokenString, issuedAt, expiresAt, account, false);
    }

    public Token verify(String tokenString) {
        JWTVerifier verifier = JWT.require(algorithm).build();
        try {
            tokenString = cryptoUtils.decryptToken(tokenString);
            DecodedJWT jwt = verifier.verify(tokenString);

            Token token = new Token();
            token.issuedAt = jwt.getIssuedAt().toInstant();
            token.expiresAt = jwt.getExpiresAt().toInstant();
            token.expired = jwt.getExpiresAt().toInstant().isBefore(Instant.now());
            token.account = accountHelper.findById(Long.valueOf(jwt.getSubject()));

            return token;
        } catch (JWTVerificationException e) {
            throw new TokenAuthenticationException("TOKEN_VERIFICATION_EXCEPTION");
        } catch (Exception e) {
            throw new RuntimeException("An exception occurred while verifying token");
        }
    }
}
