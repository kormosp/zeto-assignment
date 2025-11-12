package com.zeto.edf_processor.exceptions;

public class EdfSourceNotFoundException extends RuntimeException {
    public EdfSourceNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
