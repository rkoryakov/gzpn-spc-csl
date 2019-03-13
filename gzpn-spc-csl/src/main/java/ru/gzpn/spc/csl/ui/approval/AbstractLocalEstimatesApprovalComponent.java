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
import com.vaadin.ui.TextArea;
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

	public static final String I18N_APPROVEBUTTONN_CAP = "LocalEstimatesApprovalComponent.approveButton.cap";
	public static final String I18N_REJECTBUTTON_CAP = "LocalEstimatesApprovalComponent.rejectButton.cap";
	// event actions
	public static final Action APPROVE_ACTION = new Action("approveAction");
	public static final Action REJECT_ACTION = new Action("rejectAction");
	
	private static final String I18N_USERLAYOUTSETTINGS_DESC = "LocalEstimatesApprovalComponent.settingsbutton.cap";
	private static final String I18N_COMMENTFIELD_PLACEHOLDER = "LocalEstimatesApprovalComponent.commentField.placeholder";

	protected IUIService service;
	protected IUserSettigsService userSettingsService;
	protected MessageSource messageSource;
	protected String user;
	
	protected HorizontalLayout headFuturesLayout;
	protected VerticalLayout bodyLayout;
	protected HorizontalLayout footerLayout;

	protected Button approveButton;
	protected Button rejectButton;
	protected Button registerFilterSettingsButton;
	protected Button downloadWorksetButton;
	protected TextField registerFilterField;
	private Map<Action, Set<Listener>> listeners;
	private Button userLayoutSettingsButton;
	private TextArea commentField;

	
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
		refreshUiElements();
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
	
	public abstract VerticalLayout createBodyLayout();

	public void createFooter() {
		HorizontalLayout footer = createFooterLayout();
		addComponent(footer);
	}

	public HorizontalLayout createFooterLayout() {
		footerLayout = new HorizontalLayout();
		//HorizontalLayout footerContent = new HorizontalLayout();
		
		footerLayout.setSizeFull();
		footerLayout.setMargin(true);
		footerLayout.setSpacing(true);
		footerLayout.setDefaultComponentAlignment(Alignment.TOP_RIGHT);
		
		footerLayout.addComponent(createCommentField());
		footerLayout.setExpandRatio(commentField, 2);
		HorizontalLayout buttonsLayout = new HorizontalLayout();
		buttonsLayout.addComponents(createApproveButton(), createRejectButton());
		footerLayout.addComponent(buttonsLayout);
		footerLayout.setExpandRatio(commentField, 1);
		//footerLayout.addComponent(footerContent);
		
		return footerLayout;
	}

	public Component createCommentField() {
		commentField = new TextArea();
		commentField.setPlaceholder(getI18nText(I18N_COMMENTFIELD_PLACEHOLDER, messageSource));
		commentField.setSizeFull();
		commentField.setHeight(100, Unit.PIXELS);
		return commentField;
	}
	
	public Component createApproveButton() {
		approveButton = new Button(getI18nText(I18N_APPROVEBUTTONN_CAP, messageSource, I18N_APPROVEBUTTONN_CAP));
		approveButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		approveButton.addClickListener(listener -> {
			approve();
			refreshUiElements();
		});
		return approveButton;
	}

	public Component createRejectButton() {
		rejectButton = new Button(getI18nText(I18N_REJECTBUTTON_CAP, messageSource, I18N_REJECTBUTTON_CAP));
		rejectButton.setStyleName(ValoTheme.BUTTON_DANGER);
		rejectButton.addClickListener(listener -> {
			reject();
			refreshUiElements();
		});
		return rejectButton;
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
