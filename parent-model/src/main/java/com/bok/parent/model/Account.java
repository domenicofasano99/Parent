package com.bok.parent.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Credentials credentials;

    @CreationTimestamp
    private Instant creationTimestamp;

    @UpdateTimestamp
    private Instant updateTimestamp;

    @Column
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(orphanRemoval = true, mappedBy = "id")
    private List<AccessInfo> accessInfo;

    @OneToOne(orphanRemoval = true)
    private TemporaryAccount temporaryAccount;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "id")
    private List<Token> tokens;

    @Column(columnDefinition = "boolean default true")
    private Boolean passwordResetNeeded;

    public Account(String email, String password) {
        this.credentials = new Credentials(email, password);
    }

    public void addToken(Token token) {
        this.tokens.add(token);
    }

    public enum Role {
        USER
    }

    public void clearTokens() {
        this.tokens.clear();
    }
}