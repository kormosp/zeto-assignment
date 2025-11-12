package com.zeto.edf_processor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class EdfProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(EdfProcessorApplication.class, args);
	}

}
