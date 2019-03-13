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

import ru.gzpn.spc.csl.services.bl.interfaces.IContractRegisterService;
import ru.gzpn.spc.csl.ui.contractreg.ContractRegistryComponent;

@SpringView(name = ContractRegisterView.NAME)
@UIScope
public class ContractRegisterView extends VerticalLayout implements View {
	public static final String NAME = "contractRegisterView";
	public static final Logger logger = LoggerFactory.getLogger(ContractRegisterView.class);
	
	@Autowired
	private IContractRegisterService contractRegisterService;
	
	public ContractRegisterView() {
		setMargin(false);
		setSpacing(false);
		logger.debug("[ContractRegisterView] is called");
	}

	@PostConstruct
	void init() {
		ContractRegistryComponent layout = new ContractRegistryComponent(contractRegisterService);
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
