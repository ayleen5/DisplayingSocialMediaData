package com.tsfn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;

@SpringBootApplication
@OpenAPIDefinition
@EnableFeignClients
public class ActionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ActionServiceApplication.class, args);
	}

}
