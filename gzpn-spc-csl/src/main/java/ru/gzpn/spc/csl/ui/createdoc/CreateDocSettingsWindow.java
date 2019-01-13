package ru.gzpn.spc.csl.ui.createdoc;

import org.springframework.context.MessageSource;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.VerticalLayout;

import ru.gzpn.spc.csl.services.bl.interfaces.IDataUserSettigsService;
import ru.gzpn.spc.csl.ui.settings.UISettingsWindow;

public class CreateDocSettingsWindow extends UISettingsWindow {

	private static final long serialVersionUID = 1L;
	private MessageSource messageSource;
	
	public CreateDocSettingsWindow(IDataUserSettigsService settingsService, MessageSource messageSource) {
		super(settingsService);
		this.messageSource = messageSource;
	}

	@Override
	public VerticalLayout createBodyLayout() {
		VerticalLayout layout = new VerticalLayout();
		//Split
		return layout;
	}

	@Override
	public void refreshUiElements() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refreshUiSettings() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getI18nText(String key) {
		return messageSource.getMessage(key, null, VaadinSession.getCurrent().getLocale());
	}
}
