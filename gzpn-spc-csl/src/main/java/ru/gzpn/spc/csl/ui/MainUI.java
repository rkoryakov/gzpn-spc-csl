package ru.gzpn.spc.csl.ui;

import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Image;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import ru.gzpn.spc.csl.model.enums.Role;
import ru.gzpn.spc.csl.services.bpm.IUserTaskNavigator;
import ru.gzpn.spc.csl.ui.views.AccessDeniedView;
import ru.gzpn.spc.csl.ui.views.AdminView;
import ru.gzpn.spc.csl.ui.views.ContractRegisterView;
import ru.gzpn.spc.csl.ui.views.CreateDocView;
import ru.gzpn.spc.csl.ui.views.ErrorView;
import ru.gzpn.spc.csl.ui.views.EstimateRegisterView;
import ru.gzpn.spc.csl.ui.views.ProcessManagerView;
import ru.gzpn.spc.csl.ui.views.SummaryEstimateCardView;

@SuppressWarnings("serial")
@SpringUI
@Push(transport = Transport.WEBSOCKET_XHR) // Websocket would bypass the filter chain, Websocket+XHR works
@Theme("GzpnSpcCsl")
public class MainUI extends UI {
	public static final Logger logger = LoggerFactory.getLogger(MainUI.class);

	public static final String REQUEST_PARAM_TASKID = "taskID";
	public static final String REQUEST_PARAM_VIEWID = "viewID";
	
	@Autowired
	private SpringViewProvider viewProvider;
	@Autowired
	private ErrorView errorView;
	@Autowired
	MessageSource messageSource;

	@Autowired
	IUserTaskNavigator taskNavigator;
	
	private Panel viewContainer;
	private VerticalLayout mainLayout;
	private AbsoluteLayout head;
	private MenuBar menuBar;
	private Navigator navigator;

	private String viewId;
	private String taskId;
	private String ssrId;
	
	@Override
	protected void init(VaadinRequest request) {
		viewId = VaadinService.getCurrentRequest().getParameter(REQUEST_PARAM_VIEWID);
		taskId = VaadinService.getCurrentRequest().getParameter(REQUEST_PARAM_TASKID);
		ssrId = VaadinService.getCurrentRequest().getParameter("id");
		
		head = createHead();
		mainLayout = createMainLayout();
		viewContainer = new Panel();
		navigator = new Navigator(this, viewContainer);
		
		menuBar = createMenu();
		setContent(mainLayout);

		// View container
		viewContainer.setSizeFull();
		mainLayout.addComponents(head, menuBar, viewContainer);
		mainLayout.setExpandRatio(viewContainer, 1.0f);

		getPage().setTitle("");

		navigator.addProvider(viewProvider);
		navigator.setErrorView(errorView);
		viewProvider.setAccessDeniedViewClass(AccessDeniedView.class);
		
		navigate();
	}
	
	private void navigate() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Set<String> authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.toSet());
		
		if (taskId != null) {
			taskNavigator.navigate(navigator, taskId);
			
		} else if (viewId != null) {
			if (SummaryEstimateCardView.NAME.equals(viewId)) {
				navigator.navigateTo(viewId + "/id=" + ssrId);
			}
		} else {
			if (authorities.contains(Role.CREATOR_ROLE.toString())) {
				navigator.navigateTo(CreateDocView.NAME);
			} else if (authorities.contains(Role.ADMIN_ROLE.toString())) {
				navigator.navigateTo(AdminView.NAME);
			} else if (authorities.contains(Role.APPROVER_NTC_ROLE.toString())) {
				navigator.navigateTo("");
			} else if (authorities.contains(Role.CONTRACT_ES_ROLE.toString())) {
				navigator.navigateTo("");
			} else if (authorities.contains(Role.CONTRACT_EX_ROLE.toString())) {
				navigator.navigateTo("");
			} else if (authorities.contains(Role.EXPERT_ES_ROLE.toString())) {
				navigator.navigateTo(EstimateRegisterView.NAME);
			} else if (authorities.contains(Role.USER_ROLE.toString())) {
				navigator.navigateTo(CreateDocView.NAME);
			}
		}
	}
	
	private AbsoluteLayout createHead() {
		AbsoluteLayout head = new AbsoluteLayout();

		String exitString = getI18nText("main.ui.logout");
		Button exit = new Button(exitString);
		exit.setIcon(VaadinIcons.EXIT);
		exit.addClickListener(listener -> {
			getPage().setLocation("logout");
			VaadinSession.getCurrent().close();
		});
		exit.addStyleName(ValoTheme.BUTTON_LINK);

		head.setStyleName("gzpn-head");
		head.setHeight(80.0f, Unit.PIXELS);
		head.setWidth(100.0f, Unit.PERCENTAGE);

		ThemeResource resource = new ThemeResource("img/Logo.png");
		Image image = new Image("", resource);
		head.addComponent(image, "top:5px; left:15px");
		head.addComponent(exit, "top:20px; right:15px");

		return head;
	}

	private VerticalLayout createMainLayout() {
		VerticalLayout result = new VerticalLayout();
		result.setMargin(true);
		result.setSpacing(true);
		//result.setSizeFull();
		return result;
	}

	private MenuBar createMenu() {
		final MenuBar menu = new MenuBar();
		final Command cCreateDocument = item -> this.navigator.navigateTo(CreateDocView.NAME);
		final Command cContractRegister = item -> this.navigator.navigateTo(ContractRegisterView.NAME);
		final Command cEstimateRegister = item -> this.navigator.navigateTo(EstimateRegisterView.NAME);
		final Command cProcessManager = item -> this.navigator.navigateTo(ProcessManagerView.NAME);
		menu.setResponsive(true);
		String sCreateDocument = getI18nText("main.ui.menu.createDocument");
		String sContractRegister = getI18nText("main.ui.menu.cotractRegister");
		String sEstimateRegister = getI18nText("main.ui.menu.estimateRegister");
		String sProcessManager = getI18nText("main.ui.menu.processManager");
		
		MenuItem createDocumentItem = menu.addItem(sCreateDocument, VaadinIcons.FILE_ADD, cCreateDocument);
		MenuItem contractRegisterItem = menu.addItem(sContractRegister, VaadinIcons.FILE_TREE_SMALL, cContractRegister);
		MenuItem estimateRegisterItem = menu.addItem(sEstimateRegister, VaadinIcons.CALC_BOOK, cEstimateRegister);
		MenuItem processManagerItem = menu.addItem(sProcessManager, VaadinIcons.AUTOMATION, cProcessManager);
		
		createDocumentItem.setCheckable(true);
		contractRegisterItem.setCheckable(true);
		estimateRegisterItem.setCheckable(true);		
		processManagerItem.setCheckable(true);
		
		this.navigator.addViewChangeListener(listener -> {
			switch (listener.getViewName()) {
			case CreateDocView.NAME:
				createDocumentItem.setChecked(true);
				processManagerItem.setChecked(false);
				contractRegisterItem.setChecked(false);
				estimateRegisterItem.setChecked(false);
				break;
			case ContractRegisterView.NAME:
				contractRegisterItem.setChecked(true);
				processManagerItem.setChecked(false);
				createDocumentItem.setChecked(false);
				estimateRegisterItem.setChecked(false);
				break;
			case EstimateRegisterView.NAME:
				estimateRegisterItem.setChecked(true);
				processManagerItem.setChecked(false);
				contractRegisterItem.setChecked(false);
				createDocumentItem.setChecked(false);
				break;
			case ProcessManagerView.NAME:
				processManagerItem.setChecked(true);
				estimateRegisterItem.setChecked(false);
				contractRegisterItem.setChecked(false);
				createDocumentItem.setChecked(false);
				break;
			default:
			}
			return true;
		});
		return menu;
	}

	private String getI18nText(String key) {
		return messageSource.getMessage(key, null, VaadinSession.getCurrent().getLocale());
	}
}
