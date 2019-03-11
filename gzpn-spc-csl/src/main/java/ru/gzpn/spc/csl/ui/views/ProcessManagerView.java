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
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

import ru.gzpn.spc.csl.services.bl.interfaces.IProcessManagerService;
import ru.gzpn.spc.csl.ui.common.I18n;
import ru.gzpn.spc.csl.ui.processmanager.AnalyticsComponent;
import ru.gzpn.spc.csl.ui.processmanager.BpmProcessesComponent;
import ru.gzpn.spc.csl.ui.processmanager.BpmnModelerComponent;

@SuppressWarnings("serial")
@SpringView(name = ProcessManagerView.NAME)
@UIScope
public class ProcessManagerView extends VerticalLayout implements View, I18n {
	public static final String NAME = "processManagerView";
	public static final Logger logger = LoggerFactory.getLogger(ProcessManagerView.class);

	public static final String I18N_MODELER_TAB_CAP = "ProcessManagerView.bpmnModelerTab.cap";
	private static final String I18N_PROCVIEWERSTAB_CAP = "ProcessManagerView.procViewerTab.cap";
	private static final String I18N_PLOTSTAB_CAP = "ProcessManagerView.plotsTab.cap";
	
	@Autowired
	private IProcessManagerService processManagerService;
	
	private TabSheet sheet = new TabSheet();

	private BpmnModelerComponent bpmnModelerComponent;

	private AnalyticsComponent analyticsComponent;
	private BpmProcessesComponent bpmProcessesComponent;
	
	
	public ProcessManagerView() {
		setMargin(true);
		setSpacing(true);
	}
	
	@PostConstruct
	void init() {
		String modelerTabCap = getI18nText(I18N_MODELER_TAB_CAP, processManagerService.getMessageSource());
		String processViewerTabCap = getI18nText(I18N_PROCVIEWERSTAB_CAP, processManagerService.getMessageSource());
		String plotsTabCap = getI18nText(I18N_PLOTSTAB_CAP, processManagerService.getMessageSource());
		
		sheet.addTab(createBpmnModelerTab(), modelerTabCap);
		sheet.addTab(createBpmnViewerTab(), processViewerTabCap);
		sheet.addTab(createPlotsTab(), plotsTabCap);
		
		addComponents(sheet);
	}

	private Component createPlotsTab() {
		analyticsComponent = new AnalyticsComponent(processManagerService);
		return analyticsComponent;
	}

	private Component createBpmnViewerTab() {
		bpmProcessesComponent = new BpmProcessesComponent(processManagerService);
		return bpmProcessesComponent;
	}
	
	private Component createBpmnModelerTab() {
		bpmnModelerComponent = new BpmnModelerComponent(processManagerService);
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


