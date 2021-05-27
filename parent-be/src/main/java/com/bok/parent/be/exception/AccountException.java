package com.bok.parent.be.exception;

public class AccountException extends RuntimeException {
    public AccountException(AccountExceptionCode accountExceptionCode) {
        super(String.valueOf(accountExceptionCode));
    }

    public AccountException(String message) {
        super(message);
    }

    public enum AccountExceptionCode {
        USERNAME_ALREADY_EXISTS,
        EMAIL_ALREADY_EXISTS,
        USER_NOT_FOUND,
        USER_NOT_ENABLED,
        INVALID_CREDENTIALS
    }
}
