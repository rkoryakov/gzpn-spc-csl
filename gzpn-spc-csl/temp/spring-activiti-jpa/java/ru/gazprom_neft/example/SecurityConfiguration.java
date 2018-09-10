package ru.gazprom_neft.example;

import javax.servlet.RequestDispatcher;

import org.activiti.engine.IdentityService;
import org.activiti.spring.security.IdentityServiceUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private IdentityService identityServ;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(userDetailService());
		// @formatter:off
//		auth.inMemoryAuthentication().withUser("admin").password("p").roles("ADMIN").and().withUser("user")
//				.password("p").roles("USER");
		// @formatter:on
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http.csrf().disable() // Use Vaadin's CSRF protection
				.authorizeRequests().anyRequest().authenticated() // User must be authenticated to access
																	// any part of the application
				.and().formLogin().loginPage("/login").permitAll() // Login page is accessible to anybody
				.and().logout().logoutUrl("/logout").logoutSuccessHandler((request, response, authentication) -> {
					// TODO Auto-generated method stub
					RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher("/");
					dispatcher.forward(request, response);

				}).permitAll() // Logout
				// success
				// page
//																									// is accessible to
//																									// anybody
				.and().sessionManagement().sessionFixation().newSession(); // Create completely new session
		// @formatter:on
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/css/*"); // Static resources are ignored
	}

	@Bean
	public UserDetailsService userDetailService() {
		return new IdentityServiceUserDetailsService(this.identityServ);
	}
}
