package ru.gzpn.spc.csl.ui.views;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewBeforeLeaveEvent;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

import ru.gzpn.spc.csl.services.bl.ProjectService;
import ru.gzpn.spc.csl.services.bl.UserSettigsService;
import ru.gzpn.spc.csl.services.bl.WorkSetService;
import ru.gzpn.spc.csl.services.bl.interfaces.IDataProjectService;
import ru.gzpn.spc.csl.services.bl.interfaces.IDataUserSettigsService;
import ru.gzpn.spc.csl.services.bl.interfaces.IWorkSetService;
import ru.gzpn.spc.csl.ui.createdoc.CreateDocLayout;

@SpringView(name = CreateDocView.NAME)
@UIScope
public class CreateDocView extends VerticalLayout implements View {

	private static final long serialVersionUID = 1L;
	public static final String NAME = "createDocView";
	public static final Logger logger = LoggerFactory.getLogger(CreateDocView.class);
	
	private IDataProjectService projectService;
	
	private IDataUserSettigsService userSettingsService;
	
	private IWorkSetService worksetService;
	
	@Autowired 
	private MessageSource messageSource;
	
	@Autowired
	public void setDataProjectService(ProjectService service) {
		projectService = service;
	}
	
	@Autowired
	public void setDataUserSettingsService(UserSettigsService service) {
		userSettingsService = service;
	}
	
	@Autowired
	public void setWorksetService(WorkSetService service) {
		worksetService = service;
	}
	
	public CreateDocView() {
		setMargin(false);
		setSpacing(false);
	}

	@PostConstruct
	void init() {
		CreateDocLayout layout = new CreateDocLayout(projectService, worksetService, userSettingsService, messageSource);
		addComponent(layout);
	}

	@Override
	public void enter(ViewChangeEvent event) {

	}

	@Override
	public void beforeLeave(ViewBeforeLeaveEvent event) {
		View.super.beforeLeave(event);
	}

	@Override
	public Component getViewComponent() {
		return View.super.getViewComponent();
	}

}
