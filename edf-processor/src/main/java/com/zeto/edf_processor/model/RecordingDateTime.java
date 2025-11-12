package com.zeto.edf_processor.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

/**
 *  Represents the recording's start date and time from EDF header
 */
@Getter
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class RecordingDateTime {

    /** Recording start date */
    private final String startDate;

    /** Recording start time */
    private final String startTime;

    /** Combined and parsed LocalDateTime */
    private final LocalDateTime recordingDateTime;

    /** DateTimeFormatter to convert recordingDateTime field */
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yy HH.mm.ss");

    /**
     * Factory method to create a RecordingDateTime from strings.
     *
     * @param startDate start date string
     * @param startTime start time string
     * @return RecordingDateTime object or null if parsing fails
     */

    public static RecordingDateTime from(String startDate, String startTime) {
        if (startDate == null || startTime == null) return null;

        try {
            String combinedDateTime = startDate.trim() + " " + startTime.trim();
            LocalDateTime parsedRecordingDate  = LocalDateTime.parse(combinedDateTime, DATE_TIME_FORMATTER);
            return new RecordingDateTime(startDate, startTime, parsedRecordingDate);
        } catch (DateTimeParseException dtpe) {
            log.warn("Failed to parse recording  date: {} {}", startDate, startTime);
            return null;
        }
    }

}
