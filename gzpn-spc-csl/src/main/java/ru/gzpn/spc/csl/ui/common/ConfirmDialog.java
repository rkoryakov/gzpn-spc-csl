package ru.gzpn.spc.csl.ui.common;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class ConfirmDialog extends Window {

	private Window boxDialog = new Window();
	private Button okButton = new Button("OK");
	private Button closeButton = new Button("Отмена");
	private VerticalLayout body;
	private String text;
	
	public ConfirmDialog(String text) {
		this.text = text;
		body = createVerticalLayout();
		boxDialog.setModal(true);
		boxDialog.setClosable(false);
		boxDialog.setResizable(false);
		boxDialog.setWidth(300.0f, Unit.PIXELS);
		body.addComponent(okButton);
		body.addComponent(closeButton);
		okButton.setSizeFull();
		closeButton.setSizeFull();
		boxDialog.setContent(body);
		getUI().addWindow(boxDialog);
	}

	private VerticalLayout createVerticalLayout() {
		VerticalLayout layout = new VerticalLayout();
		HorizontalLayout top = new HorizontalLayout();
		
		HorizontalLayout bottom = new HorizontalLayout();
		
		layout.addComponents(top, bottom);
		return layout;
	}
	
	
}
