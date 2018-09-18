package ru.gzpn.spc.csl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import ru.gzpn.spc.csl.configurations.ActivitiConfiguration;
import ru.gzpn.spc.csl.configurations.BaseConfiguration;
import ru.gzpn.spc.csl.configurations.SecurityConfiguration;

@SpringBootApplication(exclude = org.activiti.spring.boot.SecurityAutoConfiguration.class)
@ComponentScan(basePackageClasses = { BaseConfiguration.class, ActivitiConfiguration.class, SecurityConfiguration.class })
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
