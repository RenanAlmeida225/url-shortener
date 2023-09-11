package com.urlshortener.exceptions;

public class FailedSendMailException extends RuntimeException {
    public FailedSendMailException(String message) {
        super(message);
    }
}
