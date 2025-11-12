package com.zeto.edf_processor.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Optional;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class EdfData {
    private final EdfFileProperties edfFileProperties;

    private final String recordingID;
    private final RecordingDateTime recordingDateTime;
    private final PatientInfo patientInfo;
    private final Channels channels;

    //Data record Length in sec
    private final RecordingMetrics metrics;

    private final int numberOfAnnotations;

    /**
     * Factory method for creating an invalid EDF representation
     */
    public static EdfData createInvalidEdfData(String fileName, String errorMessage) {
        log.warn("Creating invalid EDF data for file: {}", fileName);
        return new EdfData(EdfFileProperties.invalidEdf(fileName,errorMessage),
                                 null,
                           null,
                                 null,
                                    Channels.empty(),
                                     RecordingMetrics.empty(),
                         0
        );
    }

    /**
     * Factory method for creating valid EDF representation
     */
    public static EdfData createValidEdfData(String fileName,
                                             String recordingId,
                                             String startDate,
                                             String startTime,
                                             String subjectId,
                                             String[] channelLabels,
                                             String[] transducerType,
                                             int numberOfRecords,
                                             double durationOfRecords,
                                             int numberOfAnnotations
                                             ) {
            log.debug("Creating valid EDF data for file: {}", fileName);
            return new EdfData(EdfFileProperties.validEdf(fileName),
                                     recordingId.trim(),
                               RecordingDateTime.from(startDate, startTime),
                                     PatientInfo.from(subjectId),
                                        Channels.from(channelLabels, transducerType),
                                         RecordingMetrics.from(numberOfRecords, durationOfRecords),
                                                   numberOfAnnotations);
    }


    public double getRecordingLengthInSeconds() {
        return metrics.recordingLength();
    }

    public LocalDateTime getRecordingDateTime() {
        return recordingDateTime != null ? recordingDateTime.getRecordingDateTime() : null;
            }

    public String getPatientName() {
        return patientInfo != null ? patientInfo.getPatientName() : "Not Available";
    }

    public int getNumberOfChannels() {
        return channels.count();
    }

}
