package com.zeto.edf_processor.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Represents recording metrics such as number of records and duration per record.
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class RecordingMetrics {

    /** Number of records in the recording */
    private final int numberOfRecords;

    /** Duration of each record in seconds */
    private final double durationOfRecords;

    /**
     * Factory method to create RecordingMetrics.
     *
     * @param numberOfRecords number of records
     * @param durationOfRecords duration per record in seconds
     * @return RecordingMetrics object
     */
    public static RecordingMetrics from(int numberOfRecords, double durationOfRecords) {
            return new RecordingMetrics(numberOfRecords, durationOfRecords);
    }

    /**
     * Returns an empty RecordingMetrics object.
     *
     * @return RecordingMetrics with zero values
     */
    public static RecordingMetrics empty() {
        return new RecordingMetrics(0, 0.0);
    }

    /**
     * Computes the total recording length in seconds.
     *
     * @return total recording length
     */
    public double recordingLength() {
        return numberOfRecords * durationOfRecords;
    }


}
