package com.bok.parent.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AccountTemporaryDetails {

    @Id
    @GeneratedValue
    private Long id;
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

    @OneToOne(fetch = FetchType.EAGER)
    private Account account;

    public AccountTemporaryDetails(String name, String middleName, String surname, String email, Date birthdate, Boolean business, String fiscalCode, String vatNumber, String icc, String mobile, String houseNumber, String street, String city, String county, String country, String postalCode, String gender, Account account) {
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
        this.account = account;
    }
}
