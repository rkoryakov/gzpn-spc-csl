package ru.gzpn.spc.csl.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Link;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import ru.gzpn.spc.csl.ui.views.AccessDeniedView;
import ru.gzpn.spc.csl.ui.views.ErrorView;

@SuppressWarnings("serial")
@SpringUI
@Push(transport = Transport.WEBSOCKET_XHR) // Websocket would bypass the filter chain, Websocket+XHR works
@Theme("GzpnSpcCsl")
public class MainUI extends UI {

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
			return false;
		});

		// Main layout & head
		head = createHead();
		mainLayout = createMainLayout();

		setContent(mainLayout);

		// View container
		viewContainer = new Panel();
		viewContainer.setSizeFull();
		mainLayout.addComponents(head, viewContainer);
		mainLayout.setExpandRatio(viewContainer, 1.0f);

		getPage().setTitle("");

		navigator = new Navigator(this, viewContainer);
		navigator.addProvider(viewProvider);
		navigator.setErrorView(errorView);
		viewProvider.setAccessDeniedViewClass(AccessDeniedView.class);
	}

	private AbsoluteLayout createHead() {
		AbsoluteLayout head = new AbsoluteLayout();

		String labelString = messageSource.getMessage("main.ui.logout", null, VaadinSession.getCurrent().getLocale());
		Link logoutLink = new Link(labelString, new ExternalResource("logout"));
		logoutLink.setIcon(VaadinIcons.EXIT);

		head.setStyleName("gzpn-head");
		head.setHeight(80.0f, Unit.PIXELS);
		head.setWidth(100.0f, Unit.PERCENTAGE);

		ThemeResource resource = new ThemeResource("img/Logo.png");
		Image image = new Image("", resource);
		head.addComponent(image, "top:10px; left:15px");
		head.addComponent(logoutLink, "top:25px; right:25px");

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
