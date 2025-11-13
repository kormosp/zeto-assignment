package com.zeto.edf_processor.service;

import com.zeto.edf_processor.dto.EdfDto;
import com.zeto.edf_processor.dto.EdfMapper;
import com.zeto.edf_processor.exceptions.EdfSourceNotFoundException;
import com.zeto.edf_processor.model.EdfData;
import com.zeto.edf_processor.repository.EdfDataRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for EDF (European Data Format) file operations.
 *
 * <p>This service acts as an intermediary between the controller and repository layers,
 * providing business logic such as data transformation and sorting. It uses MapStruct
 * for mapping between domain entities and DTOs.</p>
 *
 * <p>Key responsibilities:</p>
 * <ul>
 *   <li>Convert domain entities ({@link EdfData}) to DTOs ({@link EdfDto}) </li>
 *   <li>Provide sorted (by Recording Date) and unsorted views of EDF file data</li>
 *   <li>Trigger directory rescanning operations, result might be sorted based on the optional parameter</li>
 * </ul>
 *
 * @author Peter Kormos
 * @version 1.0
 * @see EdfDto
 * @see EdfData
 */
@Service
@Slf4j
@AllArgsConstructor
public class EdfService {
    /**
     * Repository for accessing EDF file data from the file system.
     */
    private final EdfDataRepository edfDataRepository;

    /**
     * MapStruct mapper for converting between domain entities and DTOs.
     */
    private final EdfMapper edfMapper;

    /**
     * Retrieves all EDF files in their original order (unsorted).
     *
     * <p>This method returns files in the order they were read from the file system,
     * without any sorting applied. For a sorted view, use {@link #listEdfsSortedByRecordingDate()}.</p>
     *
     * <p>Both valid and invalid files are included in the response. Invalid files
     * will have {@code validEdf = false} and an error message.</p>
     *
     * @return List of all EDF files as DTOs in their original order
     */
    public List<EdfDto> listEdfs() {
        return edfMapper.toDto(edfDataRepository.listEdfs());
    }

    /**
     * Triggers a rescan of the EDF directory and returns the updated file list, sorted or unsorted
     *
     * <p>This method performs the following operations:</p>
     * <ol>
     *   <li>Clears the in-memory cache of previously loaded files</li>
     *   <li>Rescans the configured directory for .edf files</li>
     *   <li>Parses all found files (both valid and invalid)</li>
     *   <li>Returns the updated list in original or sorted order</li>
     * </ol>
     *
     * <p><b>Use cases:</b></p>
     * <ul>
     *   <li>New EDF files have been added to the directory</li>
     *   <li>Existing files have been modified or removed</li>
     *   <li>Initial directory scan failed and needs to be retried</li>
     * </ul>
     *
     * @return List of all EDF files after rescanning the directory
     * @param sorted : boolean, result shall be sorted by recording date
     * @throws EdfSourceNotFoundException, if the configured directory does not exist or is not accessible
     */
    public List<EdfDto> rescan(boolean sorted) {
        log.debug("Rescanning EDF source and return all records");
        edfDataRepository.loadEdfs();
        return sorted ? listEdfsSortedByRecordingDate()
                      : listEdfs();
    }

    /**
     * Retrieves all EDF files sorted by recording date (newest first, nulls last).
     *
     * <p>Files are sorted in descending order by their recording date
     * (most recent recordings appear first). Invalid files or files without
     * a recording date are placed at the end of the list.</p>
     *
     * <p><b>Sorting rules:</b></p>
     * <ul>
     *   <li>Valid files with dates: sorted newest to oldest</li>
     *   <li>Files with null dates: appear at the end</li>
     *   <li>Invalid files: have null dates, so appear at the end</li>
     * </ul>
     *
     * @return List of all EDF files sorted by recording date (descending)
     */
    public List<EdfDto> listEdfsSortedByRecordingDate() {
        log.debug("Get all EDF records and return them sorted");
        return listEdfs().stream()
                .sorted(Comparator.comparing(EdfDto::getRecordingDate,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .toList();
    }
}
