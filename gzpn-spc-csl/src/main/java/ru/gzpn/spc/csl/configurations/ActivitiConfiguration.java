package ru.gzpn.spc.csl.configurations;

import javax.sql.DataSource;

import org.activiti.engine.IdentityService;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

public class ActivitiConfiguration {

	@Autowired
	public DataSource dataSource;

	@Bean
	public InitializingBean initial(IdentityService identityService) {
		return () -> {
			// TODO: add some initialization
		};
	}

	@Bean
	public ProcessEngineFactoryBean processEngine() {
		ProcessEngineFactoryBean result = new ProcessEngineFactoryBean();
		result.setProcessEngineConfiguration(processEngineConfiguration());

		return result;
	}

	@Bean
	public ProcessEngineConfigurationImpl processEngineConfiguration() {
		SpringProcessEngineConfiguration configuration = new SpringProcessEngineConfiguration();
		configuration.setDataSource(dataSource);

		return configuration;
	}

	@Bean
	public IdentityService identityService() throws Exception {
		return processEngine().getObject().getIdentityService();
	}
}
