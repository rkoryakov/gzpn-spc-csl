package ru.gzpn.spc.csl.ui.contract;

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

import ru.gzpn.spc.csl.services.bl.interfaces.IProcessService;
import ru.gzpn.spc.csl.services.bl.interfaces.IUIService;
import ru.gzpn.spc.csl.services.bl.interfaces.IUserSettingsService;
import ru.gzpn.spc.csl.ui.common.I18n;

public abstract class AbstarctContractCardComponent extends VerticalLayout implements I18n {

	public static final Logger logger = LogManager.getLogger(AbstarctContractCardComponent.class);
	private static final long serialVersionUID = 1L;

	// event actions
	public static final Action SAVECARD_ACTION = new Action("saveCardAction");
	public static final Action CALCULATE_ACTION = new Action("calculateAction");
	public static final Action CLOSE_ACTION = new Action("closeAction");
	
	private static final String I18N_CLOSEBUTTON_CAP = "ContractCardComponent.closebutton.cap";
	private static final String I18N_SETTINGSBUTTON_DESC = "ContractCardComponent.settingsbutton.cap";
	private static final String I18N_SAVEBUTTON_CAP = "ContractCardComponent.saveButton.cap";

	protected IUIService service;
	protected IUserSettingsService userSettingsService;
	protected MessageSource messageSource;
	protected IProcessService processService;
	
	protected String user;
	
	protected HorizontalLayout headFuturesLayout;
	protected VerticalLayout bodyLayout;
	protected HorizontalLayout footerLayout;

	protected Button initialMaxPriceButton;
	protected Button sendForApprovalButton;

	protected Button saveButton;

	protected Map<Action, Set<Listener>> listeners;
	protected Button settingsButton;
	protected Button closeButton;
	protected CssLayout calculationFieldsLayout;
	protected Long contractCardId;
	protected String taskId;
	
	public abstract VerticalLayout createBodyLayout();
	
	
	public AbstarctContractCardComponent(IUIService service, Long itemId, String taskId) {
		this.service = service;
		this.messageSource = service.getMessageSource();
		this.userSettingsService = service.getUserSettingsService();
		this.user = userSettingsService.getCurrentUser();
		this.contractCardId = itemId;
		this.processService = service.getProcessService();
		this.taskId = taskId;
		
		logger.debug("taskId= {} ", taskId);
		logger.debug("user= {} ", user);
		
		if (taskId != null &&
			processService.isAssigneeForTask(taskId, user)) {
			this.contractCardId = (Long) processService.getProcessVariableByTaskId(taskId, IProcessService.CONTRACT_ID);
			logger.debug("contractId = {} ", this.contractCardId);
		}
		
		setSpacing(false);
		setMargin(false);

		initEventActions();
		createHeadFutures();
		createBody();
		createFooter();
	}
	
	protected void initEventActions() {
		listeners = new HashMap<>();
		listeners.put(SAVECARD_ACTION, new HashSet<>());
		listeners.put(CLOSE_ACTION, new HashSet<>());
		listeners.put(CALCULATE_ACTION, new HashSet<>());
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
		//headFuturesLayout.addComponent(createSaveButton());
		
		layout.addComponent(createSettingsButton(), "top:5px; left:5px");
		layout.addComponent(headFuturesLayout, "top:5px; right:5px");
		
		verticalLayout.addComponent(layout);
		this.addComponent(verticalLayout);	
	}

	public Component createSettingsButton() {
		settingsButton = new Button();
		settingsButton.setIcon(VaadinIcons.SLIDERS);
		settingsButton.setDescription(getI18nText(I18N_SETTINGSBUTTON_DESC, messageSource));

		return settingsButton;
	}
	
	public void createBody() {
		refreshUiElements();
		bodyLayout.setSpacing(false);
		bodyLayout.setMargin(false);
		addComponent(bodyLayout);
	}
	
	public void refreshUiElements() {
		if (bodyLayout != null) {
			VerticalLayout layout = createBodyLayout();
			layout.setSpacing(false);
			layout.setMargin(false);
			replaceComponent(bodyLayout, layout);
			bodyLayout = layout;
		} else {
			bodyLayout = createBodyLayout();
			bodyLayout.setSpacing(false);
			bodyLayout.setMargin(false);
			addComponent(bodyLayout);
		}
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
		horizontalLayout.addComponent(createSaveButton());
		horizontalLayout.addComponent(createCloseButton());
		footerLayout.addComponent(horizontalLayout);
		
		return footerLayout;
	}

	public Component createSaveButton() {
		saveButton = new Button(getI18nText(I18N_SAVEBUTTON_CAP, messageSource));
		saveButton.addClickListener(event -> {
			
		});
		
		return saveButton;
	}
	
	public Component createCloseButton() {
		closeButton = new Button(getI18nText(I18N_CLOSEBUTTON_CAP, messageSource));
		closeButton.addClickListener(clickEvent -> {
			
		});
		
		return closeButton;
	}

	public void save() {
		onSaveAction();
	}

	public void close() {
		onClose();
	}
	
	public void onSaveAction() {
		handleAction(SAVECARD_ACTION);
	}
	
	public void onClose() {
		handleAction(CLOSE_ACTION);
	}
	
	public void handleAction(Action action) {
		for (Listener listener : listeners.get(action)) {
			listener.componentEvent(new Event(this));
		}
	}
	
	public void addOnSaveListener(Listener listener) {
		listeners.get(SAVECARD_ACTION).add(listener);
	}
	
	public void addOnCloseListener(Listener listener) {
		listeners.get(CLOSE_ACTION).add(listener);
	}
	
	@Override
	public boolean equals(Object obj) {
		return (obj != null && this == obj);
	}
}
