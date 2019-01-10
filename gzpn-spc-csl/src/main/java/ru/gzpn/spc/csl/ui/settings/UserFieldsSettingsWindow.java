package ru.gzpn.spc.csl.ui.settings;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import ru.gzpn.spc.csl.model.jsontypes.UserSettingsJson;
import ru.gzpn.spc.csl.services.bl.interfaces.IDataUserSettigsService;

public abstract class UserFieldsSettingsWindow extends Window {

	private static final long serialVersionUID = 1L;
	protected IDataUserSettigsService settingsService;
	
	public UserFieldsSettingsWindow(IDataUserSettigsService settingsService) {
		this.settingsService = settingsService;
		createLayout();
	}
	
	public void createLayout() {
		VerticalLayout vLayout = new VerticalLayout();
		HorizontalLayout hLayout = new HorizontalLayout();
	}
	
	public void save(UserSettingsJson uiSettings) {
		String user = settingsService.getCurrentUser();
		settingsService.save(user, uiSettings);
	}
}
