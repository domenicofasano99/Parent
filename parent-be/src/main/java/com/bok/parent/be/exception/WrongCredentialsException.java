package com.bok.parent.be.exception;

public class WrongCredentialsException extends RuntimeException {
    public WrongCredentialsException() {
    }

    public WrongCredentialsException(String message) {
        super(message);
    }
}
