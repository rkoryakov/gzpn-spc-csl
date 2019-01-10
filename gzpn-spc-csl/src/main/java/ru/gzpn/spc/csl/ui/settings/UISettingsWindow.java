package ru.gzpn.spc.csl.ui.settings;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import ru.gzpn.spc.csl.model.jsontypes.UserSettingsJson;
import ru.gzpn.spc.csl.services.bl.interfaces.IDataUserSettigsService;

public abstract class UISettingsWindow extends Window {

	private static final long serialVersionUID = 1L;
	
	public static final String I18N_CANCELBUTTON_CAP = "settings.UISettingsWindow.cancelButton.cap";
	public static final String I18N_SAVEBUTTON_CAP = "settings.UISettingsWindow.saveButton.cap";
	
	protected IDataUserSettigsService settingsService;
	protected UserSettingsJson uiSettings;
	protected String user;
	
	protected VerticalLayout bodyLayout;
	protected HorizontalLayout footerLayout;

	protected Button cancelButton;
	protected Button saveButton;
	
	public UISettingsWindow(IDataUserSettigsService settingsService) {
		this.settingsService = settingsService;
		this.user = settingsService.getCurrentUser();
		createBody();
		createFooter();
		refreshUiElements();
	}
	
	public void createBody() {
		this.bodyLayout = createBodyLayout();
	}

	public abstract VerticalLayout createBodyLayout();

	public void createFooter() {
		Component footer = createFooterLayout();
		this.bodyLayout.addComponent(footer);
	}

	public Component createFooterLayout() {
		HorizontalLayout footerLayout = new HorizontalLayout();
		footerLayout.addComponent(createCancelButton());
		footerLayout.addComponent(createSaveButton());
		return footerLayout;
	}

	public Component createCancelButton() {
		cancelButton = new Button(getI18nText(I18N_CANCELBUTTON_CAP));
		cancelButton.addClickListener(listener -> 
			cancel()
		);
		
		return cancelButton;
	}
	
	public Component createSaveButton() {
		saveButton = new Button(getI18nText(I18N_SAVEBUTTON_CAP));
		saveButton.addClickListener(listener -> {
			save(this.uiSettings);
			refreshUiElements();
		});
		
		return saveButton;
	}

	/**  
	 * Fill these UI elements with data from the {@code UserSettingsJson uiSettings}
	 */
	public abstract void refreshUiElements();
	
	public void save(UserSettingsJson uiSettings) {
		onSave();
		refreshUiSettings();
		settingsService.save(user, uiSettings);
	}
	
	/**
	 * Fill the {@code UserSettingsJson uiSettings} with actual data
	 * from UI elements
	 */
	public abstract void refreshUiSettings();

	public void cancel() {
		onCancel();
		this.close();
	}

	public void onCancel() {

	}
	
	public void onSave() {

	}
	
	public abstract String getI18nText(String key);
}
