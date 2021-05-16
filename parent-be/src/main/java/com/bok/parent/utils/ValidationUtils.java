package com.bok.parent.utils;

import com.bok.parent.helper.EmailHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class ValidationUtils {

    @Autowired
    EmailHelper emailHelper;

    final static Pattern namePatter = Pattern.compile("^[a-zA-Z\\s]+");
    final static Pattern surnamePatter = Pattern.compile("[a-zA-z]+([ '-][a-zA-Z]+)*");


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

    public static void validateName(String name) {
        Matcher nameMatcher = namePatter.matcher(name);
        if (!nameMatcher.matches()) {
            log.info("Name {} not accepted", name);
            throw new IllegalArgumentException("Illegal person Name");
        }
    }

    public static void validateSurname(String surname) {
        Matcher surnameMatcher = surnamePatter.matcher(surname);
        if (!surnameMatcher.matches()) {
            log.info("Surname {} not accepted", surname);
            throw new IllegalArgumentException("Illegal person Surname");
        }
    }
}
