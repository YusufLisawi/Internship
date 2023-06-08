package com.nttdata.beca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BecaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BecaApplication.class, args);
	}

} 
