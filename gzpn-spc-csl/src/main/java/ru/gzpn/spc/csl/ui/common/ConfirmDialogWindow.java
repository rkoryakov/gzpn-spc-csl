package ru.gzpn.spc.csl.ui.common;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class ConfirmDialogWindow extends Window {

	private Button okButton;
	private Button closeButton;
	private VerticalLayout body;
	private String textInfoBox;
	private ClickListener listener;
	
	public ConfirmDialogWindow(String caption, String textInfoBox, String textOK, String textClose, ClickListener listener) {
		this.textInfoBox = textInfoBox;
		this.listener = listener;
		body = createVerticalLayout(textInfoBox, textOK, textClose, listener);
		this.setModal(true);
		this.setCaption(caption);
		this.setClosable(false);
		this.setResizable(false);
		this.setContent(body);	
	}
	
	public ConfirmDialogWindow(String textInfoBox, String textOK, String textClose, ClickListener listener) {
		this.textInfoBox = textInfoBox;
		this.listener = listener;
		body = createVerticalLayout(textInfoBox, textOK, textClose, listener);
		this.setModal(true);
		this.setClosable(false);
		this.setResizable(false);
		this.setContent(body);	
	}
	
	public ConfirmDialogWindow(String textInfoBox, ClickListener listener) {
		this.textInfoBox = textInfoBox;
		this.listener = listener;
		body = createVerticalLayout(textInfoBox, null, null, listener);
		this.setModal(true);
		this.setClosable(false);
		this.setResizable(false);
		this.setContent(body);	
	}

	private Button createCloseButton(String textCloseButton) {
		if(textCloseButton.isEmpty()) {
			textCloseButton = "Close";
		}
		Button closeBut = new Button(textCloseButton);
		closeBut.setStyleName(ValoTheme.BUTTON_DANGER);
		closeBut.addClickListener(event -> this.close());
		return closeBut;
	}

	private Button createOKButton(String textOKButton, ClickListener listener) {
		if(textOKButton.isEmpty()) {
			textOKButton = "OK";
		}
		Button okBut = new Button(textOKButton);
		okBut.setStyleName(ValoTheme.BUTTON_PRIMARY);
		okBut.addClickListener(listener);
		okBut.addClickListener(event -> this.close());
		return okBut;
	}

	private VerticalLayout createVerticalLayout(String textLabel,  String textOKButton, String textCloseButton, ClickListener listener) {
		VerticalLayout layout = new VerticalLayout();
		HorizontalLayout top = new HorizontalLayout();
		HorizontalLayout bottom = new HorizontalLayout();
		Label textInfo = new Label(textLabel);
		top.addComponent(textInfo);
		top.setSizeFull();
		okButton = createOKButton(textOKButton, listener);
		closeButton = createCloseButton(textCloseButton);
		bottom.addComponents(okButton, closeButton);
		
		layout.addComponents(top, bottom);
		return layout;
	}
	
}
