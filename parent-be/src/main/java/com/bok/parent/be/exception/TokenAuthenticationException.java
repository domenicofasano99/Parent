package com.bok.parent.be.exception;

public class TokenAuthenticationException extends RuntimeException {
    public TokenAuthenticationException() {
    }

    public TokenAuthenticationException(String message) {
        super(message);
    }
}
