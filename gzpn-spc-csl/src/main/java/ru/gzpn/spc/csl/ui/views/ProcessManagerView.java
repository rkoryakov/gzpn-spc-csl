package ru.gzpn.spc.csl.ui.views;

import java.time.Duration;
import java.time.Instant;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewBeforeLeaveEvent;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

import ru.gzpn.spc.csl.ui.js.Flot;

@SuppressWarnings("serial")
@SpringView(name = ProcessManagerView.NAME)
@UIScope
public class ProcessManagerView extends VerticalLayout implements View {
	public static final String NAME = "processManagerView";
	
	public static final Logger logger = LoggerFactory.getLogger(ProcessManagerView.class);
	private Flot flot = new Flot();
	
	public ProcessManagerView() {
		setMargin(true);
		setSpacing(true);
		logger.debug("[ProcessManagerView] is called");
	}
	
	@PostConstruct
	void init() {
		Button button = new Button("test refresh");
		button.addClickListener(clickEvent -> {
			removeComponent(flot);
			flot = new Flot();
			for (int i = 0; i < 30; i ++) {
				flot.addSeries(Instant.now().minus(Duration.ofDays(i)).getEpochSecond()*1000, Math.random()*10);
			}
			addComponentAsFirst(flot);
		});
		
		flot = new Flot();
		for (int i = 0; i < 30; i ++) {
			flot.addSeries(Instant.now().minus(Duration.ofDays(i)).getEpochSecond()*1000, Math.random()*10);
		}
		
		addComponents(flot, button);
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


