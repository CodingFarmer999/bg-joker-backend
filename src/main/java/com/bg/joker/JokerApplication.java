package com.bg.joker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class JokerApplication {

	public static void main(String[] args) {
		SpringApplication.run(JokerApplication.class, args);
	}

}
