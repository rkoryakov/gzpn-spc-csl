package ru.gzpn.spc.csl.ui.views;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.VerticalLayout;

import ru.gzpn.spc.csl.services.bl.interfaces.ILocalEstimatesApprovalService;
import ru.gzpn.spc.csl.ui.approval.LocalEstimatesApprovalComponent;

@SuppressWarnings("serial")
@SpringView(name = EstimatesApprovalView.NAME)
@UIScope
public class EstimatesApprovalView extends VerticalLayout implements View {
	public static final String NAME = "estimatesApprovalView";
	private static final String REQUEST_PARAM_TASKID = "taskId";
	@Autowired
	private ILocalEstimatesApprovalService service;
	
	
	@PostConstruct
	void init() {
		
	}

	@Override
	public void enter(ViewChangeEvent event) {
		if (event.getParameters() != null) {
			String taskId = event.getParameterMap().get(REQUEST_PARAM_TASKID);
			
			LocalEstimatesApprovalComponent layout = new LocalEstimatesApprovalComponent(service,  taskId);
			addComponent(layout);
		}
	}
	
}

