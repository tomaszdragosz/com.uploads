package com.demo.uploads;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class UploadsApplication {

	public static void main(String[] args) {
		SpringApplication.run(UploadsApplication.class, args);
	}

}
