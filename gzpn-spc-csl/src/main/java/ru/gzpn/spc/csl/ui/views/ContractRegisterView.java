package ru.gzpn.spc.csl.ui.views;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.VerticalLayout;

@SpringView(name = ContractRegisterView.NAME)
@UIScope
public class ContractRegisterView extends VerticalLayout implements View {
	public static final String NAME = "contractRegisterView";

	ContractRegisterView() {
	}
}
