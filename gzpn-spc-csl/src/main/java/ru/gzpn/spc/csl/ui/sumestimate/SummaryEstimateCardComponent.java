package ru.gzpn.spc.csl.ui.sumestimate;

import com.vaadin.ui.VerticalLayout;

import ru.gzpn.spc.csl.services.bl.interfaces.ISummaryEstimateCardService;
import ru.gzpn.spc.csl.ui.common.I18n;

@SuppressWarnings("serial")
public class SummaryEstimateCardComponent extends AbstarctSummaryEstimateCardComponent implements I18n {

	public SummaryEstimateCardComponent(ISummaryEstimateCardService service) {
		super(service);
	}

	@Override
	public VerticalLayout createBodyLayout() {
		VerticalLayout body = new VerticalLayout();
		body.setSizeFull();
		
		
		return body;
	}

	@Override
	public void refreshUiElements() {
	}

}
