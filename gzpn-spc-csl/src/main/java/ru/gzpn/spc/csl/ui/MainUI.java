package ru.gzpn.spc.csl.ui;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
@SpringUI
@Push(transport = Transport.WEBSOCKET_XHR) // Websocket would bypass the filter chain, Websocket+XHR works
@Theme("GzpnSpcCsl")
public class MainUI extends UI {
	private String taskId = null;

	@Override
	protected void init(VaadinRequest request) {

		getSession().addRequestHandler((vsession, vrequest, vresponse) -> {
			this.taskId = vrequest.getParameter("taskId");
			return false;
		});

		setContent(new Button("HTTP GET PARAM taskId = " + taskId));
	}
}
