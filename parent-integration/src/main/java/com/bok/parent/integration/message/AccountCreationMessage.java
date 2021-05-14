package com.bok.parent.integration.message;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;

public class AccountCreationMessage implements Serializable {
    public String name;
    public String middleName;
    public String surname;
    public String email;
    public Date birthdate;
    public String birthCity;
    public String birthCountry;
    public Boolean business;
    public String fiscalCode;
    public String vatNumber;
    public String icc;
    public String mobile;
    public String houseNumber;
    public String street;
    public String city;
    public String county;
    public String country;
    public String postalCode;
    public Long accountId;
    public String gender;

    public AccountCreationMessage() {
    }

    public AccountCreationMessage(String name, String middleName, String surname, String email, Date birthdate, String birthCity, String birthCountry, Boolean business, String fiscalCode, String vatNumber, String icc, String mobile, String houseNumber, String street, String city, String county, String country, String postalCode, Long accountId, String gender) {
        this.name = name;
        this.middleName = middleName;
        this.surname = surname;
        this.email = email;
        this.birthdate = birthdate;
        this.birthCity = birthCity;
        this.birthCountry = birthCountry;
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
        this.accountId = accountId;
        this.gender = gender;
    }

    public AccountCreationMessage(String name, String middleName, String surname, String email, Date birthdate, Boolean business, String fiscalCode, String vatNumber, String icc, String mobile, String houseNumber, String street, String city, String county, String country, String postalCode, Long accountId, String gender) {
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
        this.accountId = accountId;
        this.gender = gender;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
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
                .append("accountId", accountId)
                .append("gender", gender)
                .toString();
    }
}
