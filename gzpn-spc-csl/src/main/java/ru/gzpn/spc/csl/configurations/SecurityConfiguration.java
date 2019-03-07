package ru.gzpn.spc.csl.configurations;

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
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private IdentityService identityServ;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailService());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable() // Use Vaadin's CSRF protection
				.authorizeRequests().antMatchers("/login*").permitAll().anyRequest().authenticated().and()
					.formLogin().loginPage("/login").permitAll().and()
						.logout().logoutUrl("/logout").logoutSuccessHandler((request, response, auth) -> {
								RequestDispatcher dispatcher = request.getServletContext()
											.getRequestDispatcher("/sessionExpired");
								dispatcher.forward(request, response);
						}).permitAll().and()
							.sessionManagement().sessionFixation().newSession(); 
		
		http.headers().addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN));
		
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/css/*", "/VAADIN/themes/**");
	}

	@Bean
	public UserDetailsService userDetailService() {
		return new IdentityServiceUserDetailsService(this.identityServ);
	}
}
