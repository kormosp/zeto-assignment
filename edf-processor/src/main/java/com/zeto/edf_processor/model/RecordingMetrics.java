package com.zeto.edf_processor.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class RecordingMetrics {

    private final int numberOfRecords;
    private final double durationOfRecords;

    public static RecordingMetrics from(int numberOfRecords, double durationOfRecords) {
            return new RecordingMetrics(numberOfRecords, durationOfRecords);
    }

    public static RecordingMetrics empty() {
        return new RecordingMetrics(0, 0.0);
    }

    public double recordingLength() {
        return numberOfRecords * durationOfRecords;
    }


}
