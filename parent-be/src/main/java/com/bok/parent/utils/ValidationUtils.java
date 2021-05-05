package com.bok.parent.utils;

import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ValidationUtils {

    public static void nonNull(Object o) {
        nonNull(o, "Parameter cannot be null");
    }


    public static void nonNull(Object object, String errorMessage) {
        if (Objects.isNull(object)) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
