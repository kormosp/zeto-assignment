package com.zeto.edf_processor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object (DTO) for EDF (European Data Format) file information.
 *
 * <p>This DTO represents the external API contract for EDF file data, exposing
 * only the information needed by frontend applications. It serves as a boundary
 * between the domain model and external consumers.</p>
 *
 * <p><b>Design principles:</b></p>
 * <ul>
 *   <li>Contains only data needed by API consumers</li>
 *   <li>Nullable fields use wrapper types (Integer, Double) not primitives</li>
 *   <li>Includes both valid and invalid file representations</li>
 * </ul>
 *
 * <p><b>JSON serialization example (valid file):</b></p>
 * <pre>
 * {
 *   "fileName": "patient001.edf",
 *   "validEdf": true,
 *   "errorMessage": null,
 *   "recordingID": "Startdate 03-MAR-2022 ZHI27402 Mrs._John_Doe Zeto_WR-08",
 *   "recordingDate": "2024-01-15T10:30:00",
 *   "patientName": "John Doe",
 *   "numberOfChannels": 19,
 *   "channels": [
 *     {"name": "EEG Fp1", "type": "AgCl"},
 *     {"name": "EEG Fp2", "type": "AgCl"}
 *   ],
 *   "recordingLength": 3600.0,
 *   "numberOfAnnotations": 5
 * }
 * </pre>
 *
 * <p><b>JSON serialization example (invalid file):</b></p>
 * <pre>
 * {
 *   "fileName": "corrupted.edf",
 *   "validEdf": false,
 *   "errorMessage": "Invalid EDF file",
 *   "recordingID": null,
 *   "recordingDate": null,
 *   "patientName": null,
 *   "numberOfChannels": null,
 *   "channels": [],
 *   "recordingLength": null,
 *   "numberOfAnnotations": null
 * }
 * </pre>
 *
 * @author Peter Kormos
 * @version 1.0
 * @see ChannelDto
 * @see EdfMapper
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EdfDto {
    /**
     * Name of the EDF file including extension (e.g., "patient001.edf").
     * <p>This field is always present for both valid and invalid files.</p>
     */
    private String fileName;
    /**
     * Indicates whether the EDF file was successfully parsed.
     * <p>{@code true} if file is valid and parsed successfully, {@code false} otherwise.</p>
     */
    private Boolean validEdf;
    /**
     * Error message explaining why the file is invalid.
     * <p>Only populated when {@link #validEdf} is {@code false}.
     * {@code null} for valid files.</p>
     */
    private String errorMessage;

    /**
     * Unique identifier for the recording from the EDF header.
     * <p>May be {@code null} if not present in the file or if file is invalid.</p>
     */
    private String recordingID;
    /**
     * Date and time when the recording was made.
     * <p>Combines the startDate and startTime fields from the EDF header.
     * {@code null} for invalid files or if date parsing fails.</p>
     */
    private LocalDateTime recordingDate;
    /**
     * Name of the patient extracted from the  EDF header:SubjectID field.
     * <p>The EDF standard format for SubjectID is: "Code Sex Birthdate Name".
     * This field contains the extracted name (4th token), with underscores
     * replaced by spaces.</p>
     *
     * <p><b>Example values:</b></p>
     * <ul>
     *   <li>"John Doe" (extracted from "P001 M 01-JAN-1980 John_Doe")</li>
     *   <li>"Not Available" (if name cannot be extracted)</li>
     *   <li>{@code null} (for invalid files)</li>
     * </ul>
     */
    private String patientName;
    /**
     * List of all channels (signals) with their names and types.
     * <p>Empty list for invalid files. Each channel contains:
     * <ul>
     *   <li>name: Label of the channel (e.g., "EEG Fp1", "ECG")</li>
     *   <li>type: Transducer type (e.g., "Active electrode")</li>
     * </ul>
     * </p>
     *
     * @see ChannelDto
     */
    private List<ChannelDto> channels;
    /**
     * Total number of channels (signals) in the EDF recording.
     * <p>{@code null} for invalid files.</p>
     */
    private Integer numberOfChannels;

    /**
     * Total duration of the recording in seconds.
     * <p>Calculated as: numberOfRecords × durationOfRecords.
     * {@code null} for invalid files.</p>
     *
     * <p><b>Example values:</b></p>
     * <ul>
     *   <li>3600.0 (1 hour recording)</li>
     *   <li>1800.0 (30 minutes)</li>
     *   <li>86400.0 (24 hours)</li>
     * </ul>
     */
    private Double recordingLength;
    /**
     * Number of annotations (events) in the EDF file.
     * <p>Annotations mark significant events during the recording,
     * 0 for invalid files or if no annotations are present.</p>
     */
    private Integer numberOfAnnotations;

}
