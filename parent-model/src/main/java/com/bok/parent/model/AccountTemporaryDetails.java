package com.bok.parent.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.util.Date;


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


    @OneToOne(targetEntity = Account.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "account_id")
    private Account account;

    public AccountTemporaryDetails() {
    }

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public Boolean getBusiness() {
        return business;
    }

    public void setBusiness(Boolean business) {
        this.business = business;
    }

    public String getFiscalCode() {
        return fiscalCode;
    }

    public void setFiscalCode(String fiscalCode) {
        this.fiscalCode = fiscalCode;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public String getIcc() {
        return icc;
    }

    public void setIcc(String icc) {
        this.icc = icc;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .append("middleName", middleName)
                .append("surname", surname)
                .append("email", email)
                .append("birthdate", birthdate)
                .append("business", business)
                .append("fiscalCode", fiscalCode)
                .append("vatNumber", vatNumber)
                .append("icc", icc)
                .append("mobile", mobile)
                .append("houseNumber", houseNumber)
                .append("street", street)
                .append("city", city)
                .append("county", county)
                .append("country", country)
                .append("postalCode", postalCode)
                .append("gender", gender)
                .append("account", account)
                .toString();
    }
}
