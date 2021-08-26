package com.bok.parent.be.exception;

import static com.bok.parent.be.exception.TokenNotFoundException.Codes.TOKEN_NOT_FOUND;

public class TokenNotFoundException extends RuntimeException {
    public TokenNotFoundException() {
        super(String.valueOf(TOKEN_NOT_FOUND));
    }

    public enum Codes {
        TOKEN_NOT_FOUND
    }
}
