package ru.gzpn.spc.csl.configurations;

import javax.sql.DataSource;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

@Configuration
public class ActivitiConfiguration {
	public static final Logger logger = LoggerFactory.getLogger(ActivitiConfiguration.class);

	@Autowired
	public DataSource dataSource;

	@Bean
	public InitializingBean usersAndGroupsInitializer(IdentityService identityService) {
		return () -> {
			initUsersAndGroups(identityService);
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

	private void initUsersAndGroups(IdentityService identityService) {
		User admin = createOrGetUser("admin", "secret", identityService);
		User user = createOrGetUser("user", "secret", identityService);
		Group administrators = createOrGetGroup("administrators", "administrators", "ADMIN_ROLE", identityService);
		Group users = createOrGetGroup("users", "users", "USER_ROLE", identityService);

		try {
			identityService.createMembership(admin.getId(), administrators.getId());
		} catch (RuntimeException re) {
			logger.debug("Activiti Membership exists");
		}
		try {
			identityService.createMembership(user.getId(), users.getId());
		} catch (RuntimeException re) {
			logger.debug("Activiti Membership exists");
		}
	}

	private User createOrGetUser(String id, CharSequence pwd, IdentityService identityService) {
		User user = identityService.createUserQuery().userId(id).singleResult();
		if (user == null) {
			user = identityService.newUser(id);
			user.setPassword(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(pwd));
			identityService.saveUser(user);
		}

		return user;
	}

	private Group createOrGetGroup(String id, String type, String name, IdentityService identityService) {
		Group group = identityService.createGroupQuery().groupId(id).singleResult();
		if (group == null) {
			group = identityService.newGroup(id);
			group.setName(name);
			group.setType(type);
			identityService.saveGroup(group);
		}

		return group;
	}
}