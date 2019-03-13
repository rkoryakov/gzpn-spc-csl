package ru.gzpn.spc.csl.ui.views;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewBeforeLeaveEvent;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

import ru.gzpn.spc.csl.services.bl.interfaces.ILocalEstimatesApprovalService;
import ru.gzpn.spc.csl.ui.approval.LocalEstimatesApprovalComponent;

@SuppressWarnings("serial")
@SpringView(name = EstimatesApprovalView.NAME)
@UIScope
public class EstimatesApprovalView extends VerticalLayout implements View {
	public static final Logger logger = LogManager.getLogger(EstimatesApprovalView.class);
	
	public static final String NAME = "estimatesApprovalView";
	private static final String REQUEST_PARAM_TASKID = "taskId";
	@Autowired
	private ILocalEstimatesApprovalService service;
	
	
	public EstimatesApprovalView() {
		setMargin(false);
		setSpacing(false);
	}
	
	@PostConstruct
	void init() {
	}

	@Override
	public void enter(ViewChangeEvent event) {
		if (event.getParameters() != null) {
			String taskId = event.getParameterMap().get(REQUEST_PARAM_TASKID);
			logger.debug("[enter ] taskId = {}", taskId);
			LocalEstimatesApprovalComponent layout = new LocalEstimatesApprovalComponent(service, taskId);
			addComponent(layout);
		}
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

