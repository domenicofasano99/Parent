package com.bok.parent.exception;

import static com.bok.parent.exception.TokenNotFoundException.Codes.TOKEN_NOT_FOUND;

public class TokenNotFoundException extends RuntimeException {
    public TokenNotFoundException() {
        super(String.valueOf(TOKEN_NOT_FOUND));
    }

    public static enum Codes {
        TOKEN_NOT_FOUND
    }
}
