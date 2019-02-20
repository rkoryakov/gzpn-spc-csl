package ru.gzpn.spc.csl.ui.approval;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.MessageSource;

import com.vaadin.event.Action;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import ru.gzpn.spc.csl.services.bl.interfaces.IUIService;
import ru.gzpn.spc.csl.services.bl.interfaces.IUserSettigsService;
import ru.gzpn.spc.csl.ui.common.I18n;
import ru.gzpn.spc.csl.ui.common.RegistryComponent;
import ru.gzpn.spc.csl.ui.createdoc.CreateDocSettingsWindow;

public abstract class AbstractLocalEstimatesApprovalComponent extends VerticalLayout implements I18n {
	public static final Logger logger = LogManager.getLogger(RegistryComponent.class);
	private static final long serialVersionUID = 1L;

	public static final String I18N_CREATEITEMBUTTON_CAP = "RegisterComponent.createItemButton.cap";
	public static final String I18N_OPENITEMBUTTON_CAP = "RegisterComponent.openItemButton.cap";
	// event actions
	public static final Action APPROVE_ACTION = new Action("approveAction");
	public static final Action REJECT_ACTION = new Action("rejectAction");
	
	private static final String I18N_SEARCHSETTINGS_DESC = "";
	private static final String I18N_SEARCHFIELD_PLACEHOLDER = "RegisterComponent.filter.placeholder";
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
	private Button userLayoutSettingsButton;

	
	public AbstractLocalEstimatesApprovalComponent(IUIService service) {
		this.service = service;
		this.messageSource = service.getMessageSource();
		this.userSettingsService = service.getUserSettingsService();
		this.user = userSettingsService.getCurrentUser();
		
		setSpacing(false);
		setMargin(false);

		initEventActions();
		createBody();
		createFooter();
		refreshUiElements();
	}
	
	protected void initEventActions() {
		listeners = new HashMap<>();
		listeners.put(APPROVE_ACTION, new HashSet<>());
		listeners.put(REJECT_ACTION, new HashSet<>());
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
		this.bodyLayout.setSpacing(false);
		this.bodyLayout.setMargin(false);
		this.addComponent(bodyLayout);
	}

	public abstract VerticalLayout createBodyLayout();

	public void createFooter() {
		HorizontalLayout footer = createFooterLayout();
		this.bodyLayout.addComponent(footer);
	}

	public HorizontalLayout createFooterLayout() {
		footerLayout = new HorizontalLayout();
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		footerLayout.setSizeFull();
		footerLayout.setMargin(true);
		footerLayout.setSpacing(true);
		footerLayout.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);
		horizontalLayout.addComponent(createCreateButton());
		horizontalLayout.addComponent(createOpenButton());
		footerLayout.addComponent(horizontalLayout);
		
		return footerLayout;
	}

	public Component createCreateButton() {
		createItemButton = new Button(getI18nText(I18N_CREATEITEMBUTTON_CAP, messageSource, I18N_CREATEITEMBUTTON_CAP));
		createItemButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
		createItemButton.addClickListener(listener -> {
			approve(/* TODO */);
			refreshUiElements();
		});
		return createItemButton;
	}

	public Component createOpenButton() {
		openItemButton = new Button(getI18nText(I18N_OPENITEMBUTTON_CAP, messageSource, I18N_OPENITEMBUTTON_CAP));
		openItemButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
		openItemButton.addClickListener(listener -> {
			reject(/* TODO */);
			refreshUiElements();
		});
		return openItemButton;
	}

	public void refreshUiElements() {
		if (bodyLayout != null) {
			bodyLayout.removeAllComponents();
		}
		bodyLayout = createBodyLayout();
	}
	
	public void approve() {
		onApprove();
	}

	public void reject() {
		onReject();
	}

	public void onApprove() {
		handleAction(APPROVE_ACTION);
	}
	
	public void onReject() {
		handleAction(REJECT_ACTION);
	}
	
	public void handleAction(Action action) {
		for (Listener listener : listeners.get(action)) {
			listener.componentEvent(new Event(this));
		}
	}
	
	public void addOnApproveListener(Listener listener) {
		listeners.get(APPROVE_ACTION).add(listener);
	}
	
	public void addOnRejectListener(Listener listener) {
		listeners.get(REJECT_ACTION).add(listener);
	}
	
	@Override
	public boolean equals(Object obj) {
		return (obj != null && this == obj);
	}
}
