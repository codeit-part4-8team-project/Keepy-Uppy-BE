package com.keepyuppy.KeepyUppy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
@ConfigurationPropertiesScan
public class KeepyUppyApplication {

	public static void main(String[] args) {
		SpringApplication.run(KeepyUppyApplication.class, args);
	}

}
