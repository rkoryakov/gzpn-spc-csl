package ru.gzpn.spc.csl.ui.views;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.VerticalLayout;

@SpringView(name = "adminView")
public class AdminView extends VerticalLayout implements View{
		
	public AdminView() {
		setMargin(true);
		setSpacing(true);
	}
	
}
