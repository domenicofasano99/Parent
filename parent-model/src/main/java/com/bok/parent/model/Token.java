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
    @Column
    public Boolean expired;
    @NaturalId
    @Column
    private String tokenString;
    @Column
    private Instant issuedAt;
    @Column
    private Instant expiration;
    @ManyToOne
    private Account account;
    @Id
    @GeneratedValue
    private Long id;

    public Token(String tokenString, Instant issuedAt, Instant expiration, Account account, Boolean expired) {
        this.tokenString = tokenString;
        this.issuedAt = issuedAt;
        this.expiration = expiration;
        this.expired = expired;
        this.account = account;
    }
}
