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
import com.vaadin.ui.themes.ValoTheme;

import ru.gzpn.spc.csl.services.bl.interfaces.IUIService;
import ru.gzpn.spc.csl.services.bl.interfaces.IUserSettigsService;

public abstract class RegisterComponent extends VerticalLayout implements I18n {

		private static final long serialVersionUID = 1L;

		public static final String I18N_CREATEITEMBUTTON_CAP = "RegisterComponent.createItemButton.cap";
		public static final String I18N_OPENITEMBUTTON_CAP = "RegisterComponent.openItemButton.cap";
		// event actions
		public static final Action CREATE_ITEM_ACTION = new Action("createItemAction");
		public static final Action OPEN_ITEM_ACTION = new Action("openItemAction");


		private IUIService service;
		private IUserSettigsService userSettingsService;
		protected MessageSource messageSource;
		protected String user;
		
		protected VerticalLayout bodyLayout;
		protected HorizontalLayout footerLayout;

		protected Button createItemButton;
		protected Button openItemButton;

		private Map<Action, Set<Listener>> listeners;
		
		public RegisterComponent(IUIService service) {
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
			listeners.put(CREATE_ITEM_ACTION, new HashSet<>());
			listeners.put(OPEN_ITEM_ACTION, new HashSet<>());
		}

		public void createBody() {
			this.bodyLayout = createBodyLayout();
			this.addComponent(bodyLayout);
		}

		public abstract VerticalLayout createBodyLayout();

		public void createFooter() {
			HorizontalLayout footer = createFooterLayout();
			this.bodyLayout.addComponent(footer);
		}

		public HorizontalLayout createFooterLayout() {
			footerLayout = new HorizontalLayout();
			footerLayout.setDefaultComponentAlignment(Alignment.TOP_LEFT);
			footerLayout.addComponent(createCreateButton());
			footerLayout.addComponent(createOpenButton());
			return footerLayout;
		}

		private Component createCreateButton() {
			createItemButton = new Button(getI18nText(I18N_OPENITEMBUTTON_CAP, messageSource));
			createItemButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
			createItemButton.addClickListener(listener -> {
				createItem(/* TODO */);
				refreshUiElements();
			});
			return createItemButton;
		}

		public Component createOpenButton() {
			openItemButton = new Button(getI18nText(I18N_CREATEITEMBUTTON_CAP, messageSource));
			openItemButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
			openItemButton.addClickListener(listener -> {
				openItem(/* TODO */);
				refreshUiElements();
			});
			return openItemButton;
		}

		/**  
		 * Fill these UI elements with data from the {@code UserSettingsJson uiSettings}
		 */
		public abstract void refreshUiElements();
		
		private void createItem() {
			onCreate();
		}

		public void openItem() {
			onOpen();
		}

		private void onCreate() {
			handleAction(CREATE_ITEM_ACTION);
		}
		
		public void onOpen() {
			handleAction(OPEN_ITEM_ACTION);
		}
		
		public void handleAction(Action action) {
			for (Listener listener : listeners.get(action)) {
				listener.componentEvent(new Event(this));
			}
		}
		
		public void addOnCreateListener(Listener listener) {
			listeners.get(CREATE_ITEM_ACTION).add(listener);
		}
		
		public void addOnOpenListener(Listener listener) {
			listeners.get(OPEN_ITEM_ACTION).add(listener);
		}
		
		@Override
		public boolean equals(Object obj) {
			return (obj != null && this == obj);
		}
}
