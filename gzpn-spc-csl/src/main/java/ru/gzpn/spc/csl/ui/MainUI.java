package ru.gzpn.spc.csl.ui;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
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

	private String taskId = null;

	private Panel viewContainer;
	private VerticalLayout verticalLayout;
	private Navigator navigator;

	@Override
	protected void init(VaadinRequest request) {

		getSession().addRequestHandler((vsession, vrequest, vresponse) -> {
			this.taskId = vrequest.getParameter("taskId");
			return false;
		});

		verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);
		verticalLayout.setSpacing(true);

		setContent(verticalLayout);

		viewContainer = new Panel();
		viewContainer.setSizeFull();
		verticalLayout.addComponent(viewContainer);
		verticalLayout.setExpandRatio(viewContainer, 1.0f);

		navigator = new Navigator(this, viewContainer);
		navigator.addProvider(viewProvider);
		navigator.setErrorView(errorView);
		viewProvider.setAccessDeniedViewClass(AccessDeniedView.class);
	}
}
