package ru.gzpn.spc.csl.ui.common;

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

public abstract class ItemCreatingWindow extends Window implements I18n {
	private static final long serialVersionUID = 1L;
	
	public static final String I18N_CANCELBUTTON_CAP = "ItemCreatingWindow.closeButton.cap";
	public static final String I18N_ADDBUTTON_CAP = "ItemCreatingWindow.addButton.cap";
	
	// event actions
	public static final Action ADD_ITEM_ACTION = new Action("addItemAction");
	public static final Action CLOSE_ACTION = new Action("closelAction");

	private IUIService service;
	private IUserSettigsService userSettingsService;
	protected MessageSource messageSource;
	protected String user;
	
	protected VerticalLayout bodyLayout;
	protected HorizontalLayout footerLayout;

	protected Button closeButton;
	protected Button addButton;

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
		listeners.put(ADD_ITEM_ACTION, new HashSet<>());
		listeners.put(CLOSE_ACTION, new HashSet<>());
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
		footerLayout.addComponent(createCloseButton());
		footerLayout.addComponent(createAddButton());
		
		return footerLayout;
	}

	public Component createCloseButton() {
		closeButton = new Button(getI18nText(I18N_CANCELBUTTON_CAP, messageSource));
		closeButton.addClickListener(listener -> 
			close()
		);
		
		return closeButton;
	}
	
	public Component createAddButton() {
		addButton = new Button(getI18nText(I18N_ADDBUTTON_CAP, messageSource));
		addButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
		addButton.addClickListener(listener -> {
			addItem(/* TODO */);
			refreshUiElements();
		});
		
		return addButton;
	}

	/**  
	 * Fill these UI elements with data from the {@code UserSettingsJson uiSettings}
	 */
	public abstract void refreshUiElements();
	
	public void addItem() {
		/* TODO */
		onAdd();
		this.close();
	}

	public void close() {
		onClose();
		this.close();
	}

	public void onClose() {
		handleAction(CLOSE_ACTION);
	}
	
	public void onAdd() {
		handleAction(ADD_ITEM_ACTION);
	}
	
	public void handleAction(Action action) {
		for (Listener listener : listeners.get(action)) {
			listener.componentEvent(new Event(this));
		}
	}
	
	public void addOnAddListener(Listener listener) {
		listeners.get(ADD_ITEM_ACTION).add(listener);
	}
	
	public void addOnCloseListener(Listener listener) {
		listeners.get(CLOSE_ACTION).add(listener);
	}
	
	@Override
	public boolean equals(Object obj) {
		return (obj != null && this == obj);
	}
}
