package com.bok.parent.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
    @Column
    public String tokenString;

    @Column
    public Instant issuedAt;

    @Column
    public Instant expiresAt;

    @ManyToOne
    public Account account;

    @Column
    public Boolean expired;

    public Token(String tokenString, Instant issuedAt, Instant expiresAt, Account account, Boolean expired) {
        this.tokenString = tokenString;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
        this.expired = expired;
        this.account = account;
    }
}
