package com.zeto.edf_processor.exceptions;

public class EdfDataNotFoundException extends RuntimeException {
    public EdfDataNotFoundException(String message) {
        super(message);
    }
}
