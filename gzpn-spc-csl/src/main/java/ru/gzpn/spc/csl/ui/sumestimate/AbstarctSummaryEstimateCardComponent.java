package ru.gzpn.spc.csl.ui.sumestimate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.MessageSource;

import com.vaadin.event.Action;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import ru.gzpn.spc.csl.services.bl.interfaces.IUIService;
import ru.gzpn.spc.csl.services.bl.interfaces.IUserSettigsService;
import ru.gzpn.spc.csl.ui.common.I18n;
import ru.gzpn.spc.csl.ui.common.RegistryComponent;

public abstract class AbstarctSummaryEstimateCardComponent extends VerticalLayout implements I18n {

	public static final Logger logger = LogManager.getLogger(RegistryComponent.class);
	private static final long serialVersionUID = 1L;

	// event actions
	public static final Action SEND_FOR_APPROVAL_ACTION = new Action("sendForApprovalAction");
	public static final Action CLOSE_ACTION = new Action("closeAction");
	public static final Action MAX_PRICE_ACTION = new Action("maxPriceAction");
	
	private static final String I18N_INITIALMAXPRICEBUTTON_CAP = "SummaryEstimateCardComponent.initialmaxpricebutton.cap";
	private static final String I18N_SENDFORAPPROVALBUTTON_CAP = "SummaryEstimateCardComponent.sendforapprovalbutton.cap";
	private static final String I18N_CLOSEBUTTON_CAP = "SummaryEstimateCardComponent.closebutton.cap";
	private static final String I18N_SETTINGSBUTTON_DESC = "SummaryEstimateCardComponent.settingsbutton.cap";
	private static final String I18N_SAVEBUTTON_DESC = "SummaryEstimateCardComponent.saveButton.desc";

	protected IUIService service;
	protected IUserSettigsService userSettingsService;
	protected MessageSource messageSource;
	protected String user;
	
	protected HorizontalLayout headFuturesLayout;
	protected VerticalLayout bodyLayout;
	protected HorizontalLayout footerLayout;

	protected Button initialMaxPriceButton;
	protected Button sendForApprovalButton;

	protected Button saveButton;

	private Map<Action, Set<Listener>> listeners;
	private Button settingsButton;
	private Button closeButton;
	private CssLayout calculationFieldsLayout;

	public abstract VerticalLayout createBodyLayout();
	
	
	public AbstarctSummaryEstimateCardComponent(IUIService service) {
		this.service = service;
		this.messageSource = service.getMessageSource();
		this.userSettingsService = service.getUserSettingsService();
		this.user = userSettingsService.getCurrentUser();
		
		setSpacing(false);
		setMargin(false);

		initEventActions();
		createHeadFutures();
		createBody();
		createFooter();
	}
	
	protected void initEventActions() {
		listeners = new HashMap<>();
		listeners.put(SEND_FOR_APPROVAL_ACTION, new HashSet<>());
		listeners.put(MAX_PRICE_ACTION, new HashSet<>());
		listeners.put(CLOSE_ACTION, new HashSet<>());
	}

	public void createHeadFutures() {
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(false);
		verticalLayout.setSpacing(false);
		AbsoluteLayout layout = new AbsoluteLayout();
		
		layout.setStyleName("gzpn-head");
		layout.setHeight(50.0f, Unit.PIXELS);
		layout.setWidth(100.f, Unit.PERCENTAGE);
		headFuturesLayout = new HorizontalLayout();
		headFuturesLayout.addComponent(createSaveButton());
		
		layout.addComponent(createSettingsButton(), "top:5px; left:5px");
		layout.addComponent(headFuturesLayout, "top:5px; right:5px");
		
		verticalLayout.addComponent(layout);
		this.addComponent(verticalLayout);	
	}
	
	public Component createSaveButton() {
		saveButton = new Button(VaadinIcons.CLOUD_DOWNLOAD_O);
		saveButton.setDescription(getI18nText(I18N_SAVEBUTTON_DESC, messageSource));
		
		return saveButton;
	}

	public Component createSettingsButton() {
		settingsButton = new Button();
		settingsButton.setIcon(VaadinIcons.SLIDERS);
		settingsButton.setDescription(getI18nText(I18N_SETTINGSBUTTON_DESC, messageSource));

		return settingsButton;
	}
	
	public void createBody() {
		bodyLayout = createBodyLayout();
		bodyLayout.setSpacing(false);
		bodyLayout.setMargin(false);
		refreshUiElements();
		addComponent(bodyLayout);
	}
	
	public void refreshUiElements() {
		bodyLayout.removeAllComponents();
		createBodyLayout();
	}


	public void createFooter() {
		HorizontalLayout footer = createFooterLayout();
		this.addComponent(footer);
	}

	public HorizontalLayout createFooterLayout() {
		footerLayout = new HorizontalLayout();
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		footerLayout.setSizeFull();
		footerLayout.setMargin(true);
		footerLayout.setSpacing(true);
		footerLayout.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);
		horizontalLayout.addComponent(createInitialMaxPriceButton());
		horizontalLayout.addComponent(createSendForApprovalButton());
		horizontalLayout.addComponent(createCloseButton());
		footerLayout.addComponent(horizontalLayout);
		
		return footerLayout;
	}

	public Component createInitialMaxPriceButton() {
		initialMaxPriceButton = new Button(getI18nText(I18N_INITIALMAXPRICEBUTTON_CAP, messageSource));
		initialMaxPriceButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		initialMaxPriceButton.setEnabled(false);
		initialMaxPriceButton.addClickListener(listener -> {

		});
		return initialMaxPriceButton;
	}

	public Component createSendForApprovalButton() {
		sendForApprovalButton = new Button(getI18nText(I18N_SENDFORAPPROVALBUTTON_CAP, messageSource));
		sendForApprovalButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
		sendForApprovalButton.addClickListener(listener -> {

		});
		return sendForApprovalButton;
	}

	public Component createCloseButton() {
		closeButton = new Button(getI18nText(I18N_CLOSEBUTTON_CAP, messageSource));
		closeButton.addClickListener(clickEvent -> {
			
		});
		
		return closeButton;
	}

	
	public void calculateInitialMaxPrice() {
		OnCalculateInitialMaxPrice();
	}

	public void sendForApproval() {
		onSendForApproval();
	}

	public void close() {
		onClose();
	}
	
	public void OnCalculateInitialMaxPrice() {
		handleAction(MAX_PRICE_ACTION);
	}
	
	public void onSendForApproval() {
		handleAction(SEND_FOR_APPROVAL_ACTION);
	}
	
	public void onClose() {
		handleAction(CLOSE_ACTION);
	}
	
	public void handleAction(Action action) {
		for (Listener listener : listeners.get(action)) {
			listener.componentEvent(new Event(this));
		}
	}
	
	public void addOnCalculateMaxPriceListener(Listener listener) {
		listeners.get(MAX_PRICE_ACTION).add(listener);
	}
	
	public void addOnSendForApprovalListener(Listener listener) {
		listeners.get(SEND_FOR_APPROVAL_ACTION).add(listener);
	}
	
	
	public void addOnCloseListener(Listener listener) {
		listeners.get(CLOSE_ACTION).add(listener);
	}
	
	@Override
	public boolean equals(Object obj) {
		return (obj != null && this == obj);
	}
}
