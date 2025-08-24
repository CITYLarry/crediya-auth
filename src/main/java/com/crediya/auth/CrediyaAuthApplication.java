package com.crediya.auth;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(
		title = "CrediYa - Authentication API",
		version = "1.0.0",
		description = "API for user management and authentication in the CrediYa platform."
))
@SpringBootApplication
public class CrediyaAuthApplication {
	public static void main(String[] args) {
		SpringApplication.run(CrediyaAuthApplication.class, args);
	}
}
