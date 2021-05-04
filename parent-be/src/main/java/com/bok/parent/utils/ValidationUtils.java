package com.bok.parent.utils;

import org.springframework.stereotype.Component;

@Component
public class ValidationUtils {

    public Boolean validateEmail(String email) {
        return true;
        //return EmailValidator.getInstance().isValid(email);
    }

    //fixme fix this
    public Boolean validateName(String name) {
        return name.matches("^[a-zA-Z\\\\s]+");
    }

    //fixme fix this
    public Boolean validateSurname(String surname) {
        return surname.matches("^[a-zA-Z\\\\s]+");
    }

}
