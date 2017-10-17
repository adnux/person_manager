package com.andre.example.exception;

/**
 * For HTTP 400 errros
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }

}
