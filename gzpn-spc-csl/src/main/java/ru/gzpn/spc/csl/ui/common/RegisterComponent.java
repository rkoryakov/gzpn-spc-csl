package ru.gzpn.spc.csl.ui.common;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.context.MessageSource;

import com.vaadin.event.Action;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import ru.gzpn.spc.csl.services.bl.interfaces.IUIService;
import ru.gzpn.spc.csl.services.bl.interfaces.IUserSettigsService;
import ru.gzpn.spc.csl.ui.common.data.export.Exporter;
import ru.gzpn.spc.csl.ui.createdoc.CreateDocSettingsWindow;

public abstract class RegisterComponent extends VerticalLayout implements I18n {

		private static final long serialVersionUID = 1L;

		public static final String I18N_CREATEITEMBUTTON_CAP = "RegisterComponent.createItemButton.cap";
		public static final String I18N_OPENITEMBUTTON_CAP = "RegisterComponent.openItemButton.cap";
		// event actions
		public static final Action CREATE_ITEM_ACTION = new Action("createItemAction");
		public static final Action OPEN_ITEM_ACTION = new Action("openItemAction");

		private static final String I18N_SEARCHSETTINGS_DESC = "";

		private static final String I18N_SEARCHFIELD_PLACEHOLDER = "";

		private static final String I18N_DOWNLOADWORKSETBUTTON_DESC = "";

		private static final String I18N_USERLAYOUTSETTINGS_DESC = "";


		protected IUIService service;
		protected IUserSettigsService userSettingsService;
		protected MessageSource messageSource;
		protected String user;
		
		protected HorizontalLayout headFuturesLayout;
		protected VerticalLayout bodyLayout;
		protected HorizontalLayout footerLayout;

		protected Button createItemButton;
		protected Button openItemButton;
		protected Button registerFilterSettingsButton;
		protected Button downloadWorksetButton;
		
		protected TextField registerFilterField;
		
		private Map<Action, Set<Listener>> listeners;

		protected AbstractRegisterDataProvider registerDataProvider;

		private Grid registerGrid;

		private Button userLayoutSettingsButton;

		

		
		public RegisterComponent(IUIService service) {
			this.service = service;
			this.messageSource = service.getMessageSource();
			this.userSettingsService = service.getUserSettingsService();
			this.user = userSettingsService.getCurrentUser();
			
			initEventActions();
			createHeadFutures();
			createBody();
			createFooter();
			refreshUiElements();
		}

		public void setDataProvider(AbstractRegisterDataProvider provider) {
			this.registerDataProvider = provider;
		}
		
		protected void initEventActions() {
			listeners = new HashMap<>();
			listeners.put(CREATE_ITEM_ACTION, new HashSet<>());
			listeners.put(OPEN_ITEM_ACTION, new HashSet<>());
		}

		public void createHeadFutures() {
			headFuturesLayout = new HorizontalLayout();
			AbsoluteLayout layout = new AbsoluteLayout();
			layout.setStyleName("gzpn-head");
			layout.setHeight(50.0f, Unit.PIXELS);
			layout.setWidth(100.f, Unit.PERCENTAGE);
			headFuturesLayout.addComponent(createRegisterFilter());
			//headFuturesLayout.addComponent(createWorksetButtons());
			headFuturesLayout.addComponent(createExcelButton());
			layout.addComponent(createSettingsButton(), "top:5px; left:5px");
			layout.addComponent(headFuturesLayout, "top:5px; right:5px");
			this.addComponent(headFuturesLayout);	
		}
		
		public Component createRegisterFilter() {
			registerFilterField = new TextField();
			registerFilterField.setWidth(200.0f, Unit.PIXELS);
			registerFilterSettingsButton = new Button();
			registerFilterSettingsButton.setIcon(VaadinIcons.FILTER);
			registerFilterSettingsButton.setDescription(getI18nText(I18N_SEARCHSETTINGS_DESC, messageSource, I18N_SEARCHSETTINGS_DESC));
			JoinedLayout<TextField, Button> searchComp = new JoinedLayout<>(registerFilterField, registerFilterSettingsButton);
			registerFilterField.setPlaceholder(getI18nText(I18N_SEARCHFIELD_PLACEHOLDER, messageSource, I18N_SEARCHFIELD_PLACEHOLDER));
			
			registerFilterField.addValueChangeListener(e -> {
				registerDataProvider.getFilter().setCommonTextFilter(e.getValue());
				registerDataProvider.refreshAll();
			});
			return searchComp;
		}
		
		public Component createExcelButton() {
			downloadWorksetButton = new Button(VaadinIcons.TABLE);
			downloadWorksetButton.setDescription(getI18nText(I18N_DOWNLOADWORKSETBUTTON_DESC, messageSource, I18N_DOWNLOADWORKSETBUTTON_DESC));
			StreamResource excelStreamResource = new StreamResource(
					(StreamResource.StreamSource) () -> Exporter.exportAsExcel(registerGrid), getReportName());
			FileDownloader excelFileDownloader = new FileDownloader(excelStreamResource);
			excelFileDownloader.extend(downloadWorksetButton);
			
			return downloadWorksetButton;
		}
		
		protected String getReportName() {
			return "report.xls";
		}

		public Component createSettingsButton() {
			userLayoutSettingsButton = new Button();
			userLayoutSettingsButton.setIcon(VaadinIcons.COG_O);
			userLayoutSettingsButton.setDescription(getI18nText(I18N_USERLAYOUTSETTINGS_DESC, messageSource, I18N_USERLAYOUTSETTINGS_DESC));
			userLayoutSettingsButton.addClickListener(event -> {
				CreateDocSettingsWindow settingsWindow = new CreateDocSettingsWindow(userSettingsService, messageSource);
				settingsWindow.addOnSaveAndCloseListener(closeEvent -> {
					refreshUiElements();
				});
				getUI().getUI().addWindow(settingsWindow);
			});
			return userLayoutSettingsButton;
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
			footerLayout.setSizeFull();
			bodyLayout.setMargin(true);
			bodyLayout.setSpacing(true);
			footerLayout.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);
			footerLayout.addComponent(createCreateButton());
			footerLayout.addComponent(createOpenButton());
			return footerLayout;
		}

		public Component createCreateButton() {
			createItemButton = new Button(getI18nText(I18N_CREATEITEMBUTTON_CAP, messageSource, I18N_CREATEITEMBUTTON_CAP));
			createItemButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
			createItemButton.addClickListener(listener -> {
				createItem(/* TODO */);
				refreshUiElements();
			});
			return createItemButton;
		}

		public Component createOpenButton() {
			openItemButton = new Button(getI18nText(I18N_OPENITEMBUTTON_CAP, messageSource, I18N_OPENITEMBUTTON_CAP));
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
		
		public void createItem() {
			onCreate();
		}

		public void openItem() {
			onOpen();
		}

		public void onCreate() {
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
