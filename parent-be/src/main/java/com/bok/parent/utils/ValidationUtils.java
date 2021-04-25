package com.bok.parent.utils;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;

@Component
public class ValidationUtils {

    public Boolean validateEmail(String email){
        return EmailValidator.getInstance().isValid(email);
    }

    public Boolean validateName(String name){
        return name.matches( "[A-Z][a-z]*" );
    }

    public Boolean validateSurname(String surname){
        return surname.matches( "[A-Z]+([ '-][a-zA-Z]+)*" );
    }

}
