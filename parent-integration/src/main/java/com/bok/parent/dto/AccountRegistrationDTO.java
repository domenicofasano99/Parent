package com.bok.parent.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

public class AccountRegistrationDTO {
    public String name;
    public String surname;
    public String email;
    public String password;
    public Date birthdate;

    public AccountRegistrationDTO() {
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("surname", surname)
                .append("email", email)
                .append("password", password)
                .append("birthdate", birthdate)
                .toString();
    }

}
