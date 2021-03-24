package com.bok.parent.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bok.parent.exception.BokException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JWTService {

    @Autowired
    CryptoUtils cryptoUtils;

    private final Algorithm algorithm;
    private final int defaultExpiration;

    public JWTService(
            @Value("${security.jwt.secret}") String secret,
            @Value("${security.jwt.expiration}") int defaultExpirationSeconds) {
        this.algorithm = Algorithm.HMAC256(secret);
        this.defaultExpiration = defaultExpirationSeconds;
    }

    public String create(String username) {
        Instant issuedAt = Instant.now();
        return cryptoUtils.encryptToken(JWT.create()
                .withIssuedAt(Date.from(issuedAt))
                .withExpiresAt(Date.from(issuedAt.plusSeconds(defaultExpiration)))
                .withClaim("username", username)
                .sign(algorithm));
    }

    public Map<String, Object> verify(String token) throws BokException {
        JWTVerifier verifier = JWT.require(algorithm)
                .build();
        try {
            token = cryptoUtils.decryptToken(token);
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaims()
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().as(Object.class)));
        } catch (Exception e) {
            throw new BokException("TOKEN_VERIFICATION_EXCEPTION");
        }
    }
}
