package ru.gzpn.spc.csl.ui.estimatereg;

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
import ru.gzpn.spc.csl.ui.common.I18n;

public abstract class EstimateRegisterComponent extends VerticalLayout implements I18n {

		private static final long serialVersionUID = 1L;
		
		public static final String I18N_OPENITEMBUTTON_CAP = "EstimateRegisterComponent.addItemButton.cap";
		
		// event actions
		public static final Action OPEN_ITEM_ACTION = new Action("openItemAction");


		private IUIService service;
		private IUserSettigsService userSettingsService;
		protected MessageSource messageSource;
		protected String user;
		
		protected VerticalLayout bodyLayout;
		protected HorizontalLayout footerLayout;

		protected Button addItemButton;


		private Map<Action, Set<Listener>> listeners;
		
		public EstimateRegisterComponent(IUIService service) {
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
			
			footerLayout.addComponent(createOpenButton());
			
			return footerLayout;
		}

		
		public Component createOpenButton() {
			addItemButton = new Button(getI18nText(I18N_OPENITEMBUTTON_CAP, messageSource));
			addItemButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
			addItemButton.addClickListener(listener -> {
				openItem(/* TODO */);
				refreshUiElements();
			});
			
			return addItemButton;
		}

		/**  
		 * Fill these UI elements with data from the {@code UserSettingsJson uiSettings}
		 */
		public abstract void refreshUiElements();
		
		public void openItem() {
			/* TODO */
			onAdd();
		}

		
		public void onAdd() {
			handleAction(OPEN_ITEM_ACTION);
		}
		
		public void handleAction(Action action) {
			for (Listener listener : listeners.get(action)) {
				listener.componentEvent(new Event(this));
			}
		}
		
		public void addOnOpenListener(Listener listener) {
			listeners.get(OPEN_ITEM_ACTION).add(listener);
		}
		
		
		@Override
		public boolean equals(Object obj) {
			return (obj != null && this == obj);
		}


}
