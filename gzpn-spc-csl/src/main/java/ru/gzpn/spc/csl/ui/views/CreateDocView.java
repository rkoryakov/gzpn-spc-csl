package ru.gzpn.spc.csl.ui.views;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewBeforeLeaveEvent;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

@SpringView(name = CreateDocView.NAME)
@UIScope
public class CreateDocView extends VerticalLayout implements View {
	public static final String NAME = "createDocView";
	public static final Logger logger = LoggerFactory.getLogger(CreateDocView.class);

	public CreateDocView() {
		logger.debug("[CreateDocView] is called");
	}

	@Override
	public void enter(ViewChangeEvent event) {
		logger.debug("[enter] is called");
		View.super.enter(event);
	}

	@Override
	public void beforeLeave(ViewBeforeLeaveEvent event) {
		logger.debug("[beforeLeave] is called");
		View.super.beforeLeave(event);
	}

	@Override
	public Component getViewComponent() {
		logger.debug("[getViewComponent] is called");
		return View.super.getViewComponent();
	}
}
