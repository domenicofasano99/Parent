package com.bok.parent.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Token {
    @Id
    @GeneratedValue
    private Long id;

    @NaturalId
    @Column(unique = true, length = 400)
    public String tokenString;

    @Column
    public Instant issuedAt;

    @Column
    public Instant expiresAt;

    @Column
    public String issuer;

    @Column
    public Long accountId;

    @Column
    public String email;

    @Column
    public Boolean expired;

    public Token(String tokenString, Instant issuedAt, Instant expiresAt, String issuer, Long accountId, String email, Boolean expired) {
        this.tokenString = tokenString;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
        this.issuer = issuer;
        this.accountId = accountId;
        this.email = email;
        this.expired = expired;
    }
}
