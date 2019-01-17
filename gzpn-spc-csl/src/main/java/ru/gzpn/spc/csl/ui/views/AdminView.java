package ru.gzpn.spc.csl.ui.views;

import javax.annotation.PostConstruct;

import org.activiti.engine.IdentityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.vaadin.navigator.View;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

import ru.gzpn.spc.csl.services.bl.interfaces.IDataProjectService;
import ru.gzpn.spc.csl.ui.admin.UsersAndRolesVerticalLayout;
import ru.gzpn.spc.csl.ui.admin.project.ProjectPermissionsVerticalLayout;

@SpringView(name = AdminView.NAME)
@UIScope
public class AdminView extends VerticalLayout implements View {

	public static final String NAME = "adminView";
	public static final Logger logger = LoggerFactory.getLogger(AdminView.class);
	public static final String I18N_CAPTION_TAB_ADMIN = "adminView.caption.admin";
	public static final String I18N_CAPTION_TAB_PROJECT = "adminView.caption.project";
	@Autowired
	IdentityService identityService;
	@Autowired
	MessageSource messageSource;
	@Autowired
	IDataProjectService projectService;

	public AdminView() {
		setMargin(true);
		setSpacing(true);
	}

	@PostConstruct
	public void init() {
		TabSheet tabSet = new TabSheet();
		UsersAndRolesVerticalLayout admin = new UsersAndRolesVerticalLayout(identityService, messageSource);
		ProjectPermissionsVerticalLayout project = new ProjectPermissionsVerticalLayout(projectService, identityService, messageSource);
		tabSet.addTab(admin, getI18nText(I18N_CAPTION_TAB_ADMIN));
		tabSet.addTab(project, getI18nText(I18N_CAPTION_TAB_PROJECT));
		addComponent(tabSet);
	}
	
	private String getI18nText(String key) {
		return messageSource.getMessage(key, null, VaadinSession.getCurrent().getLocale());
	}
}
