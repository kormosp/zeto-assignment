package com.zeto.edf_processor.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Domain entity representing the parsed contents of an EDF (European Data Format) file.
 * <p>
 * This class acts as the main aggregate root in the EDF processing domain.
 * It contains key aspects of an EDF recording, such as file metadata,
 * patient information, recording details, channels, and annotations.
 * </p>
 *
 * <p>
 * The class is designed to be immutable and is instantiated through static
 * factory methods to ensure controlled creation for both valid and invalid EDF files.
 * </p>
 *
 * <h2>Usage</h2>
 * <ul>
 *   <li>Use {@link #createValidEdfData(String, String, String, String, String, String[], String[], int, double, int)}
 *       to build a valid EDF representation from parsed file data.</li>
 *   <li>Use {@link #createInvalidEdfData(String, String)} to represent a corrupted or unreadable EDF file.</li>
 * </ul>
 *
 * <p><strong>DDD Role:</strong> Aggregate Root of the EDF domain.</p>
 *
 * @author Peter Kormos
 * @version 1.0
 */
@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class EdfData {
    /**
     * File-level properties such as file name, validity, and potential error message.
     */
    private final EdfFileProperties edfFileProperties;

    /**
     * Identifier string of the EDF recording.
     */
    private final String recordingID;

    /**
     * Value object representing the recording date and time.
     */
    private final RecordingDateTime recordingDateTime;

    /**
     * Value object representing patient-related details extracted from the EDF header.
     */
    private final PatientInfo patientInfo;

    /**
     * Collection of signal channels contained within the EDF recording.
     */
    private final Channels channels;

    /**
     * Recording metrics, including record counts and duration (used to calculate total recording length).
     */
    private final RecordingMetrics metrics;

    /**
     * Number of annotations stored within the EDF file.
     */
    private final int numberOfAnnotations;

    // --------------------------------------------------------------------------------------------
    // Static Factory Methods
    // --------------------------------------------------------------------------------------------

    /**
     * Factory method for creating an EDF representation marked as invalid.
     * <p>
     * Used when an EDF file cannot be parsed, opened, or fails validation.
     * The resulting instance will contain an invalid {@link EdfFileProperties} object
     * and empty placeholders or null for other components.
     * </p>
     *
     * @param fileName     name of the EDF file
     * @param errorMessage reason or description of the validation failure
     * @return a new {@code EdfData} instance representing an invalid file
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
     * Factory method for creating a valid EDF representation.
     * <p>
     * Constructs a fully populated {@link EdfData} object using values extracted
     * from the EDF header and metadata.
     * </p>
     *
     * @param fileName            name of the EDF file
     * @param recordingId         unique recording identifier
     * @param startDate           start date from the EDF header
     * @param startTime           start time from the EDF header
     * @param subjectId           subject identifier string containing patient details
     * @param channelLabels       array of channel labels
     * @param transducerType      array of transducer (channel type) descriptions
     * @param numberOfRecords     total number of data records
     * @param durationOfRecords   duration of each record (seconds)
     * @param numberOfAnnotations number of annotations in the EDF
     * @return a fully initialized, valid {@code EdfData} instance
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

    // --------------------------------------------------------------------------------------------
    // Derived Getters and Convenience Methods
    // --------------------------------------------------------------------------------------------

    /**
     * Computes the total recording length in seconds based on
     * {@link RecordingMetrics#getNumberOfRecords()} and
     * {@link RecordingMetrics#getDurationOfRecords()}.
     *
     * @return total recording duration in seconds
     */
    public double getRecordingLengthInSeconds() {
        return metrics.recordingLength();
    }

    /**
     * Retrieves the parsed recording start {@link LocalDateTime}, if available.
     *
     * @return recording date-time or {@code null} if unavailable
     */
    public LocalDateTime getRecordingDateTime() {
        return recordingDateTime != null ? recordingDateTime.getRecordingDateTime() : null;
            }

    /**
     * Returns the patient's name extracted from the EDF subject ID, if available.
     *
     * @return patient name or "Not Available" if missing or invalid
     */
    public String getPatientName() {
        return patientInfo != null ? patientInfo.getPatientName() : "Not Available";
    }

    /**
     * Returns the number of recorded signal channels.
     *
     * @return number of channels in the EDF recording
     */
    public int getNumberOfChannels() {
        return channels.count();
    }

    /**
     * Checks if this EDF data is a valid file.
     * @return true if file is valid
     */
    public boolean isValidEdf() {
        return edfFileProperties != null && edfFileProperties.isValidEdf();
    }

}
