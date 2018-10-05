package ru.gzpn.spc.csl.configurations;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import ru.gzpn.spc.csl.model.BaseEntity;
import ru.gzpn.spc.csl.model.HProject;
import ru.gzpn.spc.csl.model.interfaces.ICProject;
import ru.gzpn.spc.csl.services.bl.LoginController;
import ru.gzpn.spc.csl.ui.MainUI;
import ru.gzpn.spc.csl.ui.admin.UsersAndRoles;
import ru.gzpn.spc.csl.ui.views.AdminView;

@Configuration
@EnableJpaRepositories(basePackageClasses = { BaseEntity.class })
@ComponentScan(basePackageClasses = { BaseEntity.class, ICProject.class, MainUI.class, LoginController.class,
		AdminView.class, UsersAndRoles.class })
public class BaseConfiguration {

	@Bean
	public MessageSource messageSource() {
		final ResourceBundleMessageSource bundleMessageSource = new ResourceBundleMessageSource();
		bundleMessageSource.setBasename("i18n/captions");
		return bundleMessageSource;
	}

	@Bean
	@ConfigurationProperties(prefix = "app.datasource")
	public BasicDataSource dataSource() {
		return DataSourceBuilder.create().type(BasicDataSource.class).build();
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean projectEntityManagerFactory(EntityManagerFactoryBuilder builder) {
		return builder.dataSource(dataSource()).packages(HProject.class).persistenceUnit("projects").build();
	}
}
