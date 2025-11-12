package com.zeto.edf_processor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing a single channel (signal) in an EDF recording.
 *
 * <p>In EDF files, a channel represents a single signal being recorded, such as
 * an EEG electrode, ECG lead, or other physiological measurement. Each channel
 * has a label (name) and optionally a transducer type describing the sensor used.</p>
 *
 * <p><b>JSON serialization example:</b></p>
 * <pre>
 * {
 *   "name": "EEG Fp1",
 *   "type": "Active electrode"
 * }
 * </pre>
 *
 * @author Peter Kormos
 * @version 1.0
 * @see EdfDto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChannelDto {
    /**
     * Label or name of the channel.
     * <p>The name is trimmed of leading/trailing whitespace during processing.</p>
     */
    private String name;
    /**
     * Type or description of the transducer (sensor) used for this channel.
     *
     * <p>This field describes the physical sensor or measurement method.</p>
     *
     * <p>This field is trimmed of leading/trailing whitespace and defaults to
     * an empty string if not present in the EDF file.</p>
     */
    private String type;
}
