package com.cinesphere.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CineSphereApplication {

	public static void main(String[] args) {
		SpringApplication.run(CineSphereApplication.class, args);
	}

}
