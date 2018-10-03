package ru.gzpn.spc.csl.configurations;

import javax.sql.DataSource;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

@Configuration
public class ActivitiConfiguration {

	@Autowired
	public DataSource dataSource;

	@Bean
	public InitializingBean usersAndGroupsInitializer(IdentityService identityService) {
		return () -> {
			if (identityService.createUserQuery().userId("act_user").singleResult() == null) {
				User user = identityService.newUser("act_user");
				user.setPassword(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("secret"));
				identityService.saveUser(user);

				Group group = identityService.newGroup("user");// createGroupQuery().groupId("").singleResult();
				group.setName("ROLE_USER");
				group.setType("USER");
				identityService.saveGroup(group);
				identityService.createMembership(user.getId(), group.getId());
			}
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
