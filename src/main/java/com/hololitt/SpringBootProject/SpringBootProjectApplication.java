package com.hololitt.SpringBootProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.hololitt.SpringBootProject")
public class SpringBootProjectApplication {

	public static void main(String[] args) {
		System.out.println("java version: " + System.getProperty("java.version"));
		SpringApplication.run(SpringBootProjectApplication.class, args);

	}

}
