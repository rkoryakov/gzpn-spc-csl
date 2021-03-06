package ru.gzpn.spc.csl.ui.views;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewBeforeLeaveEvent;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

import ru.gzpn.spc.csl.services.bl.interfaces.ICreateDocService;
import ru.gzpn.spc.csl.ui.createdoc.CreateDocComponent;

@SpringView(name = CreateDocView.NAME)
@UIScope
public class CreateDocView extends VerticalLayout implements View {

	private static final long serialVersionUID = 1L;
	public static final String NAME = "createDocView";
	public static final Logger logger = LoggerFactory.getLogger(CreateDocView.class);
	
	@Autowired
	private ICreateDocService createDocService;
	
	public CreateDocView() {
		setMargin(false);
		setSpacing(false);
	}

	@PostConstruct
	void init() {
		CreateDocComponent layout = new CreateDocComponent(createDocService);
		addComponent(layout);
	}

	@Override
	public void enter(ViewChangeEvent event) {

	}

	@Override
	public void beforeLeave(ViewBeforeLeaveEvent event) {
		View.super.beforeLeave(event);
	}

	@Override
	public Component getViewComponent() {
		return View.super.getViewComponent();
	}

}
