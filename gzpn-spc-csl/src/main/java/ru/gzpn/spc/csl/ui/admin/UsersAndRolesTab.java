package ru.gzpn.spc.csl.ui.admin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class UsersAndRolesTab extends VerticalLayout {

	public static final Logger logger = LoggerFactory.getLogger(UsersAndRolesTab.class);

	private IdentityService identityService;

	//private ComboBox<String> searchUserGroup;
	private TextField searchUserGroup;
	private NativeSelect<String> selectUserGroup;
	private HorizontalLayout headerHorizont;
	private VerticalLayout bodyLayout;
	private VerticalLayout resultPage;

	public UsersAndRolesTab(IdentityService identityService) {
		this.identityService = identityService;
		headerHorizont = new HorizontalLayout();
		bodyLayout = dataLayout();
		resultPage = new VerticalLayout();
		searchUserGroup = createSearchUserGroup();
		selectUserGroup = createSelectUserGroup();

		headerHorizont.addComponents(searchUserGroup, selectUserGroup, addButtonCreate());
		resultPage.addComponents(headerHorizont, bodyLayout);
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
		//searchUserGroup.setDataProvider(dataProvider);
	}

	private TextField createSearchUserGroup() {
		TextField filterTextField = new TextField();
		
		return filterTextField;
	}
	
	private NativeSelect<String> createSelectUserGroup() {
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
	
	private VerticalLayout dataLayout() {
		VerticalLayout vertL = new VerticalLayout();
		Collection<User> us = identityService.createUserQuery().list();
		ListDataProvider<User> dataProvider = DataProvider.ofCollection(us);
		Grid<User> grid = new Grid<>();
		grid.addColumn(User :: getId).setCaption("Id");
		grid.addColumn(User :: getFirstName).setCaption("First Name");
		grid.addColumn(User :: getLastName).setCaption("Last Name");
		grid.addColumn(User :: getEmail).setCaption("Email");
		
		grid.setDataProvider(dataProvider);
		vertL.addComponent(grid);
		return vertL;
	}

}
