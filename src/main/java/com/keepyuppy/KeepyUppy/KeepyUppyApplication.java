package com.keepyuppy.KeepyUppy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class KeepyUppyApplication {

	public static void main(String[] args) {
		SpringApplication.run(KeepyUppyApplication.class, args);
	}

}
