package ru.gzpn.spc.csl.ui.processmanager;

import org.springframework.context.MessageSource;

import com.vaadin.ui.VerticalLayout;

import ru.gzpn.spc.csl.services.bl.interfaces.IUIService;
import ru.gzpn.spc.csl.services.bl.interfaces.IUserSettingsService;
import ru.gzpn.spc.csl.ui.common.I18n;

@SuppressWarnings("serial")
public abstract class AbstractAnalyticsComponent extends VerticalLayout implements I18n {
	
	protected IUIService service;
	private MessageSource messageSource;
	private IUserSettingsService userSettingsService;
	private String user;
	private VerticalLayout bodyLayout;

	public AbstractAnalyticsComponent(IUIService service) {
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
		// TODO Auto-generated method stub
		
	}

	private void createFooter() {
		// TODO Auto-generated method stub
		
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
