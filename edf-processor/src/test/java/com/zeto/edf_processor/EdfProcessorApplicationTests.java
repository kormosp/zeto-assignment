package com.zeto.edf_processor;

import com.zeto.edf_processor.controller.EdfController;
import com.zeto.edf_processor.dto.EdfDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
class EdfProcessorApplicationTests {
    @Autowired
    private EdfController edfController;

	@Test
	void getAllEdfsSortedByRecordingDate_shouldReturnListSortedDescending() {

        List<EdfDto> edfDtos = edfController.getAllEdfsSortedByRecordingDate();
        var recordingDateList = edfDtos.stream().map(EdfDto::getRecordingDate).toList();

    }

}
