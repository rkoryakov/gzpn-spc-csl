package ru.gzpn.spc.csl.configurations;

import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.vaadin.spring.access.ViewAccessControl;
import com.vaadin.ui.UI;

import ru.gzpn.spc.csl.ui.views.AdminView;

@Component
public class VaadinAccessController implements ViewAccessControl {
	public static final Logger logger = LoggerFactory.getLogger(VaadinAccessController.class);

	@Override
	public boolean isAccessGranted(UI ui, String beanName) {
		boolean result = false;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null) {
			Set<String> authorities = authentication.getAuthorities().stream().map(a -> a.getAuthority())
					.collect(Collectors.toSet());

			logger.debug("name: {0}, authorities {1}", authentication.getName(), authorities);
			if (beanName != null) {
				switch (beanName) {
				case AdminView.ADMIN_VIEW:
					result = authorities.contains("ADMIN_ROLE");
					break;
				case "view n":
					break;
				}
			}
		}

		return result;
	}

}
