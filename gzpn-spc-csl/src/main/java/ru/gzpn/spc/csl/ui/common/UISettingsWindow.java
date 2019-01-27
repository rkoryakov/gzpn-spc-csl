package ru.gzpn.spc.csl.ui.common;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.context.MessageSource;

import com.vaadin.event.Action;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import ru.gzpn.spc.csl.model.jsontypes.ISettingsJson;
import ru.gzpn.spc.csl.services.bl.interfaces.IUserSettigsService;

public abstract class UISettingsWindow extends Window implements I18n {

	private static final long serialVersionUID = 1L;
	
	public static final String I18N_CANCELBUTTON_CAP = "settings.UISettingsWindow.cancelButton.cap";
	public static final String I18N_SAVEBUTTON_CAP = "settings.UISettingsWindow.saveButton.cap";
	
	public static final String I18N_SETTINGS_COLUMN_ID = "settings.columnsGrid.columns.id";
	public static final String I18N_SETTINGS_COLUMN_ENTITY = "settings.columnsGrid.columns.entity";
	public static final String I18N_SETTINGS_COLUMN_VISIBLE = "settings.columnsGrid.columns.visible";
	public static final String I18N_SETTINGS_COLUMN_MERGEDHEAD = "settings.columnsGrid.columns.header";
	
	public static final String I18N_SETTINGS_HEADERS_COLUMN_HEAD = "settings.headersGrid.columns.header";
	public static final String I18N_SETTINGS_HEADERS_COLUMN_ID = "settings.headersGrid.columns.id";
	public static final String I18N_SETTINGS_HEADERS_COLUMN_VISIBLE = "settings.headersGrid.columns.visible";
	// event actions
	public static final Action SAVE_ACTION = new Action("saveAction");
	public static final Action CLOSE_ACTION = new Action("closeAction");
	public static final Action SAVE_AND_CLOSE_ACTION = new Action("save&closeAction");
	
	protected IUserSettigsService settingsService;
	protected ISettingsJson userSettings;
	protected MessageSource messageSource;
	protected String user;
	
	protected ComponentContainer bodyLayout;
	protected HorizontalLayout footerLayout;

	protected Button cancelButton;
	protected Button saveButton;

	private Map<Action, Set<Listener>> listeners;
	
	public UISettingsWindow(IUserSettigsService settingsService, MessageSource messageSource) {
		this.settingsService = settingsService;
		this.user = settingsService.getCurrentUser();
		this.messageSource = messageSource;
		this.userSettings = settingsService.getUserSettings();
		initEventActions();
		
		createBody();
		createFooter();
		refreshUiElements();
	}
	
	protected void initEventActions() {
		listeners = new HashMap<>();
		listeners.put(SAVE_ACTION, new HashSet<>());
		listeners.put(CLOSE_ACTION, new HashSet<>());
		listeners.put(SAVE_AND_CLOSE_ACTION, new HashSet<>());
	}
	
	public ISettingsJson getUiSettings() {
		if (Objects.isNull(userSettings)) {
			userSettings = settingsService.getUserSettings();
		}
		return userSettings;
	}

	public void setUiSettings(ISettingsJson uiSettings) {
		this.userSettings = uiSettings;
	}

	public void createBody() {
		this.bodyLayout = createBodyLayout();
		this.setContent(bodyLayout);
	}

	public abstract ComponentContainer createBodyLayout();

	public void createFooter() {
		HorizontalLayout footer = createFooterLayout();
		this.bodyLayout.addComponent(footer);
	}

	public HorizontalLayout createFooterLayout() {
		footerLayout = new HorizontalLayout();
		footerLayout.setDefaultComponentAlignment(Alignment.TOP_LEFT);
		footerLayout.addComponent(createCancelButton());
		footerLayout.addComponent(createSaveCloseButton());
		
		return footerLayout;
	}

	public Component createCancelButton() {
		cancelButton = new Button(getI18nText(I18N_CANCELBUTTON_CAP, messageSource));
		cancelButton.addClickListener(listener -> 
			close()
		);
		
		return cancelButton;
	}
	
	public Component createSaveCloseButton() {
		saveButton = new Button(getI18nText(I18N_SAVEBUTTON_CAP, messageSource));
		saveButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
		saveButton.addClickListener(listener -> {
			saveAndClose();
			refreshUiElements();
		});
		
		return saveButton;
	}

	public void saveAndClose() {
		save();
		closeSettings();
		onSaveAndClose();
	}

	/**  
	 * Fill these UI elements with data from the {@code UserSettingsJson uiSettings}
	 */
	public abstract void refreshUiElements();
	
	public void save() {
		refreshSettings();
		settingsService.save(user, userSettings);
		refreshUiElements();
		onSave();
	}
	
	/**
	 * Fill the {@code UserSettingsJson userSettings} with actual data
	 * from UI elements
	 */
	public abstract void refreshSettings();

	public void closeSettings() {
		onClose();
		this.close();
	}

	public void onClose() {
		handleAction(CLOSE_ACTION);
	}
	
	public void onSave() {
		handleAction(SAVE_ACTION);
	}
	
	public void onSaveAndClose() {
		handleAction(SAVE_AND_CLOSE_ACTION);
	}
	
	public void handleAction(Action action) {
		for (Listener listener : listeners.get(action)) {
			listener.componentEvent(new Event(this));
		}
	}
	
	public void addOnSaveListener(Listener listener) {
		listeners.get(SAVE_ACTION).add(listener);
	}
	
	public void addOnCloseListener(Listener listener) {
		listeners.get(CLOSE_ACTION).add(listener);
	}
	
	public void addOnSaveAndCloseListener(Listener listener) {
		listeners.get(SAVE_AND_CLOSE_ACTION).add(listener);
	}
	
	@Override
	public boolean equals(Object obj) {
		return (obj != null && this == obj);
	}
}
