package com.bok.parent.be.utils;

import com.bok.parent.be.helper.EmailHelper;
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

    final static Pattern namePatter = Pattern.compile("[a-zA-z]+([ '-][a-zA-Z]+)*");
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

    public static void validateName(String name) {
        Matcher nameMatcher = namePatter.matcher(name);
        if (!nameMatcher.matches()) {
            log.info("Name {} not accepted", name);
            throw new IllegalArgumentException("Illegal person Name");
        }
    }

    public static void validateSurname(String surname) {
        Matcher surnameMatcher = namePatter.matcher(surname);
        if (!surnameMatcher.matches()) {
            log.info("Surname {} not accepted", surname);
            throw new IllegalArgumentException("Illegal person Surname");
        }
    }

    public static boolean checkVatNumber(String vatNumeber) {
        final String regex = "(?xi)^(\n"
                + "(AT)?U[0-9]{8} |                              # Austria\n"
                + "(BE)?0[0-9]{9} |                              # Belgium\n"
                + "(BG)?[0-9]{9,10} |                            # Bulgaria\n"
                + "(HR)?[0-9]{11} |                              # Croatia\n"
                + "(CY)?[0-9]{8}L |                              # Cyprus\n"
                + "(CZ)?[0-9]{8,10} |                            # Czech Republic\n"
                + "(DE)?[0-9]{9} |                               # Germany\n"
                + "(DK)?[0-9]{8} |                               # Denmark\n"
                + "(EE)?[0-9]{9} |                               # Estonia\n"
                + "(EL|GR)?[0-9]{9} |                            # Greece\n"
                + "ES[A-Z][0-9]{7}(?:[0-9]|[A-Z]) |              # Spain\n"
                + "(FI)?[0-9]{8} |                               # Finland\n"
                + "(FR)?[0-9A-Z]{2}[0-9]{9} |                    # France\n"
                + "(GB)?([0-9]{9}([0-9]{3})?|[A-Z]{2}[0-9]{3}) | # United Kingdom\n"
                + "(HU)?[0-9]{8} |                               # Hungary\n"
                + "(IE)?[0-9]S[0-9]{5}L |                        # Ireland\n"
                + "(IT)?[0-9]{11} |                              # Italy\n"
                + "(LT)?([0-9]{9}|[0-9]{12}) |                   # Lithuania\n"
                + "(LU)?[0-9]{8} |                               # Luxembourg\n"
                + "(LV)?[0-9]{11} |                              # Latvia\n"
                + "(MT)?[0-9]{8} |                               # Malta\n"
                + "(NL)?[0-9]{9}B[0-9]{2} |                      # Netherlands\n"
                + "(PL)?[0-9]{10} |                              # Poland\n"
                + "(PT)?[0-9]{9} |                               # Portugal\n"
                + "(RO)?[0-9]{2,10} |                            # Romania\n"
                + "(SE)?[0-9]{12} |                              # Sweden\n"
                + "(SI)?[0-9]{8} |                               # Slovenia\n"
                + "(SK)?[0-9]{10}                                # Slovakia\n"
                + ")$";
        final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(vatNumeber);
        return matcher.matches();
    }
    public static boolean checkTaxCode(String taxCode) {
        final String regex = "[a-zA-Z]{6}[0-9]{2}[a-zA-Z][0-9]{2}[a-zA-Z][0-9]{3}[a-zA-Z]";

        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(taxCode);
        return matcher.matches();

    }
}
