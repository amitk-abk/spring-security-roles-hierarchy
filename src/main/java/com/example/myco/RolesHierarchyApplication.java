package com.example.myco;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.myco")
public class RolesHierarchyApplication {

	public static void main(String[] args) {
		SpringApplication.run(RolesHierarchyApplication.class, args);
	}
}
