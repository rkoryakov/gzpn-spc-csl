package ru.gzpn.spc.csl.ui.views;

import javax.annotation.PostConstruct;

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
	public static final String NAME = "summaryEstimateCardView";
	
	@Autowired
	private ISummaryEstimateCardService service;

	public SummaryEstimateCardView() {
		setMargin(false);
		setSpacing(false);
	}

	@PostConstruct
	void init() {
		SummaryEstimateCardComponent layout = new SummaryEstimateCardComponent(service);
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
