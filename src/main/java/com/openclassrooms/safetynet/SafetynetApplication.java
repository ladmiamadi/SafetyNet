package com.openclassrooms.safetynet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpringBootApplication
public class SafetynetApplication {
	private static final Logger LOG = (Logger) LogManager.getLogger(SafetynetApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(SafetynetApplication.class, args);
		LOG.info("Get Greeting Message Input: " );

	}

}
