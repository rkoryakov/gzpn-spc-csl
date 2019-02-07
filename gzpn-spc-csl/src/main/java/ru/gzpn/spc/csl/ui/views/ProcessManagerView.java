package ru.gzpn.spc.csl.ui.views;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewBeforeLeaveEvent;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

import ru.gzpn.spc.csl.ui.common.I18n;
import ru.gzpn.spc.csl.ui.processmanager.AnalyticsComponent;
import ru.gzpn.spc.csl.ui.processmanager.BpmnModelerComponent;

@SuppressWarnings("serial")
@SpringView(name = ProcessManagerView.NAME)
@UIScope
public class ProcessManagerView extends VerticalLayout implements View, I18n {
	public static final String NAME = "processManagerView";
	
	public static final Logger logger = LoggerFactory.getLogger(ProcessManagerView.class);
	
	private TabSheet sheet = new TabSheet();

	private BpmnModelerComponent bpmnModelerComponent;

	private AnalyticsComponent analyticsComponent;
	
	
	public ProcessManagerView() {
		setMargin(true);
		setSpacing(true);
	}
	
	@PostConstruct
	void init() {
		sheet.addTab(createBpmnModelerTab(), getI18nText(key, messageSource)_);
		sheet.addTab(createPlotsTab());
		
		addComponents(sheet);
	}

	private Component createPlotsTab() {
		analyticsComponent = new AnalyticsComponent(null);
		return analyticsComponent;
	}

	private Component createBpmnModelerTab() {
		bpmnModelerComponent = new BpmnModelerComponent(null);
		return bpmnModelerComponent;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		View.super.enter(event);
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


