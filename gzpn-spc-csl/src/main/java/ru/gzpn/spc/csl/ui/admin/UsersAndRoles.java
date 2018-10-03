package ru.gzpn.spc.csl.ui.admin;

import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.VerticalLayout;

import ru.gzpn.spc.csl.ui.components.JoinedLayout;

public class UsersAndRoles extends VerticalLayout {
	
	private HorizontalLayout headerHorizont = new HorizontalLayout();
	private HorizontalLayout bodyHorizontTop = new HorizontalLayout();
	private HorizontalLayout bodyHorizontBottom = new HorizontalLayout();
	private HorizontalLayout bottomHorizont = new HorizontalLayout();
	private VerticalLayout resultPage = new VerticalLayout();
	
	public UsersAndRoles() {
		headerHorizont.addComponents(addJoinCBNS(), addButtonCreate());
		bodyHorizontTop.addComponents(addJoinLL(), addJoinLR());
		bodyHorizontBottom.addComponent(new TwinColSelect<>());
		bottomHorizont.addComponents(addButtonSave(), addButtonCancel());
		resultPage.addComponents(headerHorizont,bodyHorizontTop,bodyHorizontBottom,bottomHorizont);
		addComponent(resultPage);
	}

	private JoinedLayout addJoinCBNS() {
		ComboBox<String> comboB = new ComboBox<>();
		NativeSelect<String> natS = new NativeSelect<>();
		JoinedLayout<ComboBox<String>, NativeSelect<String>> cbns = new JoinedLayout<>(comboB, natS);
		return cbns;
	}
	
	private JoinedLayout addJoinLR() {
		Button findButtonL = new Button("FindL");
		ComboBox<String> comboB = new ComboBox<>();
		JoinedLayout<Button, ComboBox<String>> leftF = new JoinedLayout<>(findButtonL, comboB);
		return leftF;
	}


	private JoinedLayout addJoinLL() {
		Button findButtonR = new Button("FindR");
		ComboBox<String> comboB = new ComboBox<>();
		JoinedLayout<Button, ComboBox<String>> rightF = new JoinedLayout(findButtonR, comboB);
		return rightF;
	}


	private Button addButtonCreate() {
		Button createButton = new Button("Create");
		createButton.addClickListener(event -> Notification.show("The button was clicked", Type.HUMANIZED_MESSAGE));
		return createButton;
	}
	
	private Button addButtonSave() {
		Button saveButton = new Button("Save");
		saveButton.addClickListener(event -> Notification.show("Save user", Type.TRAY_NOTIFICATION));
		return saveButton;
	}
	
	private Button addButtonCancel() {
		Button cancelButton = new Button("Cancel");
		cancelButton.addClickListener(event -> Notification.show("Cancel", Type.WARNING_MESSAGE));
		return cancelButton;
	}
}
