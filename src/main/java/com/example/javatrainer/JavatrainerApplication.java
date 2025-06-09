package com.example.javatrainer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.example.javatrainer.entity")
public class JavatrainerApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavatrainerApplication.class, args);
	}

}
