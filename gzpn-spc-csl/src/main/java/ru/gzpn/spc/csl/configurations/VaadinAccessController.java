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

import ru.gzpn.spc.csl.services.bl.Roles;
import ru.gzpn.spc.csl.ui.views.AdminView;
import ru.gzpn.spc.csl.ui.views.ContractRegisterView;
import ru.gzpn.spc.csl.ui.views.CreateDocView;
import ru.gzpn.spc.csl.ui.views.EstimateRegisterView;
import ru.gzpn.spc.csl.ui.views.ProcessManagerView;
import ru.gzpn.spc.csl.ui.views.SummaryEstimateCardView;

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

			logger.debug("beanName {}, name: {}, authorities {}", beanName, authentication.getName(), authorities);
			if (authorities.contains(Roles.ADMIN_ROLE.toString())) {
				result = true;
			} else 
			if (beanName != null) {
				switch (beanName) {
				case AdminView.NAME:
					result = authorities.contains(Roles.ADMIN_ROLE.toString());
					break;
				case CreateDocView.NAME:
					result = authorities.contains(Roles.CREATOR_ROLE.toString());
					break;
				case ContractRegisterView.NAME:
					result = authorities.contains(Roles.CONTRACT_ES_ROLE.toString());
					break;
				case EstimateRegisterView.NAME:
					result = authorities.contains(Roles.EXPERT_ES_ROLE.toString());
					break;
				case ProcessManagerView.NAME:
					result = true;
					break;
				case SummaryEstimateCardView.NAME:
					result = authorities.contains(Roles.EXPERT_ES_ROLE.toString());
					break;
				}
			}
		}

		return result;
	}

}
