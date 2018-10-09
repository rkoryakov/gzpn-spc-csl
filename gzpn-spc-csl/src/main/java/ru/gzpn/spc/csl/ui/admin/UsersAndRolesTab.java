package ru.gzpn.spc.csl.ui.admin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
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

	private TextField searchUserGroup;
	private NativeSelect<String> selectUserGroup;
	private HorizontalLayout headerHorizont;
	private VerticalLayout resultPage;
	private Grid<UserTemplate> gridUser;
	private Grid<Group> gridGroup;

	public UsersAndRolesTab(IdentityService identityService) {
		this.identityService = identityService;
		headerHorizont = new HorizontalLayout();
		resultPage = new VerticalLayout();
		gridUser = createGridUser();
		gridGroup = createGridGroup();
		searchUserGroup = createSearchUserGroup();
		selectUserGroup = createSelectUserGroup();

		headerHorizont.addComponents(searchUserGroup, selectUserGroup, buttonCreate());
		resultPage.addComponents(headerHorizont, gridUser, gridGroup);
		addComponent(resultPage);
	}

	/**
	 * init or refresh all the componet's data
	 */
	public void refreshData() {
//		List<String> list = identityService.createUserQuery().list().stream().map(m -> m.getId())
//				.collect(Collectors.toList());
//		ListDataProvider<String> dataProvider = new ListDataProvider<>(list);
//		searchUserGroup.setDataProvider(dataProvider);
	}

	private TextField createSearchUserGroup() {
		TextField filterTextField = new TextField();
		
		return filterTextField;
	}
	
	private Grid<UserTemplate> createGridUser() {
		List<User> userList = identityService.createUserQuery().list();
		List<UserTemplate> userTemplateList = new ArrayList<>();
		
		for(User request : userList) {
			UserTemplate user = new UserTemplate();
			user.setId(request.getId());
			user.setFirstName(request.getFirstName());
			user.setLastName(request.getLastName());
			user.setEmail(request.getEmail());
			user.setPassword(request.getPassword());
			user.setEdit(new Button());
			user.setDelete(new Button());
			userTemplateList.add((UserTemplate) user);
		}
		
		ListDataProvider<UserTemplate> dataProvider = DataProvider.ofCollection(userTemplateList);
		Grid<UserTemplate> grid = new Grid<>();
		grid.addColumn(UserTemplate :: getId).setCaption("Login");
		grid.addColumn(UserTemplate :: getFirstName).setCaption("First Name");
		grid.addColumn(UserTemplate :: getLastName).setCaption("Last Name");
		grid.addColumn(UserTemplate :: getEmail).setCaption("Email");
		grid.addComponentColumn(UserTemplate :: getEdit).setCaption("Edit");
		grid.addComponentColumn(UserTemplate :: getDelete).setCaption("Delete");
		grid.setDataProvider(dataProvider);
		grid.setColumnReorderingAllowed(true);
		return grid;
	}
	
	private Grid<Group> createGridGroup() {
		Collection<Group> gr = identityService.createGroupQuery().list();
		ListDataProvider<Group> dataProvider = DataProvider.ofCollection(gr);
		Grid<Group> grid = new Grid<>();
		grid.addColumn(Group :: getId).setCaption("Id");
		grid.addColumn(Group :: getName).setCaption("Name");
		grid.addColumn(Group :: getType).setCaption("Type");
		grid.setDataProvider(dataProvider);
		gridGroup = grid;
		return grid;
	}
	
	private NativeSelect<String> createSelectUserGroup() {
		List<String> data = new ArrayList<>();
		data.add("Users");
		data.add("Roles");
		NativeSelect<String> nativeSelect = new NativeSelect<>(null, data);
		nativeSelect.setEmptySelectionAllowed(false);
		nativeSelect.setSelectedItem(data.get(0));
		gridGroup.setVisible(false);
		nativeSelect.addSelectionListener(event ->{
			if (event.getSelectedItem().get().equals(data.get(0))) {
				gridUser.setVisible(true);
				gridGroup.setVisible(false);
			}
			else if(event.getSelectedItem().get().equals(data.get(1))) {
				gridUser.setVisible(false);
				gridGroup.setVisible(true);
			}
		});
		return nativeSelect;
	}

	private Button buttonCreate() {
		Button createButton = new Button("Create");
		createButton.addClickListener(event -> Notification.show("The button was clicked", Type.HUMANIZED_MESSAGE));
		return createButton;
	}
}
