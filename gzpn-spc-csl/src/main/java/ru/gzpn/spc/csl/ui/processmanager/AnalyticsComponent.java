package ru.gzpn.spc.csl.ui.processmanager;

import java.time.Duration;
import java.time.Instant;

import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

import ru.gzpn.spc.csl.services.bl.interfaces.IUIService;
import ru.gzpn.spc.csl.ui.js.flot.Flot;

@SuppressWarnings("serial")
public class AnalyticsComponent extends AbstractAnalyticsComponent {

	private Flot plot;

	public AnalyticsComponent(IUIService service) {
		super(service);
	}

	@Override
	public VerticalLayout createBodyLayout() {
		VerticalLayout body = new VerticalLayout();
		body.setSpacing(false);
		body.setMargin(false);
		
		body.addComponent(createLastMonthPlot());

		return body;
	}
	
	public Component createLastMonthPlot() {
//		button.addClickListener(clickEvent -> {
//			removeComponent(flot);
//			flot = new Flot();
//			for (int i = 0; i < 30; i ++) {
//				flot.addSeries(Instant.now().minus(Duration.ofDays(i)).getEpochSecond()*1000, Math.random()*10);
//			}
//			addComponentAsFirst(flot);
//		});
//		
		plot = new Flot();
		for (int i = 0; i < 30; i ++) {
			plot.addSeries(Instant.now().minus(Duration.ofDays(i)).getEpochSecond()*1000, Math.random()*10);
		}
		return plot;
	}
}
