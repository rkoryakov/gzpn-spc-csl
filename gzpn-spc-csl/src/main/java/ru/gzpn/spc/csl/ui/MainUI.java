package ru.gzpn.spc.csl.ui;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.HorizontalLayout;
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
	SpringViewProvider viewProvider;

	@Autowired
	ErrorView errorView;

	private String taskId;
	private Locale locale;

	private Panel viewContainer;
	private VerticalLayout mainLayout;
	private HorizontalLayout head;
	private MenuBar menuBar;
	private Navigator navigator;

	@Override
	protected void init(VaadinRequest request) {

		getSession().addRequestHandler((vsession, vrequest, vresponse) -> {
			this.taskId = vrequest.getParameter("taskId");
			this.locale = vrequest.getLocale();
			return false;
		});

		// Main layout
		mainLayout = createMainLayout();
		setContent(mainLayout);

		// Menu
		menuBar = createMenu();
		head.addComponent(menuBar);

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

	private VerticalLayout createMainLayout() {
		VerticalLayout result = new VerticalLayout();
		result.setMargin(true);
		result.setSpacing(true);
		result.setSizeFull();
		return result;
	}

	private MenuBar createMenu() {
		MenuBar menu = new MenuBar();
		final Command menuCommand = selectedItem -> Notification.show("Action " + selectedItem.getText(), Type.TRAY_NOTIFICATION);
		// TODO: use ResourceBundleMessageSource to get localize messages
		// menu.addItem(caption, VaadinIcons.FILE, menuCommand);
		return menu;
	}
}
