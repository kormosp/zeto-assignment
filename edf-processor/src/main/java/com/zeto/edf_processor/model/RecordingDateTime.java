package com.zeto.edf_processor.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

/*
* Value object contains the Recording Date and Time from EDF Header
 */
@Getter
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class RecordingDateTime {
    private final String startDate;
    private final String startTime;
    private final LocalDateTime recordingDateTime;

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yy HH.mm.ss");

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
