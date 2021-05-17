package com.bok.parent.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bok.parent.exception.TokenAuthenticationException;
import com.bok.parent.helper.AccountHelper;
import com.bok.parent.helper.TokenHelper;
import com.bok.parent.model.Account;
import com.bok.parent.model.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

import static com.bok.parent.utils.Constants.ACCOUNT_ID;
import static com.bok.parent.utils.Constants.EMAIL;
import static com.bok.parent.utils.Constants.EXPIRATION_INSTANT;

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
                .withClaim(EMAIL, account.getCredentials().getEmail())
                .withClaim(EXPIRATION_INSTANT, issuedAt.toString())
                .withClaim(ACCOUNT_ID, account.getId())
                .withIssuer("BOK")
                .sign(algorithm));
        return new Token(tokenString, issuedAt, expiresAt, "BOK", account, false);
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
            token.issuer = jwt.getIssuer();
            token.account = accountHelper.findById(jwt.getClaim(ACCOUNT_ID).asLong());

            return token;
        } catch (Exception e) {
            throw new TokenAuthenticationException("TOKEN_VERIFICATION_EXCEPTION");
        }
    }
}
