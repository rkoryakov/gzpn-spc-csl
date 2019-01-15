package ru.gzpn.spc.csl.ui.views;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@Component // No SpringView annotation because this view can not be navigated to
@UIScope
public class ErrorView extends VerticalLayout implements View {
	public static final Logger logger = LoggerFactory.getLogger(ErrorView.class);
	
	private Label errorLabel;
	

	public ErrorView() {
		//setMargin(true);
		errorLabel = new Label();
		errorLabel.addStyleName(ValoTheme.LABEL_FAILURE);
		errorLabel.setSizeUndefined();
		addComponent(errorLabel);
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		
		errorLabel.setValue(String.format("No such view: %s", event.getViewName()));
	}
}