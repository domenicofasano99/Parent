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
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TemporaryAccount {

    @Id
    @GeneratedValue
    private Long id;
    @Column
    private UUID confirmationToken;
    @Column
    private String name;
    @Column
    private String middleName;
    @Column
    private String surname;
    @Column
    private String email;
    @Column
    private Date birthdate;
    @Column
    private Boolean business;
    @Column
    private String fiscalCode;
    @Column
    private String vatNumber;
    @Column
    private String icc;
    @Column
    private String mobile;
    @Column
    private String houseNumber;
    @Column
    private String street;
    @Column
    private String city;
    @Column
    private String county;
    @Column
    private String country;
    @Column
    private String postalCode;
    @Column
    private String gender;
    @CreationTimestamp
    private Instant creationTimestamp;

    public TemporaryAccount(String name, String middleName, String surname, String email, Date birthdate, Boolean business, String fiscalCode, String vatNumber, String icc, String mobile, String houseNumber, String street, String city, String county, String country, String postalCode, String gender) {
        this.name = name;
        this.middleName = middleName;
        this.surname = surname;
        this.email = email;
        this.birthdate = birthdate;
        this.business = business;
        this.fiscalCode = fiscalCode;
        this.vatNumber = vatNumber;
        this.icc = icc;
        this.mobile = mobile;
        this.houseNumber = houseNumber;
        this.street = street;
        this.city = city;
        this.county = county;
        this.country = country;
        this.postalCode = postalCode;
        this.gender = gender;
        this.confirmationToken = UUID.randomUUID();
    }
}
