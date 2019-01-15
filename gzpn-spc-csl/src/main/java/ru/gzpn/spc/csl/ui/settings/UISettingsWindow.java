package ru.gzpn.spc.csl.ui.settings;

import java.util.Objects;

import org.springframework.context.MessageSource;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import ru.gzpn.spc.csl.model.jsontypes.CreateDocSettingsJson;
import ru.gzpn.spc.csl.services.bl.interfaces.IDataUserSettigsService;

public abstract class UISettingsWindow extends Window {

	private static final long serialVersionUID = 1L;
	
	public static final String I18N_CANCELBUTTON_CAP = "settings.UISettingsWindow.cancelButton.cap";
	public static final String I18N_SAVEBUTTON_CAP = "settings.UISettingsWindow.saveButton.cap";
	
	protected IDataUserSettigsService settingsService;
	protected CreateDocSettingsJson userSettings;
	protected MessageSource messageSource;
	protected String user;
	
	protected VerticalLayout bodyLayout;
	protected HorizontalLayout footerLayout;

	protected Button cancelButton;
	protected Button saveButton;
	
	public UISettingsWindow(IDataUserSettigsService settingsService, MessageSource messageSource) {
		this.settingsService = settingsService;
		this.user = settingsService.getCurrentUser();
		this.messageSource = messageSource;
		
		createBody();
		createFooter();
		refreshUiElements();
	}
	
	public CreateDocSettingsJson getUiSettings() {
		if (Objects.isNull(userSettings)) {
			userSettings = settingsService.getUserSettings();
		}
		return userSettings;
	}

	public void setUiSettings(CreateDocSettingsJson uiSettings) {
		this.userSettings = uiSettings;
	}

	public void createBody() {
		this.bodyLayout = createBodyLayout();
		this.setContent(bodyLayout);
	}

	public abstract VerticalLayout createBodyLayout();

	public void createFooter() {
		HorizontalLayout footer = createFooterLayout();
		this.bodyLayout.addComponent(footer);
	}

	public HorizontalLayout createFooterLayout() {
		footerLayout = new HorizontalLayout();
		footerLayout.setDefaultComponentAlignment(Alignment.TOP_RIGHT);
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
			save(this.userSettings);
			refreshUiElements();
		});
		
		return saveButton;
	}

	/**  
	 * Fill these UI elements with data from the {@code UserSettingsJson uiSettings}
	 */
	public abstract void refreshUiElements();
	
	public void save(CreateDocSettingsJson userSettings) {
		onSave();
		refreshSettings();
		settingsService.save(user, userSettings);
	}
	
	/**
	 * Fill the {@code UserSettingsJson userSettings} with actual data
	 * from UI elements
	 */
	public abstract void refreshSettings();

	public void cancel() {
		onCancel();
		this.close();
	}

	public void onCancel() {

	}
	
	public void onSave() {

	}
	
	@Override
	public boolean equals(Object obj) {
		return (obj != null && this == obj);
	}

	public abstract String getI18nText(String key);
}
