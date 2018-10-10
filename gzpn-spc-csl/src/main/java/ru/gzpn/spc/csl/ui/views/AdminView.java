package ru.gzpn.spc.csl.ui.views;

import javax.annotation.PostConstruct;

import org.activiti.engine.IdentityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

import ru.gzpn.spc.csl.ui.admin.UsersAndRolesTab;

@SpringView(name = AdminView.NAME)
@UIScope
public class AdminView extends VerticalLayout implements View {

	public static final String NAME = "adminView";
	public static final Logger logger = LoggerFactory.getLogger(AdminView.class);

	@Autowired
	IdentityService identityService;
	@Autowired
	MessageSource messageSource;

	public AdminView() {
		setMargin(true);
		setSpacing(true);
	}

	@PostConstruct
	public void init() {
		TabSheet tabSet = new TabSheet();
		UsersAndRolesTab admin = new UsersAndRolesTab(identityService, messageSource);
		tabSet.addTab(admin, "Admin");
		addComponent(tabSet);
	}
}
