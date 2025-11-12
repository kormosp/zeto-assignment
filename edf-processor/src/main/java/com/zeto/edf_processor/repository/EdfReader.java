package com.zeto.edf_processor.repository;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ru.mipt.edf.EDFParser;
import ru.mipt.edf.EDFParserException;
import ru.mipt.edf.EDFParserResult;

import java.io.*;
import java.util.Optional;

/**
 * Utility class for reading and parsing EDF (European Data Format) files.
 *
 * <p>This class wraps the third-party EDFParser library and provides a clean,
 * exception-safe interface for reading EDF files. Exceptions are caught
 * and logged, with failures indicated by returning an empty Optional rather
 * than throwing exceptions.</p>
 *
 * <p><b>Key features:</b></p>
 * <ul>
 *   <li>Safe parsing that never throws exceptions to callers</li>
 *   <li>Comprehensive error logging for debugging</li>
 *   <li>Automatic resource cleanup (try-with-resources)</li>
 *   <li>Handles multiple types of failures (IO, parsing, unexpected)</li>
 * </ul>
 * @author Peter Kormos
 * @version 1.0
 * @see EDFParser
 * @see EDFParserResult
 */
@Slf4j
public class EdfReader {

    /**
     * Static instance of the EDF parser from the third-party library.
     * <p>The parser is stateless and can be safely reused across multiple invocations.</p>
     */
    private static final EDFParser EDF_PARSER = new EDFParser();

    /**
     * Reads and parses an EDF file, returning the parsed result if successful.
     *
     * <p>This method attempts to read and parse the specified EDF file using
     * the EDFParser library. All exceptions are caught and logged, ensuring
     * that parsing failures do not propagate to callers.</p>
     *
     * <p><b>Exception handling strategy:</b></p>
     * <table border="1">
     *   <tr>
     *     <th>Exception Type</th>
     *     <th>Cause</th>
     *     <th>Action</th>
     *   </tr>
     *   <tr>
     *     <td>{@link EDFParserException}</td>
     *     <td>Invalid EDF format, corrupted file</td>
     *     <td>Log error, return empty Optional</td>
     *   </tr>
     *   <tr>
     *     <td>{@link IOException}</td>
     *     <td>File not found, read permission denied</td>
     *     <td>Log error, return empty Optional</td>
     *   </tr>
     *   <tr>
     *     <td>{@link Exception}</td>
     *     <td>Any unexpected error (NPE, RuntimeException, etc.)</td>
     *     <td>Log error with full stack trace, return empty Optional</td>
     *   </tr>
     * </table>
     *
     * @param file the EDF file to read and parse (must not be null)
     * @return Optional containing the parsed result if successful, empty Optional if parsing failed
     */
    public Optional<EDFParserResult> readEdfFile(File file) {
        // Validate file before attempting to read
        if (file == null) {
            log.error("File is null");
            return Optional.empty();
        }

        try (InputStream is = new FileInputStream(file)) {
            return Optional.of(EDFParser.parseEDF(is));
        } catch (EDFParserException e) {
            log.error("Error at parsing of file: {} - {}", file.getName(), e.getMessage());
        } catch (IOException e) {
            log.error("IO error reading file: {} - {}", file.getName(), e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error processing file: {} - {}", file.getName(), e.getMessage(), e);
        }

        return Optional.empty();
    }
}
