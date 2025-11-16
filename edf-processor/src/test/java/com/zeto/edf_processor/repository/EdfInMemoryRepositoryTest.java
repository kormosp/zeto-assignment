package com.zeto.edf_processor.repository;

import com.zeto.edf_processor.config.EdfProcessorProperties;
import com.zeto.edf_processor.exceptions.EdfSourceNotFoundException;
import com.zeto.edf_processor.model.EdfData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class EdfInMemoryRepositoryTest {

    private EdfInMemoryRepository edfInMemoryRepository;
    private EdfProcessorProperties properties;

    @TempDir
    File tempDir;

    @BeforeEach
    void setUp() {
        properties = new EdfProcessorProperties();
    }

    @Test
    void loadEdfs_whenSourceDirectoryNotExist_thenThrowException() {
        properties.setEdfAppDir("/tmp");
        properties.setEdfSource("non_existent_path/edf");
        edfInMemoryRepository = new EdfInMemoryRepository(properties);

        // EdfInMemoryRepository::loadEdfs is PostConstruct, call it explicitly
        assertThrows(EdfSourceNotFoundException.class, edfInMemoryRepository::loadEdfs);
    }


    @Test
    void loadEdfs_whenSourceDirectoryEmpty_thenEdfListIsEmptyNoError() {
        properties.setEdfAppDir("");
        properties.setEdfSource(tempDir.toString());
        edfInMemoryRepository = new EdfInMemoryRepository(properties);

        edfInMemoryRepository.loadEdfs();

        List<EdfData> edfs = edfInMemoryRepository.listEdfs();
        assertThat(edfs.size(), equalTo(0));
    }




}
