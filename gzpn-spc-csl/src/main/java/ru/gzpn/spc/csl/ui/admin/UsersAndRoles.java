package ru.gzpn.spc.csl.ui.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.activiti.engine.IdentityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.provider.ListDataProvider;
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

	public static final Logger logger = LoggerFactory.getLogger(UsersAndRoles.class);

	IdentityService identityService;

	private HorizontalLayout headerHorizont = new HorizontalLayout();
	private HorizontalLayout bodyHorizontTop = new HorizontalLayout();
	private HorizontalLayout bodyHorizontBottom = new HorizontalLayout();
	private HorizontalLayout bottomHorizont = new HorizontalLayout();
	private VerticalLayout resultPage = new VerticalLayout();
	private ComboBox<String> searchUserGroup = createSearchUserGroup();

	public UsersAndRoles() {
	}

	public UsersAndRoles(IdentityService identityService) {
		this.identityService = identityService;
		headerHorizont.addComponents(searchUserGroup, addNativeSelect(), addButtonCreate());
		bodyHorizontTop.addComponents(addJoinLL(), addJoinLR());
		bodyHorizontBottom.addComponent(addTwinColSelect());
		bottomHorizont.addComponents(addButtonSave(), addButtonCancel());
		resultPage.addComponents(headerHorizont, bodyHorizontTop, bodyHorizontBottom, bottomHorizont);
		refreshData();
		addComponent(resultPage);
	}

	/**
	 * init or refresh all the componet's data
	 */
	public void refreshData() {
		List<String> list = identityService.createUserQuery().list().stream().map(m -> m.getId())
				.collect(Collectors.toList());
		ListDataProvider<String> dataProvider = new ListDataProvider<>(list);
		searchUserGroup.setDataProvider(dataProvider);
	}

	private ComboBox<String> createSearchUserGroup() {
		ComboBox<String> comboBox = new ComboBox<>();

		return comboBox;
	}

	private NativeSelect<String> addNativeSelect() {
		List<String> data = new ArrayList<>();
		data.add("Users");
		data.add("Roles");
		NativeSelect<String> natS = new NativeSelect<>(null, data);
		natS.setEmptySelectionAllowed(false);
		natS.setSelectedItem(data.get(0));
		return natS;
	}

	private Button addButtonCreate() {
		Button createButton = new Button("Create");
		createButton.addClickListener(event -> Notification.show("The button was clicked", Type.HUMANIZED_MESSAGE));
		return createButton;
	}

	private JoinedLayout<Button, ComboBox<String>> addJoinLL() {
		Button findButtonL = new Button("FindL");
		ComboBox<String> comboB = new ComboBox<>();
		JoinedLayout<Button, ComboBox<String>> leftF = new JoinedLayout(comboB, findButtonL);
		return leftF;
	}

	private JoinedLayout<Button, ComboBox<String>> addJoinLR() {
		Button findButtonR = new Button("FindR");
		ComboBox<String> comboB = new ComboBox<>();
		JoinedLayout<Button, ComboBox<String>> rightF = new JoinedLayout(comboB, findButtonR);
		return rightF;
	}

	private TwinColSelect<String> addTwinColSelect() {
		List<String> dataD = new ArrayList<>();
		TwinColSelect<String> twinCol = new TwinColSelect<>(null, dataD);
		return twinCol;
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
