package ru.gzpn.spc.csl.ui.admin;

import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.VerticalLayout;

public class UsersAndRoles extends VerticalLayout {
	
	private HorizontalLayout headerHorizont = new HorizontalLayout();
	private HorizontalLayout bodyHorizontTop = new HorizontalLayout();
	private HorizontalLayout bodyHorizontBottom = new HorizontalLayout();
	private HorizontalLayout bottomHorizont = new HorizontalLayout();
	private VerticalLayout resultPage = new VerticalLayout();
	
	public UsersAndRoles() {
		headerHorizont.addComponents(new ComboBox<>(), new NativeSelect<>(), addButtonCreate());
		bodyHorizontTop.addComponents(new TextField(), new Button("Find"), new TextField(), new Button("Find"));
		bodyHorizontBottom.addComponent(new TwinColSelect<>());
		bottomHorizont.addComponents(addButtonSave(), addButtonCancel());
		resultPage.addComponents(headerHorizont,bodyHorizontTop,bodyHorizontBottom,bottomHorizont);
		addComponent(resultPage);
	}


	private Button addButtonCreate() {
		Button createButton = new Button("Create");
		createButton.addClickListener(event -> Notification.show("The button was clicked", Type.HUMANIZED_MESSAGE));
		return createButton;
	}
	
	private Button addButtonSave() {
		Button saveButton = new Button("Save");
		saveButton.addClickListener(event -> Notification.show("Save user", Type.HUMANIZED_MESSAGE));
		return saveButton;
	}
	
	private Button addButtonCancel() {
		Button cancelButton = new Button("Cancel");
		cancelButton.addClickListener(event -> Notification.show("Cancel", Type.HUMANIZED_MESSAGE));
		return cancelButton;
	}
}
