package com.bok.parent.utils;

import com.bok.parent.helper.EmailHelper;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ValidationUtils {

    @Autowired
    EmailHelper emailHelper;

    public static void nonNull(Object o) {
        nonNull(o, "Parameter cannot be null");
    }


    public static void nonNull(Object object, String errorMessage) {
        if (Objects.isNull(object)) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public static void checkEmail(String email, String errorMessage) {
        if (!validateEmail(email)) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public static Boolean validateEmail(String email) {
        return EmailValidator.getInstance().isValid(email);
    }

    public static void check(Boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void check(Boolean expression) {
        check(expression, "Invalid argument.");
    }
}
