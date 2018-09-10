package ru.gazprom_neft.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(exclude = org.activiti.spring.boot.SecurityAutoConfiguration.class)
public class SpringActivitiJpaApplication extends SpringBootServletInitializer {
	public static void main(String[] args) {
		SpringApplication.run(SpringActivitiJpaApplication.class, args);
	}
}
