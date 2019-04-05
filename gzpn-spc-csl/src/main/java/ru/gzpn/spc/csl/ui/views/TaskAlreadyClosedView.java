package ru.gzpn.spc.csl.ui.views;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.support.ResourceBundleMessageSource;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import ru.gzpn.spc.csl.ui.common.I18n;

@SuppressWarnings("serial")
@SpringView(name = TaskAlreadyClosedView.NAME)
@UIScope
public class TaskAlreadyClosedView extends VerticalLayout implements View, I18n {
	public static final Logger logger = LogManager.getLogger(TaskAlreadyClosedView.class);
	public static final String NAME = "taskAlreadyClosedView";
	public static final String I18N_TASKALREADYCLOSED_TEXT = "TaskAlreadyClosedView.text";
	
	ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
	
	public TaskAlreadyClosedView() {
		setSizeFull();
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		String assignee = event.getParameterMap().get("assignee");
		String duedate = event.getParameterMap().get("duedate");
		
		messageSource.setBasename("i18n/captions");
		setMargin(true);
		Label lbl = new Label(getI18nText(I18N_TASKALREADYCLOSED_TEXT, new String[] {assignee, duedate}, messageSource));
		
		lbl.addStyleName(ValoTheme.LABEL_LARGE);
		lbl.setSizeUndefined();
		
		addComponent(lbl);
	}
}
