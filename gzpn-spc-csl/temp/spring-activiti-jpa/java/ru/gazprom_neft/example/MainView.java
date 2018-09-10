package ru.gazprom_neft.example;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringUI(path = "/vaadin")
@Push(transport = Transport.WEBSOCKET_XHR) // Websocket would bypass the filter chain, Websocket+XHR works
@Theme(ValoTheme.THEME_NAME) // Looks nicer
public class MainView extends UI {

	private VerticalLayout verticalLayout;

	@Override
	protected void init(VaadinRequest request) {

		verticalLayout = new VerticalLayout();
		Button btn = new Button("test");

		btn.setResponsive(true);
		btn.setIcon(VaadinIcons.PLUS);
		btn.setStyleName(ValoTheme.BUTTON_PRIMARY);
		TextField text = new TextField();
		text.setPlaceholder("Enter some text");
		btn.addClickListener(e -> {
			verticalLayout.addComponent(new TextField());
		});
		verticalLayout.addComponents(btn, text);

		setContent(verticalLayout);
	}
}