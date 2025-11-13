package com.zeto.edf_processor.config;

import com.zeto.edf_processor.repository.EdfDataRepository;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * Configuration properties for the EDF processor application.
 *
 * <p>This class binds external configuration from {@code application.properties}
 * to strongly-typed Java objects using Spring Boot's {@link ConfigurationProperties}
 * mechanism. It provides type-safe access to application configuration.</p>
 *
 * <p><b>Configuration in application.properties:</b></p>
 * <pre>
 * Path to project root
 * edf.edf-app-dir = ${user.dir}/..
 *
 * Directory contains edf files in project root
 * edf.edf-source = data/edf
 * </pre>
 *
 * <p><b>Validation:</b> The application validates the directory exists and
 * is readable during startup in {@link EdfDataRepository}.</p>
 *
 * @author Peter Kormos
 * @version 1.0
 * @see ConfigurationProperties
 */
@Data
@Component
@ConfigurationProperties(prefix = "edf")
@Validated
public class EdfProcessorProperties {
    @NotBlank(message = "App root directory must be configured")
    private String edfAppDir;
    @NotBlank(message = "EDF source directory must be configured")
    private String edfSource;

    // return the String of Absolute Path to directory containing the  edf files
    public String getEdfSourceDirectory() {
        return getEdfSourcePath().toString();
    }

    /**
     * Returns the absolute path to the directory containing EDF files
     * using Path for proper cross-platform path handling
     */
    public Path getEdfSourcePath() {
        return Paths.get(edfAppDir, edfSource).toAbsolutePath().normalize();
    }
}
