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

import ru.gzpn.spc.csl.services.bl.interfaces.IEstimateRegisterService;
import ru.gzpn.spc.csl.ui.estimatereg.EstimateCalculationsRegistryComponent;

@SuppressWarnings("serial")
@SpringView(name = EstimateRegisterView.NAME)
@UIScope
public class EstimateRegisterView extends VerticalLayout implements View {
	public static final String NAME = "estimateRegisterView";
	public static final Logger logger = LoggerFactory.getLogger(EstimateRegisterView.class);
	
	@Autowired
	private IEstimateRegisterService estimateRegisterService;
	
	public EstimateRegisterView() {
		setMargin(false);
		setSpacing(false);
	}
	
	@PostConstruct
	void init() {
		EstimateCalculationsRegistryComponent layout = new EstimateCalculationsRegistryComponent(estimateRegisterService);
		addComponent(layout);
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
