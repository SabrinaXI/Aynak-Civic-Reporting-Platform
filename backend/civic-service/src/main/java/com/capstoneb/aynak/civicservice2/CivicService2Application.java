package com.capstoneb.aynak.civicservice2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CivicService2Application {

	public static void main(String[] args) {
		SpringApplication.run(CivicService2Application.class, args);
	}

}
