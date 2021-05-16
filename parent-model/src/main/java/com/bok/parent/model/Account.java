package com.bok.parent.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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

    @Column
    private Boolean enabled;

    @Embedded
    private Credentials credentials;

    @CreationTimestamp
    private Instant creationTimestamp;

    @UpdateTimestamp
    private Instant updateTimestamp;

    @Column
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(orphanRemoval = true)
    private List<AccessInfo> accessInfo;

    @OneToOne(orphanRemoval = true)
    private AccountTemporaryDetails accountTemporaryDetails;

    @OneToMany
    private List<Token> token;

    public Account(String email, String password) {
        this.credentials = new Credentials(email, password);
    }

    public enum Role {
        USER
    }
}