package com.bok.parent.integration.dto;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
public class AccountRegistrationDTO {
    public String name;
    public String middleName;
    public String surname;
    public Date birthdate;
    public Boolean business;
    public String fiscalCode;
    public String vatNumber;
    public String gender;

    public CredentialsDTO credentials;
    public MobileDTO mobile;
    public AddressDTO address;

    public static Gender genderOf(String gender) {
        if (gender.equalsIgnoreCase("m")) {
            return Gender.M;
        }
        if (gender.equalsIgnoreCase("f")) {
            return Gender.F;
        }
        return null;
    }

    public enum Gender {
        M,
        F
    }

    @Data
    public static class MobileDTO {
        public String icc;
        public String number;

    }

    @Data
    public static class AddressDTO {
        public String houseNumber;
        public String street;
        public String city;
        public String county;
        public String country;
        public String postalCode;
    }

    @Data
    public static class CredentialsDTO {
        public String email;
        @ToString.Exclude
        public String password;
    }

}
