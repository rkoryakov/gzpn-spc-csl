package ru.gzpn.spc.csl.configurations;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import ru.gzpn.spc.csl.model.BaseEntity;
import ru.gzpn.spc.csl.model.interfaces.ICProject;

@Configuration
@EnableJpaRepositories(basePackageClasses = { BaseEntity.class })
@ComponentScan(basePackageClasses = { BaseEntity.class, ICProject.class })
public class BaseConfiguration {

}
