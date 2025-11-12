package com.zeto.edf_processor.repository;


import com.zeto.edf_processor.config.EdfProcessorProperties;
import com.zeto.edf_processor.exceptions.EdfSourceNotFoundException;
import com.zeto.edf_processor.model.EdfData;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.mipt.edf.EDFHeader;
import ru.mipt.edf.EDFParserResult;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Repository for managing EDF (European Data Format) file data from the file system.
 *
 * <p>This repository acts as the data access layer, responsible for:</p>
 * <ul>
 *   <li>Scanning the configured directory for .edf files</li>
 *   <li>Parsing each file using the EDFParser library</li>
 *   <li>Converting parsed data into domain entities</li>
 *   <li>Maintaining an in-memory cache of loaded files</li>
 *   <li>Handling both valid and invalid EDF files gracefully</li>
 * </ul>
 *
 * <p>The repository automatically loads all EDF files on application startup
 * via the {@link #loadEdfs()} method annotated with {@link PostConstruct}.</p>
 *
 * @author Peter Kormos
 * @version 1.0
 * @see EdfData
 * @see EdfReader
 */
@Repository
@Slf4j
@RequiredArgsConstructor
public class EdfDataRepository {
    /**
     * Static instance of EDF file reader for parsing operations.
     */
    private static final EdfReader EDF_READER = new EdfReader();

    /**
     * Configuration properties containing the EDF directory path.
     */
    private final EdfProcessorProperties properties;

    /**
     * In-memory cache of loaded EDF file data.
     * <p>This list contains both valid and invalid EDF files.</p>
     */
    private final List<EdfData> edfs = new LinkedList<>();

    /**
     * Loads all EDF files from the configured directory on application startup.
     *
     * <p>This method is automatically invoked thanks to the {@link PostConstruct} annotation.
     * It performs the following steps:</p>
     *
     * <ol>
     *   <li>Clears any previously cached file data</li>
     *   <li>Validates the configured directory exists and is accessible</li>
     *   <li>Filters for files with .edf extension (case-insensitive)</li>
     *   <li>Parses each file, handling both valid and invalid files</li>
     *   <li>Logs statistics about loaded files (total, valid, invalid)</li>
     * </ol>
     *
     * <p><b>Behavior for edge cases:</b></p>
     * <ul>
     *   <li>If directory is empty: returns gracefully (no exception)</li>
     *   <li>If file is corrupted: marks as invalid but continues processing others</li>
     *   <li>If file parsing fails: logs error and includes in results as invalid</li>
     * </ul>
     *
     * @throws EdfSourceNotFoundException if the configured directory does not exist or is not a valid directory
     */
    @PostConstruct
    public void loadEdfs() {
        // clear local list first
        edfs.clear();

        File directory = new File(properties.getFullEdfSource());
        log.info("Start loading of EDF files from {}", directory.getAbsolutePath());

        if (!directory.exists() || !directory.isDirectory()) {
            log.error("EDF directory not found");
            throw new EdfSourceNotFoundException("EDF directory not found in: %s".formatted(properties.getEdfSource()));
        }

        //read only *.edf files
        File[] edfFiles = directory.listFiles((dirName, fileName) -> fileName.toLowerCase().endsWith(".edf"));

        //check if edf source is empty, if yes, return empty list which is a valid state
        if (edfFiles == null || edfFiles.length == 0) {
            log.warn("No EDF files found in directory: {}", directory.getAbsolutePath());
            return;
        }

        // add all parsed edfs file to cache
        edfs.addAll(Arrays.stream(edfFiles)
                .map(this::parseEdfFile)
                .toList());

        // log statistics of valid/invalid files
        long validEdfFiles = edfs.stream().filter(e -> e.getEdfFileProperties().isValidEdf()).count();
        log.info("Loaded {} EDF files, valid:{}, invalid:{} ", edfs.size(),validEdfFiles, edfs.size()-validEdfFiles);

        log.info("Loaded {} EDF files from {}", edfs.size(), directory.getAbsolutePath());
    }

    /**
     * Parses a single EDF file and converts it to a domain entity.
     *
     * <p>This private method handles the parsing of individual files using the
     * {@link EdfReader}. It gracefully handles parsing failures by creating
     * invalid {@link EdfData} objects rather than throwing exceptions.</p>
     *
     * <p><b>Parsing process:</b></p>
     * <ol>
     *   <li>Attempts to read and parse the file using EDFParser</li>
     *   <li>If successful: extracts header data and creates valid EdfData</li>
     *   <li>If failed: creates invalid EdfData with error message</li>
     *   <li>Counts annotations if present in the parsed result</li>
     * </ol>
     *
     * <p><b>Exception handling:</b> All exceptions from the parsing process
     * are caught by {@link EdfReader}, which returns an empty Optional.
     * This method then creates an invalid EdfData object.</p>
     *
     * @param file the EDF file to parse
     * @return {@link EdfData} entity representing the file (valid or invalid)
     */
    private EdfData parseEdfFile(File file) {
        Optional<EDFParserResult> result = EDF_READER.readEdfFile(file);

        if (result.isEmpty()) {
            log.error("File {}: invalid", file.getName());
            // Create invalid EdfData
            return EdfData.createInvalidEdfData(file.getName(),"Invalid EDF File");
        }

        log.info("File {}: valid", file.getName());
        EDFHeader edfh = result.get().getHeader();
        var annotations = result.get().getAnnotations();
        var annotationsCount = annotations != null ? annotations.size() : 0;

        log.debug("Successfully parsed: {}", file.getName());
        // Use factory method with all required data to create a valid EdfData
        return EdfData.createValidEdfData(file.getName(),
                                                    edfh.getRecordingID(),
                                                    edfh.getStartDate(),
                                                    edfh.getStartTime(),
                                                    edfh.getSubjectID(),
                                                    edfh.getChannelLabels(),
                                                    edfh.getTransducerTypes(),
                                                    edfh.getNumberOfRecords(),
                                                    edfh.getDurationOfRecords(),
                                                    annotationsCount);
    }

    /**
     * Returns an immutable copy of all loaded EDF files.
     *
     * <p>This method provides read-only access to the in-memory cache of EDF files.
     * The returned list is immutable to prevent external modification of the
     * repository's internal state.</p>
     *
     * <p><b>Note:</b> The returned list includes both valid and invalid files.
     * Callers should check {@code EdfData.isValid()} to distinguish between them.</p>
     *
     * @return Immutable list of all loaded EDF file data
     */
    public List<EdfData> listEdfs() {
        return List.copyOf(edfs);
    }
}
