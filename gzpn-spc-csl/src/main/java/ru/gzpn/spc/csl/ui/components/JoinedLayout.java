package ru.gzpn.spc.csl.ui.components;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;

public class JoinedLayout<E1 extends AbstractComponent, E2 extends AbstractComponent> extends CssLayout {
	
	public JoinedLayout() {
	 	setStyleName("v-component-group");
	}
	
	public JoinedLayout(E1 componentLeft, E2 componentRight) {
		checkTypes(componentLeft);
		checkTypes(componentRight);
		addComponents(componentLeft, componentRight);
	 	setStyleName("v-component-group");
	}

	private <T extends AbstractComponent> void checkTypes(T component) {
		if(!((component instanceof Button) || (component instanceof ComboBox) || (component instanceof NativeSelect) || (component instanceof TextField))) {
			throw new IllegalArgumentException(component.getClass().getName() + " isn't supported by JoinedLayout");
		}
	}
	
}
