package com.bok.parent.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class ServiceNotAvailableException extends RuntimeException {

    public ServiceNotAvailableException(String resource) {
        super(String.format("Resource %s not found", resource));
    }

}
