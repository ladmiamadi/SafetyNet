package com.openclassrooms.safetynet;

import com.openclassrooms.safetynet.dao.DataFromJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@SpringBootApplication
public class SafetynetApplication {
	private static final Logger LOG = (Logger) LogManager.getLogger(SafetynetApplication.class);
	public static void main(String[] args) throws IOException {
		SpringApplication.run(SafetynetApplication.class, args);
	}

}
