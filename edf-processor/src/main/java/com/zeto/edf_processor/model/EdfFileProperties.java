package com.zeto.edf_processor.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Value object for source file properties and file validation status
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class EdfFileProperties {
    private final String fileName;
    private final boolean validEdf;
    private final String errorMessage;

    public static EdfFileProperties validEdf(String fileName) {
        return new EdfFileProperties(fileName, true, null );
    }

    public static EdfFileProperties invalidEdf(String fileName, String errorMessage) {
        return new EdfFileProperties(fileName, false, errorMessage);
    }

}
