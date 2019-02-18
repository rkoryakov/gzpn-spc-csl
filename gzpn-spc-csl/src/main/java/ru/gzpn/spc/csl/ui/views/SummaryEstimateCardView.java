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

import ru.gzpn.spc.csl.services.bl.interfaces.ISummaryEstimateCardService;
import ru.gzpn.spc.csl.ui.sumestimate.SummaryEstimateCardComponent;

@SuppressWarnings("serial")
@SpringView(name = SummaryEstimateCardView.NAME)
@UIScope
public class SummaryEstimateCardView extends VerticalLayout implements View {
	public static final Logger logger = LogManager.getLogger(SummaryEstimateCardView.class);
	public static final String NAME = "summaryEstimateCardView";

	private static final String REQUEST_PARAM_SSRID = "ssrId";
	private static final String REQUEST_PARAM_TASKID = "taskId";
	
	@Autowired
	private ISummaryEstimateCardService service;

	public SummaryEstimateCardView() {
		setMargin(false);
		setSpacing(false);
	}

	@PostConstruct
	void init() {
		
	}

	@Override
	public void enter(ViewChangeEvent event) {
		if (event.getParameters() != null) {
			String ssrId = event.getParameterMap().get(REQUEST_PARAM_SSRID);
			String taskId = event.getParameterMap().get(REQUEST_PARAM_TASKID);
			logger.debug(event.getParameters());
			logger.debug(event.getParameterMap());
			
			Long ssrid = null;
			if (ssrId != null) {
				ssrid = Long.parseLong(ssrId);
			}
			
			SummaryEstimateCardComponent layout = new SummaryEstimateCardComponent(service, ssrid, taskId);
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
