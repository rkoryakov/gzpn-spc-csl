package ru.gzpn.spc.csl.ui.processmanager;

import org.springframework.context.MessageSource;

import com.vaadin.ui.VerticalLayout;

import ru.gzpn.spc.csl.services.bl.interfaces.IUIService;
import ru.gzpn.spc.csl.services.bl.interfaces.IUserSettigsService;
import ru.gzpn.spc.csl.ui.common.I18n;

@SuppressWarnings("serial")
public abstract class AbstractBpmnModelerComponent extends VerticalLayout implements I18n {

	private IUIService service;
	private MessageSource messageSource;
	private IUserSettigsService userSettingsService;
	private String user;
	private VerticalLayout bodyLayout;

	public AbstractBpmnModelerComponent(IUIService service) {
		this.service = service;
		this.messageSource = service.getMessageSource();
		this.userSettingsService = service.getUserSettingsService();
		this.user = userSettingsService.getCurrentUser();
		
		setSpacing(false);
		setMargin(false);

		initEventActions();
		createHeadFutures();
		createBody();
		createFooter();
		refreshUiElements();
	}

	private void refreshUiElements() {
		
	}

	private void createFooter() {
		
	}

	public void createBody() {
		this.bodyLayout = createBodyLayout();
		this.addComponent(bodyLayout);
	}

	public abstract VerticalLayout createBodyLayout();

	private void createHeadFutures() {
		// TODO Auto-generated method stub
		
	}

	private void initEventActions() {
		// TODO Auto-generated method stub
		
	}
}
