package ru.gzpn.spc.csl.ui.settings;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import ru.gzpn.spc.csl.services.bl.interfaces.IDataUserSettigsService;

public abstract class UserLayoutSettingsWindow extends Window {

	protected IDataUserSettigsService settingsService;
	
	public UserLayoutSettingsWindow(IDataUserSettigsService settingsService) {
		this.settingsService = settingsService;
		createLayout();
	}
	
	public void createLayout() {
		VerticalLayout vLayout = new VerticalLayout();
		HorizontalLayout hLayout = new HorizontalLayout();
		
	}
	
	
	public void save() {
		String user = settingsService.getCurrentUser();
		//settingsService.saveCreateDocSettingsJson(user, createDoc);
	}
}
