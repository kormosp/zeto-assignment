package com.zeto.edf_processor.config;

import com.zeto.edf_processor.repository.EdfDataRepository;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;


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
public class EdfProcessorProperties {
    private String edfAppDir;
    private String edfSource;

    // returns Absolute Path to directory contains edf files
    public String getFullEdfSource() {
        return edfAppDir + File.separator + edfSource;
    }
}
