package com.zeto.edf_processor.controller;

import com.zeto.edf_processor.dto.EdfDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
public class EdfControllerIT {

    @Autowired
    EdfController edfController;

    @Test
    void getAllEdfsSortedByRecordingDate_shouldReturnListSortedByRecordingDateDescending() {

        List<EdfDto> edfDtos = edfController.getAllEdfsSortedByRecordingDate();
        var recordingDateList = edfDtos.stream()
                                        .map(EdfDto::getRecordingDate)
                                        .toList();

        boolean isEdfDtoListDescending =
                IntStream.range(0,recordingDateList.size()-1)
                         .anyMatch(i -> recordingDateList.get(i).isAfter(recordingDateList.get(i+1)));

        assertTrue(isEdfDtoListDescending);
    }
}
