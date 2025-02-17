package com.bbc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = "com.bbc")
public class Team31Application {

	public static void main(String[] args) {
		SpringApplication.run(Team31Application.class, args);
	}

}
