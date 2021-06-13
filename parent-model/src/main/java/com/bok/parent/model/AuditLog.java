package com.bok.parent.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String ipAddress;

    @Column
    private Long accountId;

    @Column
    private String method;

    @Column
    private String path;

    @Column
    private String email;

    @Column
    @Type(type = "text")
    private String payload;

    @Column
    @Type(type = "text")
    private String parameters;

    @CreationTimestamp
    private Instant timestamp;

}
