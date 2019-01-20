package ru.gzpn.spc.csl.ui.settings;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.context.MessageSource;

import com.vaadin.event.Action;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import ru.gzpn.spc.csl.services.bl.interfaces.IUIService;
import ru.gzpn.spc.csl.services.bl.interfaces.IUserSettigsService;
import ru.gzpn.spc.csl.ui.common.I18n;

public abstract class ItemCreatingWindow extends Window implements I18n {
	private static final long serialVersionUID = 1L;
	
	public static final String I18N_CANCELBUTTON_CAP = "ItemCreatingWindow.cancelButton.cap";
	public static final String I18N_SAVEBUTTON_CAP = "ItemCreatingWindow.saveButton.cap";
	
	// event actions
	public static final Action SAVE_ACTION = new Action("saveAction");
	public static final Action CANCEL_ACTION = new Action("cancelAction");

	private IUIService service;
	private IUserSettigsService userSettingsService;
	protected MessageSource messageSource;
	protected String user;
	
	protected VerticalLayout bodyLayout;
	protected HorizontalLayout footerLayout;

	protected Button cancelButton;
	protected Button saveButton;

	private Map<Action, Set<Listener>> listeners;
	
	public ItemCreatingWindow(IUIService service) {
		this.service = service;
		this.messageSource = service.getMessageSource();
		this.userSettingsService = service.getUserSettingsService();
		
		initEventActions();
		
		createBody();
		createFooter();
		refreshUiElements();
	}
	
	protected void initEventActions() {
		listeners = new HashMap<>();
		listeners.put(SAVE_ACTION, new HashSet<>());
		listeners.put(CANCEL_ACTION, new HashSet<>());
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
		footerLayout.setDefaultComponentAlignment(Alignment.TOP_LEFT);
		footerLayout.addComponent(createCancelButton());
		footerLayout.addComponent(createSaveButton());
		
		return footerLayout;
	}

	public Component createCancelButton() {
		cancelButton = new Button(getI18nText(I18N_CANCELBUTTON_CAP, messageSource));
		cancelButton.addClickListener(listener -> 
			cancel()
		);
		
		return cancelButton;
	}
	
	public Component createSaveButton() {
		saveButton = new Button(getI18nText(I18N_SAVEBUTTON_CAP, messageSource));
		saveButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
		saveButton.addClickListener(listener -> {
			save(/* TODO */);
			refreshUiElements();
		});
		
		return saveButton;
	}

	/**  
	 * Fill these UI elements with data from the {@code UserSettingsJson uiSettings}
	 */
	public abstract void refreshUiElements();
	
	public void save() {
		refreshSettings();
		/* TODO */
		onSave();
		this.close();
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
		handleAction(CANCEL_ACTION);
	}
	
	public void onSave() {
		handleAction(SAVE_ACTION);
	}
	
	public void handleAction(Action action) {
		for (Listener listener : listeners.get(action)) {
			listener.componentEvent(new Event(this));
		}
	}
	
	public void addOnSaveListener(Listener listener) {
		listeners.get(SAVE_ACTION).add(listener);
	}
	
	public void addOnCancelListener(Listener listener) {
		listeners.get(CANCEL_ACTION).add(listener);
	}
	
	@Override
	public boolean equals(Object obj) {
		return (obj != null && this == obj);
	}
}
