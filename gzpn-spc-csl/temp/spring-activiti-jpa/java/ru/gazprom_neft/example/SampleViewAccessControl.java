package ru.gazprom_neft.example;

import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.vaadin.spring.access.ViewAccessControl;
import com.vaadin.ui.UI;

/**
 * This demonstrates how you can control access to views.
 */
@Component
public class SampleViewAccessControl implements ViewAccessControl {
	public static final Logger logger = LoggerFactory.getLogger(SampleViewAccessControl.class);

	@Override
	public boolean isAccessGranted(UI ui, String beanName) {
		boolean result = false;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		logger.debug("authentication {}", authentication);
		if (authentication != null) {
			logger.debug("beanName {}", beanName);
			Set<String> authorities = authentication.getAuthorities().stream().map(a -> a.getAuthority())
					.collect(Collectors.toSet());

			if (beanName.equals("adminView")) {
				result = authorities.contains("ROLE_ADMIN");
			} else {
				result = authorities.contains("ROLE_USER");
			}
			logger.debug("result {}", result);
		}
		return result;
	}
}