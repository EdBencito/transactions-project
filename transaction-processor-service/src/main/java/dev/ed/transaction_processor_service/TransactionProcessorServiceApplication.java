package dev.ed.transaction_processor_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "dev.ed.transaction_processor_service.client")
public class TransactionProcessorServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionProcessorServiceApplication.class, args);
	}

}
