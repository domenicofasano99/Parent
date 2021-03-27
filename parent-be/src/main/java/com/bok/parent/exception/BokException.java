package com.bok.parent.exception;

public class BokException extends RuntimeException {
    public BokException() {
    }

    public BokException(String message) {
        super(message);
    }
}
