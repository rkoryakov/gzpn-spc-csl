package ru.gzpn.spc.csl.ui.admin;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class UsersAndRolesTab extends VerticalLayout {

	public static final Logger logger = LoggerFactory.getLogger(UsersAndRolesTab.class);
	private IdentityService identityService;
	private MessageSource messageSource;
	private TextField searchUserGroup;
	private HorizontalLayout headerHorizont;
	private VerticalLayout resultPage;
	private Grid<UserTemplate> gridUser;
	private Grid<GroupTemplate> gridGroup;
	private ComboBox<EnumUserGroup> selectUserGroup;
	//private JoinedLayout<TextField , ComboBox<EnumUserGroup>> joi;

	public UsersAndRolesTab(IdentityService identityService, MessageSource messageSource) {
		this.identityService = identityService;
		this.messageSource = messageSource;
		headerHorizont = new HorizontalLayout();
		resultPage = new VerticalLayout();
		gridUser = createGridUser();
		gridGroup = createGridGroup();
		searchUserGroup = createSearchUserGroup();
		selectUserGroup = createSelectUserGroup();
		//joi = new JoinedLayout<>(searchUserGroup, selectUserGroup);
				
		headerHorizont.addComponents(searchUserGroup, selectUserGroup, buttonCreate());
		resultPage.addComponents(headerHorizont, gridUser, gridGroup);
		addComponent(resultPage);
	}
	
	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	private ComboBox<EnumUserGroup> createSelectUserGroup() {
		ComboBox<EnumUserGroup> comboBox = new ComboBox<>();
		comboBox.setItems(EnumSet.allOf(EnumUserGroup.class));
		comboBox.setTextInputAllowed(false);
		comboBox.setEmptySelectionAllowed(false);
		comboBox.setSelectedItem(EnumUserGroup.USERS);
		gridGroup.setVisible(false);
		
		comboBox.setItemCaptionGenerator(item -> {
			String result = getI18nText("adminView.caption.usersKey");
			if (item == EnumUserGroup.GROUPS) {
				result = getI18nText("adminView.caption.groupsKey");
			}
			return result;
		});
		
		comboBox.addSelectionListener(event -> {
			if (event.getSelectedItem().get().equals(EnumUserGroup.USERS)) {
				gridUser.setVisible(true);
				gridGroup.setVisible(false);
			} else if (event.getSelectedItem().get().equals(EnumUserGroup.GROUPS)) {
				gridUser.setVisible(false);
				gridGroup.setVisible(true);
			}
		});
		return comboBox;
	}

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

		for (User request : userList) {
			UserTemplate user = new UserTemplate();
			user.setId(request.getId());
			user.setFirstName(request.getFirstName());
			user.setLastName(request.getLastName());
			user.setEmail(request.getEmail());
			user.setPassword(request.getPassword());
			user.setEdit(buttonEditUser(request));
			user.setDelete(buttonDeleteUser(request));
			userTemplateList.add((UserTemplate) user);
		}

		String editCaption = getI18nText("adminView.caption.edit");
		String deleteCaption = getI18nText("adminView.caption.delete");
		String loginCaption = getI18nText("adminView.caption.login");
		String firstNameCaption = getI18nText("adminView.caption.firstName");
		String lastNameCaption = getI18nText("adminView.caption.lastName");
		String emailCaption = getI18nText("adminView.caption.email");

		ListDataProvider<UserTemplate> dataProvider = DataProvider.ofCollection(userTemplateList);
		Grid<UserTemplate> grid = new Grid<>();
		grid.addColumn(UserTemplate::getId).setCaption(loginCaption);
		grid.addColumn(UserTemplate::getFirstName).setCaption(firstNameCaption);
		grid.addColumn(UserTemplate::getLastName).setCaption(lastNameCaption);
		grid.addColumn(UserTemplate::getEmail).setCaption(emailCaption);
		grid.addComponentColumn(UserTemplate::getEdit).setCaption(editCaption);
		grid.addComponentColumn(UserTemplate::getDelete).setCaption(deleteCaption);
		grid.setDataProvider(dataProvider);
		grid.setColumnReorderingAllowed(true);
		return grid;
	}

	private Grid<GroupTemplate> createGridGroup() {
		List<Group> groupList = identityService.createGroupQuery().list();
		List<GroupTemplate> groupTemplateList = new ArrayList<>();

		for (Group request : groupList) {
			GroupTemplate group = new GroupTemplate();
			group.setId(request.getId());
			group.setName(request.getName());
			group.setType(request.getType());
			group.setEdit(buttonEditGroup(request));
			group.setDelete(buttonDeleteGroup(request));
			groupTemplateList.add((GroupTemplate) group);
		}

		String editCaption = getI18nText("adminView.caption.edit");
		String deleteCaption = getI18nText("adminView.caption.delete");
		String idCaption = getI18nText("adminView.caption.id");
		String nameCaption = getI18nText("adminView.caption.nameRoles");
		String typeCaption = getI18nText("adminView.caption.typeRoles");

		ListDataProvider<GroupTemplate> dataProvider = DataProvider.ofCollection(groupTemplateList);
		Grid<GroupTemplate> grid = new Grid<>();
		grid.addColumn(GroupTemplate::getId).setCaption(idCaption);
		grid.addColumn(GroupTemplate::getName).setCaption(nameCaption);
		grid.addColumn(GroupTemplate::getType).setCaption(typeCaption);
		grid.addComponentColumn(GroupTemplate::getEdit).setCaption(editCaption);
		grid.addComponentColumn(GroupTemplate::getDelete).setCaption(deleteCaption);
		grid.setDataProvider(dataProvider);
		return grid;
	}

	private Button buttonCreate() {
		String nameCreateButton = getI18nText("adminView.button.nameCreateButton");

		Button createButton = new Button(nameCreateButton);
		  createButton.addClickListener(event -> {
		  if(selectUserGroup.getSelectedItem().get().equals(EnumUserGroup.USERS)) {
		  getUI().addWindow(formUser(null)); } else
		  if(selectUserGroup.getSelectedItem().get().equals(EnumUserGroup.GROUPS)) {
		  getUI().addWindow(formGroup(null)); } });
		 
		return createButton;
	}

	private Button buttonEditUser(User user) {
		Button editButton = new Button();
		editButton.addClickListener(event -> getUI().addWindow(formUser(user)));
		return editButton;
	}
	
	private Button buttonDeleteUser(User request) {
		Button deleteButton = new Button();
		Button okButton = new Button("OK");
		deleteButton.addClickListener(event -> {
			Window win = new Window();
			win.setModal(true);
			win.setResizable(false);
			win.setWidth(200.0f, Unit.PIXELS);
			VerticalLayout l = new VerticalLayout();
			l.addComponent(okButton);
			okButton.setSizeFull();
			win.setContent(l);
			getUI().addWindow(win);
		});
		okButton.addClickListener(event -> identityService.deleteUser(request.getId()));
		return deleteButton;
	}
	
	private Button buttonEditGroup(Group group) {
		Button editButton = new Button();
		editButton.addClickListener(event -> getUI().addWindow(formGroup(group)));
		return editButton;
	}
	
	private Button buttonDeleteGroup(Group request) {
		Button deleteButton = new Button();
		deleteButton.addClickListener(event -> identityService.deleteGroup(request.getId()));
		return deleteButton;
	}
	
	private Window formUser(User currentUser) {
		
		String userWindowCaption = getI18nText("adminView.caption.userKey");
		String loginCaption = getI18nText("adminView.caption.login");
		String firstNameCaption = getI18nText("adminView.caption.firstName");
		String lastNameCaption = getI18nText("adminView.caption.lastName");
		String emailCaption = getI18nText("adminView.caption.email");
		String newPasswordCaption = getI18nText("adminView.caption.newPasswordCaption");
		String nameSaveButton = getI18nText("adminView.button.nameSaveButton");
		
		final Window window = new Window(userWindowCaption);
		window.setModal(true);
		window.setResizable(false);
		window.setWidth(300.0f, Unit.PIXELS);
		final FormLayout content = new FormLayout();
		content.setMargin(true);
		content.addStyleName("outlined");
		
		final TextField loginField;
		final TextField firstNameField;
		final TextField lastNameField;
		final TextField emailField;
		final TextField newPasswordField;
		Button saveButton = new Button(nameSaveButton);
		
		if (currentUser != null) {
			loginField = new TextField(loginCaption, currentUser.getId());
			loginField.setReadOnly(true);
			firstNameField = new TextField(firstNameCaption, currentUser.getFirstName() == null ? "" : currentUser.getFirstName());
			lastNameField = new TextField(lastNameCaption, currentUser.getLastName() == null ? "" : currentUser.getLastName());
			emailField = new TextField(emailCaption, currentUser.getEmail() == null ? "" : currentUser.getEmail());
			newPasswordField = new TextField(newPasswordCaption, "");
		}
			
		else {
			loginField = new TextField(loginCaption, "");
			firstNameField = new TextField(firstNameCaption, "");
			lastNameField = new TextField(lastNameCaption, "");
			emailField = new TextField(emailCaption, "");
			newPasswordField = new TextField(newPasswordCaption, "");
		}
		
		loginField.setWidth(90.0f, Unit.PERCENTAGE);
		firstNameField.setWidth(90.0f, Unit.PERCENTAGE);
		lastNameField.setWidth(90.0f, Unit.PERCENTAGE);
		emailField.setWidth(90.0f, Unit.PERCENTAGE);
		newPasswordField.setWidth(90.0f, Unit.PERCENTAGE);
		
		content.addComponent(loginField);
		content.addComponent(firstNameField);
		content.addComponent(lastNameField);
		content.addComponent(emailField);
		content.addComponent(newPasswordField);
		content.addComponent(saveButton);
		
		window.setContent(content);
		
		saveButton.addClickListener(event -> {
			User user = identityService.createUserQuery().userId(loginField.getValue()).singleResult();
			if (user == null) {
				user = identityService.newUser(loginField.getValue());
				user.setFirstName(firstNameField.getValue());
				user.setLastName(lastNameField.getValue());
				user.setEmail(emailField.getValue());
				user.setPassword(newPasswordField.getValue());
				identityService.saveUser(user);
			}
			else if (user.getId() != null) {
				user.setFirstName(firstNameField.getValue());
				user.setLastName(lastNameField.getValue());
				user.setEmail(emailField.getValue());
				user.setPassword(newPasswordField.getValue());
				identityService.saveUser(user);
			}
		});
		return window;
	}

	private Window formGroup(Group currentGroup) {
		
		String groupWindowCaption = getI18nText("adminView.caption.groupKey");
		String idCaption = getI18nText("adminView.caption.id");
		String nameCaption = getI18nText("adminView.caption.nameRoles");
		String typeCaption = getI18nText("adminView.caption.typeRoles");
		String nameSaveButton = getI18nText("adminView.button.nameSaveButton");

		final Window window = new Window(groupWindowCaption);
		window.setModal(true);
		window.setResizable(false);
		window.setWidth(300.0f, Unit.PIXELS);
		final FormLayout content = new FormLayout();
		content.setMargin(true);
		content.addStyleName("outlined");
		
		final TextField idField;
		final TextField nameField;
		final TextField typeField;
		Button saveButton = new Button(nameSaveButton);
		
		if (currentGroup != null) {
			idField = new TextField(idCaption, currentGroup.getId());
			idField.setReadOnly(true);
			nameField = new TextField(nameCaption, currentGroup.getName());
			typeField = new TextField(typeCaption, currentGroup.getType());
		}
		
		else {
			idField = new TextField(idCaption, "");
			nameField = new TextField(nameCaption, "");
			typeField = new TextField(typeCaption, "");
		}
		idField.setWidth(90.0f, Unit.PERCENTAGE);
		nameField.setWidth(90.0f, Unit.PERCENTAGE);
		typeField.setWidth(90.0f, Unit.PERCENTAGE);
		
		content.addComponent(idField);
		content.addComponent(nameField);
		content.addComponent(typeField);
		content.addComponent(saveButton);
		
		window.setContent(content);
		
		saveButton.addClickListener(event -> {
			Group group = identityService.createGroupQuery().groupId(idField.getValue()).singleResult();
			if (group == null) {
				group = identityService.newGroup(idField.getValue());
				group.setName(nameField.getValue());
				group.setType(typeField.getValue());
				identityService.saveGroup(group);
			}
			else if (group.getId() != null) {
				group.setName(nameField.getValue());
				group.setType(typeField.getValue());
				identityService.saveGroup(group);
			}
		});
		
		return window;
	}

	private String getI18nText(String key) {
		return messageSource.getMessage(key, null, VaadinSession.getCurrent().getLocale());
	}
}

enum EnumUserGroup {
	USERS, GROUPS;
}