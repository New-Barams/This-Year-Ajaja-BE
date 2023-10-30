package com.newbarams.ajaja;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@SpringBootApplication
public class AjajaApplication {
	public static void main(String[] args) {
		SpringApplication.run(AjajaApplication.class, args);
	}

	@PostMapping("/health-check")
	public ResponseEntity<Void> healthCheck() {
		return ResponseEntity.noContent().build();
	}
}
