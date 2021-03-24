package com.bok.parent.exception;

public class UserException extends RuntimeException {
    public UserException(UserExceptionCode userExceptionCode) {
        super(String.valueOf(userExceptionCode));
    }

    public enum UserExceptionCode {
        USERNAME_ALREADY_EXISTS,
        EMAIL_ALREADY_EXISTS,
        USER_NOT_FOUND,
        USER_NOT_ENABLED,
        INVALID_CREDENTIALS
    }
}
