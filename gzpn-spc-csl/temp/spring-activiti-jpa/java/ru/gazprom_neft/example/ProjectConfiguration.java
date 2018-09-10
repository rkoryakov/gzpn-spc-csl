package ru.gazprom_neft.example;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import ru.gazprom_neft.example.model.Project;
import ru.gazprom_neft.example.services.ApplicationService;

@Configuration
@EnableJpaRepositories(basePackages = {
		"ru.gazprom_neft.example.model" }, entityManagerFactoryRef = "projectEntityManagerFactory")
@ComponentScan(basePackages = { "ru.gazprom_neft.example.model", "ru.gazprom_neft.example.services" })
public class ProjectConfiguration {

	@Bean
	public CommandLineRunner init(final ApplicationService service) {
		return (args) -> {
			service.initRepository();
		};
	}

	@Bean
	@ConfigurationProperties(prefix = "app.datasource")
	public BasicDataSource dataSource() {
		return DataSourceBuilder.create().type(BasicDataSource.class).build();
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean projectEntityManagerFactory(EntityManagerFactoryBuilder builder) {
		return builder.dataSource(dataSource()).packages(Project.class).persistenceUnit("projects").build();
	}

}
