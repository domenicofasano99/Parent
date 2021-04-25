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
        return true;
        //return name.matches( "[A-Z][a-z]*" );
    }

    //fixme fix this
    public Boolean validateSurname(String surname) {
        return true;
        //return surname.matches( "[A-Z]+([ '-][a-zA-Z]+)*" );
    }

}
