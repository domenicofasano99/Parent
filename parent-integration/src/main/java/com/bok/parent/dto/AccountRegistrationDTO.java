package com.bok.parent.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

public class AccountRegistrationDTO {
    public String name;
    public String middleName;
    public String surname;
    public Date birthdate;
    public Boolean business;
    public String fiscalCode;
    public String vatNumber;

    public CredentialsDTO credentials;
    public MobileDTO mobile;
    public AddressDTO address;


    public AccountRegistrationDTO() {
    }

    public static class MobileDTO {
        public String icc;
        public String mobile;

        public MobileDTO(String icc, String mobile) {
            this.icc = icc;
            this.mobile = mobile;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .append("icc", icc)
                    .append("mobile", mobile)
                    .toString();
        }
    }

    public static class AddressDTO {
        public String houseNumber;
        public String street;
        public String city;
        public String county;
        public String country;
        public String postalCode;

        public AddressDTO(String houseNumber, String street, String city, String county, String country, String postalCode) {
            this.houseNumber = houseNumber;
            this.street = street;
            this.city = city;
            this.county = county;
            this.country = country;
            this.postalCode = postalCode;
        }
    }

    public static class CredentialsDTO {
        public String email;
        public String password;

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .append("email", email)
                    .append("password", password)
                    .toString();
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("middleName", middleName)
                .append("surname", surname)
                .append("birthdate", birthdate)
                .append("business", business)
                .append("fiscalCode", fiscalCode)
                .append("vatNumber", vatNumber)
                .append("credentials", credentials)
                .append("mobile", mobile)
                .append("address", address)
                .toString();
    }
}
