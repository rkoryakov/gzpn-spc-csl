package ru.gzpn.spc.csl.configurations;

import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.spring.access.ViewAccessControl;
import com.vaadin.ui.UI;

public class VaadinAccessController implements ViewAccessControl {
	public static final Logger logger = LoggerFactory.getLogger(VaadinAccessController.class);

	@Override
	public boolean isAccessGranted(UI ui, String beanName) {
		boolean result = false;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null) {
			Set<String> authorities = authentication.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.toSet());

			logger.debug("name: {0}, authorities {1}", authentication.getName(), authorities);
			if (beanName != null) {
				switch (beanName) {
				case "view 1":
					result = authorities.contains("ROLE1");
					break;
				case "view n":
					break;
				}
			}
		}

		return result;
	}

}
