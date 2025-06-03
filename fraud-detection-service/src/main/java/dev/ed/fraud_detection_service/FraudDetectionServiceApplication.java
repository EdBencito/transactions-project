package dev.ed.fraud_detection_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class FraudDetectionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FraudDetectionServiceApplication.class, args);
	}

}
