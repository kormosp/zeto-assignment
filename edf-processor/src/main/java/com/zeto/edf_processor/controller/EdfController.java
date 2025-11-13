package com.zeto.edf_processor.controller;

import com.zeto.edf_processor.dto.EdfDto;
import com.zeto.edf_processor.exceptions.EdfSourceNotFoundException;
import com.zeto.edf_processor.service.EdfService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing EDF (European Data Format) file operations.
 *
 * <p>This controller provides endpoints for retrieving and rescanning EDF files
 * from a directory which is predefined as configuration property.
 * It serves as the main entry point for frontend applications.</p>
 *
 * <p>All endpoints return data in JSON format through {@link EdfDto} objects.</p>
 *
 * <p>Errors are returned in RFC-7807 format.</p>
 * @author Peter Kormos
 * @version 1.0
 */
@RestController
@RequestMapping("/api/edfs")
@RequiredArgsConstructor
@Slf4j
public class EdfController {

    private final EdfService edfService;

    /**
     * Retrieves all EDF files from the configured directory.
     *
     * <p>Returns both valid and invalid EDF files. Invalid files are marked
     * with appropriate error messages in the response.</p>
     *
     * <p><b>Example response:</b></p>
     * <pre>
     * [
     *   {
     *     "fileName": "patient001.edf",
     *     "validEdf": true,
     *     "recordingDate": "2024-01-15T10:30:00",
     *     "patientName": "John Doe",
     *     ...
     *   },
     *   {
     *     "fileName": "corrupted.edf",
     *     "validEdf": false,
     *     "errorMessage": "Invalid EDF file"
     *   }
     * ]
     * </pre>
     *
     * @return List of all EDF files as DTOs({@link EdfDto}) , including both valid and invalid files
     */
    @GetMapping
    public List<EdfDto> getAllEdfs() {
        log.debug("Fetching all EDF files");
        return edfService.listEdfs();
    }

    /**
     * Retrieves all EDF files from the configured directory sorted by the recording date
     *
     * <p>Returns both valid and invalid EDF files. Invalid files are placed at the end of the list and marked
     * with appropriate error messages in the response.</p>
     * @return List of all EDF files as DTOs({@link EdfDto}) sorted by the recording date, including both valid and invalid files
     */
    @GetMapping("/sorted")
    public List<EdfDto> getAllEdfsSortedByRecordingDate() {
        log.debug("Fetching all EDF files sorted by Recording Date");
        return edfService.listEdfsSortedByRecordingDate();
    }

    /**
     * Rescans the EDF directory and returns the updated list of files.
     *
     * <p>This endpoint triggers a scan of the source directory,
     * reloading all EDF files from disk. This is useful when files have been
     * added, removed, or modified in the directory.</p>
     *
     * <p><b>Note:</b> This operation clears the in-memory cache and reloads
     * all files.</p>
     *
     * @param sorted list shall be sorted by recording date, required:false, defaultValue:false
     * @return List of all EDF files after rescanning the directory
     * @throws EdfSourceNotFoundException, if the configured directory does not exist
     */
    @PostMapping("/rescan")
    public List<EdfDto> rescanSource(@RequestParam(defaultValue = "false") boolean sorted) {
        log.debug("Rescanning EDF source directory");
        List<EdfDto> edfDtos = edfService.rescan(sorted);
        return edfDtos;
    }

}
