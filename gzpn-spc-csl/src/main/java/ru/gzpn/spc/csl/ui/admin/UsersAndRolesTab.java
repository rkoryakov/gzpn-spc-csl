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
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class UsersAndRolesTab extends VerticalLayout {

	private MessageSource messageSource;

	public static final Logger logger = LoggerFactory.getLogger(UsersAndRolesTab.class);
	private IdentityService identityService;

	private TextField searchUserGroup;
	private NativeSelect<String> selectUserGroup;
	private HorizontalLayout headerHorizont;
	private VerticalLayout resultPage;
	private Grid<UserTemplate> gridUser;
	private Grid<GroupTemplate> gridGroup;

	private ComboBox<EnumUserGroup> selectUserGroupC;

	public UsersAndRolesTab(IdentityService identityService, MessageSource messageSource) {
		this.identityService = identityService;
		this.messageSource = messageSource;
		headerHorizont = new HorizontalLayout();
		resultPage = new VerticalLayout();
		gridUser = createGridUser();
		gridGroup = createGridGroup();
		searchUserGroup = createSearchUserGroup();
		selectUserGroup = createSelectUserGroup();

		selectUserGroupC = createSelectUserGroupC();

		headerHorizont.addComponents(searchUserGroup, selectUserGroup, selectUserGroupC, buttonCreate());
		resultPage.addComponents(headerHorizont, gridUser, gridGroup);
		addComponent(resultPage);
	}

	private ComboBox<EnumUserGroup> createSelectUserGroupC() {
		ComboBox<EnumUserGroup> comboBox = new ComboBox<>();
		comboBox.setItems(EnumSet.allOf(EnumUserGroup.class));

		comboBox.setEmptySelectionAllowed(false);
		comboBox.setSelectedItem(EnumUserGroup.USERS);

		comboBox.setItemCaptionGenerator(item -> {
			String result = getI18nText("<user's key>"); // TODO: Add localized key

			if (comboBox.getSelectedItem().get() == EnumUserGroup.GROUPS) {
				result = getI18nText("<group's key>"); // TODO: Add localized key
			}

			return result;
		});

		return comboBox;
	}

	public void refreshData() {
//		List<String> list = identityService.createUserQuery().list().stream().map(m -> m.getId())
//				.collect(Collectors.toList());
//		ListDataProvider<String> dataProvider = new ListDataProvider<>(list);
//		searchUserGroup.setDataProvider(dataProvider);
	}

	private List<String> data() {
		List<String> data = new ArrayList<>();
		data.add("Users");
		data.add("Roles");
		return data;
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
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
			user.setEdit(buttonEditUser());
			user.setDelete(new Button());
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
			group.setDelete(new Button());
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

	private NativeSelect<String> createSelectUserGroup() {
		NativeSelect<String> nativeSelect = new NativeSelect<>(null, data());
		nativeSelect.setEmptySelectionAllowed(false);
		nativeSelect.setSelectedItem(data().get(0));
		gridGroup.setVisible(false);
		nativeSelect.addSelectionListener(event -> {
			if (event.getSelectedItem().get().equals(data().get(0))) {
				gridUser.setVisible(true);
				gridGroup.setVisible(false);
			} else if (event.getSelectedItem().get().equals(data().get(1))) {
				gridUser.setVisible(false);
				gridGroup.setVisible(true);
			}
		});

		return nativeSelect;
	}

	private Button buttonCreate() {
		String nameCreateButton = getI18nText("adminView.button.nameCreateButton");

		Button createButton = new Button(nameCreateButton);
		/*
		 * createButton.addClickListener(event -> {
		 * if(selectUserGroup.getSelectedItem().get().equals(data().get(0))) {
		 * getUI().addWindow(formUser()); } else
		 * if(selectUserGroup.getSelectedItem().get().equals(data().get(1))) {
		 * getUI().addWindow(formGroup(req)); } });
		 */
		return createButton;
	}

	private Button buttonEditUser() {
		Button editButton = new Button();
		editButton.addClickListener(event -> getUI().addWindow(formUser()));
		return editButton;
	}

	private Button buttonEditGroup(Group group) {
		Button editButton = new Button();
		editButton.addClickListener(event -> getUI().addWindow(formGroup(group)));
		return editButton;
	}

	private Window formUser() {

		String loginCaption = getI18nText("adminView.caption.login");
		String firstNameCaption = getI18nText("adminView.caption.firstName");
		String lastNameCaption = getI18nText("adminView.caption.lastName");
		String emailCaption = getI18nText("adminView.caption.email");
		String nameSaveButton = getI18nText("adminView.button.nameSaveButton");

		final Window window = new Window("User");
		window.setModal(true);
		window.setResizable(false);
		window.setWidth(300.0f, Unit.PIXELS);

		final FormLayout content = new FormLayout();
		content.setMargin(true);
		content.addStyleName("outlined");

		final TextField loginField = new TextField(loginCaption, "");
		loginField.setWidth(100.0f, Unit.PERCENTAGE);
		content.addComponent(loginField);

		final TextField firstNameField = new TextField(firstNameCaption, "");
		firstNameField.setWidth(100.0f, Unit.PERCENTAGE);
		content.addComponent(firstNameField);

		final TextField lastNameField = new TextField(lastNameCaption, "");
		lastNameField.setWidth(100.0f, Unit.PERCENTAGE);
		content.addComponent(lastNameField);

		final TextField emailField = new TextField(emailCaption, "");
		emailField.setWidth(100.0f, Unit.PERCENTAGE);
		content.addComponent(emailField);

		Button saveButton = new Button(nameSaveButton);
		content.addComponent(saveButton);

		window.setContent(content);
		return window;
	}

	private Window formGroup(Group gt) {

		String idCaption = getI18nText("adminView.caption.id");
		String nameCaption = getI18nText("adminView.caption.nameRoles");
		String typeCaption = getI18nText("adminView.caption.typeRoles");
		String nameSaveButton = getI18nText("adminView.button.nameSaveButton");

		final Window window = new Window("Group");
		window.setModal(true);
		window.setResizable(false);
		window.setWidth(300.0f, Unit.PIXELS);

		final FormLayout content = new FormLayout();
		content.setMargin(true);
		content.addStyleName("outlined");

		final TextField idField = new TextField(idCaption, gt.getId());
		idField.setWidth(100.0f, Unit.PERCENTAGE);
		content.addComponent(idField);

		final TextField nameField = new TextField(nameCaption, gt.getName());
		nameField.setWidth(100.0f, Unit.PERCENTAGE);
		content.addComponent(nameField);

		final TextField typeField = new TextField(typeCaption, gt.getType());
		typeField.setWidth(100.0f, Unit.PERCENTAGE);
		content.addComponent(typeField);

		Button saveButton = new Button(nameSaveButton);
		content.addComponent(saveButton);

		window.setContent(content);
		return window;
	}

	private String getI18nText(String key) {
		return messageSource.getMessage(key, null, VaadinSession.getCurrent().getLocale());
	}
}

enum EnumUserGroup {
	USERS, GROUPS;
}