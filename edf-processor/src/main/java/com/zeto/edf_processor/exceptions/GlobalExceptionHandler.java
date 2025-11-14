package com.zeto.edf_processor.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


/**
 * Global exception handler for the EDF processor application.
 *
 * <p>This class provides centralized exception handling for all REST controllers,
 * converting exceptions into standardized RFC 7807 Problem Detail responses.
 * It ensures consistent error responses across the API and prevents sensitive
 * stack traces from being exposed to clients.</p>
 *
 * <p><b>Features:</b></p>
 * <ul>
 *   <li>Converts exceptions to RFC 7807 ProblemDetail format</li>
 *   <li>Maps exception types to appropriate HTTP status codes</li>
 *   <li>Logs errors for debugging and monitoring</li>
 *   <li>Provides user-friendly error messages</li>
 *   <li>Prevents stack trace exposure in production</li>
 * </ul>
 *
 * <p><b>Response format (JSON):</b></p>
 * <pre>
 * {
 *   "type": "about:blank",
 *   "title": "Not Found",
 *   "status": 404,
 *   "detail": "No EDF files found in directory",
 *   "instance": "/api/edfs"
 * }
 * </pre>
 *
 * <p><b>Configuration requirement:</b> Requires
 * {@code spring.mvc.problemdetails.enabled=true} in application.properties
 * for automatic ProblemDetail support.</p>
 *
 * @author Peter Kormos
 * @version 1.0
 * @see ControllerAdvice
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(EdfSourceNotFoundException.class)
    public ProblemDetail handleEdfSourceNotFound(EdfSourceNotFoundException ex) {
        log.error("EDF source not found: {}", ex.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(EdfDataNotFoundException.class)
    public ProblemDetail handleEdfDataNotFound(EdfDataNotFoundException ex) {
        log.error("EDF data not found: {}", ex.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleNotFound(Exception ex) {
        log.error("Unexpected error occurred: {}", ex.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

}

