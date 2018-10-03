package ru.gzpn.spc.csl.configurations;

import javax.servlet.ServletException;

import org.springframework.stereotype.Component;

import com.vaadin.server.CustomizedSystemMessages;
import com.vaadin.spring.server.SpringVaadinServlet;

import ru.gzpn.spc.csl.ui.MainUI;

@Component("vaadinServlet")
@SuppressWarnings("serial")
@com.vaadin.annotations.VaadinServletConfiguration(productionMode = false, ui = MainUI.class/*
																							 * , widgetset =
																							 * "ru.gzpn.spc.csl.WidgetSet"
																							 */)
public class VaadinServletConfiguration extends SpringVaadinServlet {

	@Override
	protected void servletInitialized() throws ServletException {
		super.servletInitialized();
		getService().setSystemMessagesProvider(systemMessagesProvider -> {
			CustomizedSystemMessages systemMessages = new CustomizedSystemMessages();
			// Don't show any messages, redirect immediately to the session expired URL
			systemMessages.setSessionExpiredNotificationEnabled(false);
			// Force a logout to also end the HTTP session and not only the Vaadin session
			systemMessages.setSessionExpiredURL("sessionExpired");
			// Don't show any message, reload the page instead
			systemMessages.setCommunicationErrorNotificationEnabled(false);

			return systemMessages;
		});
	}
}
