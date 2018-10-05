package ru.gzpn.spc.csl.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Image;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import ru.gzpn.spc.csl.ui.views.AccessDeniedView;
import ru.gzpn.spc.csl.ui.views.AdminView;
import ru.gzpn.spc.csl.ui.views.ErrorView;

@SuppressWarnings("serial")
@SpringUI
@Push(transport = Transport.WEBSOCKET_XHR) // Websocket would bypass the filter chain, Websocket+XHR works
@Theme("GzpnSpcCsl")
public class MainUI extends UI {
	public static final Logger logger = LoggerFactory.getLogger(MainUI.class);

	@Autowired
	private SpringViewProvider viewProvider;

	@Autowired
	private ErrorView errorView;

	@Autowired
	MessageSource messageSource;

	private String taskId;

	private Panel viewContainer;
	private VerticalLayout mainLayout;
	private AbsoluteLayout head;
	private MenuBar menuBar;
	private Navigator navigator;

	@Override
	protected void init(VaadinRequest request) {
		getSession().addRequestHandler((vsession, vrequest, vresponse) -> {
			this.taskId = vrequest.getParameter("taskId");
			logger.debug("taskId = " + taskId);
			return false;
		});

		mainLayout = createMainLayout();
		viewContainer = new Panel();
		navigator = new Navigator(this, viewContainer);
		head = createHead();

		setContent(mainLayout);

		// View container
		viewContainer.setSizeFull();
		mainLayout.addComponents(head, viewContainer);
		mainLayout.setExpandRatio(viewContainer, 1.0f);

		getPage().setTitle("");

		navigator.addProvider(viewProvider);
		navigator.setErrorView(errorView);
		navigator.navigateTo(AdminView.ADMIN_VIEW);
		viewProvider.setAccessDeniedViewClass(AccessDeniedView.class);
	}

	private AbsoluteLayout createHead() {
		AbsoluteLayout head = new AbsoluteLayout();

		String exitString = messageSource.getMessage("main.ui.logout", null, VaadinSession.getCurrent().getLocale());
		Button exit = new Button(exitString);
		exit.setIcon(VaadinIcons.EXIT);
		exit.addClickListener(listener -> {
			getPage().setLocation("logout");
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
		result.setSizeFull();
		return result;
	}

	private MenuBar createMenu() {
		MenuBar menu = new MenuBar();
		final Command menuCommand = selectedItem -> Notification.show("Action " + selectedItem.getText(),
				Type.TRAY_NOTIFICATION);
		String itemCaption = messageSource.getMessage("menu.menuItem1", null, VaadinSession.getCurrent().getLocale());
		menu.addItem(itemCaption, VaadinIcons.FILE, menuCommand);
		return menu;
	}
}
