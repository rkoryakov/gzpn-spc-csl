package ru.gzpn.spc.csl.ui;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
@SpringUI
@Push(transport = Transport.WEBSOCKET_XHR) // Websocket would bypass the filter chain, Websocket+XHR works
@Theme(ValoTheme.THEME_NAME) // Looks nicer
public class MainView extends UI {

	@Override
	protected void init(VaadinRequest request) {
		// TODO Auto-generated method stub

	}
}
