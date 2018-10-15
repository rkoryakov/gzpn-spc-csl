package ru.gzpn.spc.csl.ui.common;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class ConfirmDialog extends Window {

	private Button okButton;
	private Button closeButton;
	private VerticalLayout body;
	//private String textInfoBox;
	//private ClickListener listener;
	
	public ConfirmDialog(String textInfoBox, String textOK, String textClose, ClickListener listener) {
		//this.textInfoBox = textInfoBox;
		//this.listener = listener;
		body = createVerticalLayout(textInfoBox, textOK, textClose, listener);
		this.setModal(true);
		this.setClosable(false);
		this.setResizable(false);
		this.setWidth(400.0f, Unit.PIXELS);
		this.setContent(body);	
	}

	private Button createCloseButton(String textCloseButton) {
		Button closeBut = new Button(textCloseButton);
		closeBut.setSizeFull();
		closeBut.addClickListener(event -> this.close());
		return closeBut;
	}

	private Button createOKButton(String textOKButton, ClickListener listener) {
		Button okBut = new Button(textOKButton);
		okBut.setSizeFull();
		okBut.addClickListener(listener);
		okBut.addClickListener(event -> this.close());
		return okBut;
	}

	private VerticalLayout createVerticalLayout(String textLabel,  String textOKButton, String textCloseButton, ClickListener listener) {
		VerticalLayout layout = new VerticalLayout();
		HorizontalLayout top = new HorizontalLayout();
		HorizontalLayout bottom = new HorizontalLayout();
		Label textInfo = new Label(textLabel);
		top.setSizeFull();
		top.addComponent(textInfo);
		okButton = createOKButton(textOKButton, listener);
		closeButton = createCloseButton(textCloseButton);
		bottom.addComponents(okButton, closeButton);
		bottom.setSizeFull();
		layout.addComponents(top, bottom);
		return layout;
	}
	
}
