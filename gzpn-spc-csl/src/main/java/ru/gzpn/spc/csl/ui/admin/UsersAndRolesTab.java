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
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class UsersAndRolesTab extends VerticalLayout {

	public static final Logger logger = LoggerFactory.getLogger(UsersAndRolesTab.class);

	private IdentityService identityService;

	private TextField searchUserGroup;
	private NativeSelect<String> selectUserGroup;
	private HorizontalLayout headerHorizont;
	private VerticalLayout resultPage;
	private Grid<UserTemplate> gridUser;
	private Grid<GroupTemplate> gridGroup;

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

	public void refreshData() {
//		List<String> list = identityService.createUserQuery().list().stream().map(m -> m.getId())
//				.collect(Collectors.toList());
//		ListDataProvider<String> dataProvider = new ListDataProvider<>(list);
//		searchUserGroup.setDataProvider(dataProvider);
	}

	private List<String> data(){
		List<String> data = new ArrayList<>();
		data.add("Users");
		data.add("Roles");
		return data;
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
			user.setEdit(buttonEditUser());
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
	
	private Grid<GroupTemplate> createGridGroup() {
		List<Group> groupList = identityService.createGroupQuery().list();
		List<GroupTemplate> groupTemplateList = new ArrayList<>();
		
		for(Group request : groupList) {
			GroupTemplate group = new GroupTemplate();
			group.setId(request.getId());
			group.setName(request.getName());
			group.setType(request.getType());
			group.setEdit(buttonEditGroup());
			group.setDelete(new Button());
			groupTemplateList.add((GroupTemplate) group);
		}
		
		ListDataProvider<GroupTemplate> dataProvider = DataProvider.ofCollection(groupTemplateList);
		Grid<GroupTemplate> grid = new Grid<>();
		grid.addColumn(GroupTemplate :: getId).setCaption("Id");
		grid.addColumn(GroupTemplate :: getName).setCaption("Name");
		grid.addColumn(GroupTemplate :: getType).setCaption("Type");
		grid.addComponentColumn(GroupTemplate :: getEdit).setCaption("Edit");
		grid.addComponentColumn(GroupTemplate :: getDelete).setCaption("Delete");
		grid.setDataProvider(dataProvider);
		return grid;
	}
	
	private NativeSelect<String> createSelectUserGroup() {
		NativeSelect<String> nativeSelect = new NativeSelect<>(null, data());
		nativeSelect.setEmptySelectionAllowed(false);
		nativeSelect.setSelectedItem(data().get(0));
		gridGroup.setVisible(false);
		nativeSelect.addSelectionListener(event ->{
			if (event.getSelectedItem().get().equals(data().get(0))) {
				gridUser.setVisible(true);
				gridGroup.setVisible(false);
			}
			else if(event.getSelectedItem().get().equals(data().get(1))) {
				gridUser.setVisible(false);
				gridGroup.setVisible(true);
			}
		});
		
		return nativeSelect;
	}
	
	private Button buttonCreate() {
		Button createButton = new Button("Create");
		return createButton;
	}
	
	private Button buttonEditUser() {
		Button editButton = new Button();
		editButton.addClickListener(event -> getUI().addWindow(formUser()));
		return editButton;
	}
	
	private Button buttonEditGroup() {
		Button editButton = new Button();
		editButton.addClickListener(event -> getUI().addWindow(formGroup()));
		return editButton;
	}
	
	private Window formUser() {
		final Window window = new Window("User");
		window.setWidth(300.0f, Unit.PIXELS);
        final FormLayout content = new FormLayout();
        content.setMargin(true);
        window.setContent(content);
		return window;
	}
	
	private Window formGroup() {
		final Window window = new Window("Group");
		window.setWidth(300.0f, Unit.PIXELS);
        final FormLayout content = new FormLayout();
        content.setMargin(true);
        window.setContent(content);
		return window;
	}
}
