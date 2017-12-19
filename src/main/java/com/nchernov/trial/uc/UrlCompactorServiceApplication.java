package com.nchernov.trial.uc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "com.nchernov.trial.uc.app", "com.nchernov.trial.uc.rest", "com.nchernov.trial.uc.services" })
public class UrlCompactorServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UrlCompactorServiceApplication.class, args);
	}
}
