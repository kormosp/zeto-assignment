package com.zeto.edf_processor.repository;

import com.zeto.edf_processor.model.EdfData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
public class EdfDataRepositoryIT {

    @Autowired
    private EdfDataRepository edfDataRepository;

    @Test
    void loadAllEdfs_thenCheckEdfCount(){
        edfDataRepository.loadEdfs();
        List<EdfData> edfDataList = edfDataRepository.listEdfs();
        assertThat(edfDataList.size(), equalTo(4));
    }


}
