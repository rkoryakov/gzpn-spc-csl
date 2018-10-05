package ru.gzpn.spc.csl.ui.views;

import javax.annotation.PostConstruct;

import org.activiti.engine.IdentityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

import ru.gzpn.spc.csl.ui.admin.UsersAndRolesTab;

@SpringView(name = AdminView.ADMIN_VIEW)
@UIScope
public class AdminView extends VerticalLayout implements View {

	public static final String ADMIN_VIEW = "adminView";
	public static final Logger logger = LoggerFactory.getLogger(AdminView.class);

	@Autowired
	IdentityService identityService;

	public AdminView() {
		setMargin(true);
		setSpacing(true);
	}

	@PostConstruct
	public void init() {
		TabSheet tabSet = new TabSheet();
		UsersAndRolesTab admin = new UsersAndRolesTab(identityService);
		UsersAndRolesTab business = new UsersAndRolesTab(identityService);
		UsersAndRolesTab directory = new UsersAndRolesTab(identityService);
		tabSet.addTab(admin, "Admin");
		tabSet.addTab(business, "BPM");
		tabSet.addTab(directory, "Directory");
		addComponent(tabSet);
	}
}
