package com.bok.parent.exception;

public class TokenAuthenticationException extends RuntimeException {
    public TokenAuthenticationException() {
    }

    public TokenAuthenticationException(String message) {
        super(message);
    }
}
