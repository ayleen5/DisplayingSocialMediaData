package com.tsfn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;

@EnableFeignClients
@SpringBootApplication
@OpenAPIDefinition
public class LoadersServiceApplication {


	public static void main(String[] args) {
		SpringApplication.run(LoadersServiceApplication.class, args);
	}

}
