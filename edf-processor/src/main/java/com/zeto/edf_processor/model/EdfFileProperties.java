package com.zeto.edf_processor.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Represents properties of an EDF file including its name and validation status.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class EdfFileProperties {
    /** Name of the EDF file */
    private final String fileName;
    /** Indicates if the EDF file is valid */
    private final boolean validEdf;
    /** Error message if the EDF file is invalid */
    private final String errorMessage;

    /**
     * Creates a valid EDF file properties object.
     *
     * @param fileName file name
     * @return EdfFileProperties for a valid EDF
     */
    public static EdfFileProperties validEdf(String fileName) {
        return new EdfFileProperties(fileName, true, null );
    }

    /**
     * Creates an invalid EDF file properties object with error message
     *
     * @param fileName file name
     * @param errorMessage reason for invalidity
     * @return EdfFileProperties for an invalid EDF
     */
    public static EdfFileProperties invalidEdf(String fileName, String errorMessage) {
        return new EdfFileProperties(fileName, false, errorMessage);
    }

}
