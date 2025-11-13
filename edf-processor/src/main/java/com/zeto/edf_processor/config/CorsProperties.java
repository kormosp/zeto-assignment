package com.zeto.edf_processor.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Data
@Component
@ConfigurationProperties(prefix = "cors")
public class CorsProperties {
    private String[] allowedOrigins;
    @NotEmpty(message = "App Interface must allow the most restrictive http method requests")
    private String[] allowedMethods;
    private boolean allowCredentials;
}
