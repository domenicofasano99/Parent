package com.bok.parent.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AccessInfo {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Account account;

    @Column
    private String ipAddress;

    @Column
    @CreationTimestamp
    private Instant timestamp;

}
