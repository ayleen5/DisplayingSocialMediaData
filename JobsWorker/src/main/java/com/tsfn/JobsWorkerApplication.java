package com.tsfn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableFeignClients
@EnableTransactionManagement
@SpringBootApplication
public class JobsWorkerApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobsWorkerApplication.class, args);
	}

}
