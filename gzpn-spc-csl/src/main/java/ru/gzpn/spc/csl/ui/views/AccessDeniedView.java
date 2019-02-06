package ru.gzpn.spc.csl.ui.views;

import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import ru.gzpn.spc.csl.ui.common.I18n;

@Component // No SpringView annotation because this view can not be navigated to
@UIScope
public class AccessDeniedView extends VerticalLayout implements View, I18n {
	public static final String I18N_ACCESSDENIEDVIEW_TEXT = "AccessDeniedView.text";
	
	ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
	
	public AccessDeniedView() {
		messageSource.setBasename("i18n/captions");
		setMargin(true);
		Label lbl = new Label(getI18nText(I18N_ACCESSDENIEDVIEW_TEXT, messageSource));
		lbl.addStyleName(ValoTheme.LABEL_FAILURE);
		lbl.setSizeUndefined();
		addComponent(lbl);
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
	}
}