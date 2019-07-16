package com.statis.statis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "*", maxAge = 3600) 
@SpringBootApplication
@EnableScheduling 
public class StatisApplication {

	public static void main(String[] args) {
		SpringApplication.run(StatisApplication.class, args);
	}

}
