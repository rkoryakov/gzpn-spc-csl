package ru.gzpn.spc.csl.ui.admin;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.components.grid.SingleSelectionModel;

import ru.gzpn.spc.csl.ui.common.ConfirmDialog;

//TODO: use another name. This "UsersAndRolesTab" name is incorrect as VerticalLayout type is not a Tab 
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
	private DataProvider<UserTemplate, String> userDataProvider = createUserDataProvider();
	private DataProvider<GroupTemplate, String> groupDataProvider = createGroupDataProvider();
	private ConfigurableFilterDataProvider<UserTemplate, Void, String> userFilter;
	private ConfigurableFilterDataProvider<GroupTemplate, Void, String> groupFilter;
	private Button createButton;
	private MarginInfo marginForHeader;
	private UserInfoTabSheet infoUser;
	
	private HorizontalSplitPanel panel;
	//TODO: use another name. The same reason 
	public UsersAndRolesTab(IdentityService identityService, MessageSource messageSource) {
		this.identityService = identityService;
		this.messageSource = messageSource;
		panel = new HorizontalSplitPanel();
		createButton = createButtonCreate();
		
		UIContainer container = new UIContainer();
		container.setCreateUserAndRolesButton(createButton);
		
		infoUser = new UserInfoTabSheet(identityService, messageSource, userDataProvider, groupDataProvider, container);
		infoUser.setUserAndRolesTab(this);
		
		headerHorizont = new HorizontalLayout();
		resultPage = new VerticalLayout();
		gridUser = createGridUser();
		gridGroup = createGridGroup();
		searchUserGroup = createSearchUserGroup();
		selectUserGroup = createSelectUserGroup();
		
		marginForHeader = new MarginInfo(true, false, false, false);
		
		headerHorizont.addComponents(searchUserGroup, selectUserGroup, createButton);
		headerHorizont.setMargin(marginForHeader);
		resultPage.addComponents(headerHorizont, gridUser, gridGroup);
		resultPage.setMargin(false);
		
		panel.setSplitPosition(70, Unit.PERCENTAGE);
		panel.setMaxSplitPosition(70, Unit.PERCENTAGE);
		panel.setMinSplitPosition(30, Unit.PERCENTAGE);
		panel.setFirstComponent(resultPage);
		panel.setSecondComponent(infoUser);
		panel.setHeight(100, Unit.PERCENTAGE);
		setHeight(100, Unit.PERCENTAGE);
		addComponent(panel);
		
	}

	public DataProvider<UserTemplate, String> createUserDataProvider(){
		return DataProvider.fromFilteringCallbacks(query -> {

			List<User> userList = identityService.createUserQuery().list()
					.stream()
						.filter(user -> {
							return user.getId().startsWith(query.getFilter().orElse(""));
						}).collect(Collectors.toList());
			
			return userList.stream().map(m -> {
				UserTemplate user = new UserTemplate();
				user.setId(m.getId());
				user.setFirstName(m.getFirstName());
				user.setLastName(m.getLastName());
				user.setEmail(m.getEmail());
				user.setPassword(m.getPassword());
				return user;
			});
		}, query -> {
			return identityService.createUserQuery().list()
					.stream()
						.filter(user -> {
							return user.getId().startsWith(query.getFilter().orElse(""));
						}).collect(Collectors.toList()).size();
		});
	}
	
	
	private DataProvider<GroupTemplate, String> createGroupDataProvider() {
		return DataProvider.fromFilteringCallbacks(query -> {

			List<Group> groupList = identityService.createGroupQuery().list()
					.stream()
						.filter(group -> {
							return group.getId().startsWith(query.getFilter().orElse(""));
						}).collect(Collectors.toList());
			
			return groupList.stream().map(m -> {
				GroupTemplate group = new GroupTemplate();
				group.setId(m.getId());
				group.setName(m.getName());
				group.setType(m.getType());
				group.setEdit(buttonEditGroup(m));
				group.setDelete(buttonDeleteGroup(m));
				return group;
			});
		}, query -> {
			// TODO: correct the expression considering the Sonar Lint advice
			int count = identityService.createGroupQuery().list().stream().filter(group -> {
						return group.getId().startsWith(query.getFilter().orElse(""));
					}).collect(Collectors.toList()).size();
			return count;
		});
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
			searchUserGroup.setValue("");
		});
		return comboBox;
	}

	private TextField createSearchUserGroup() {
		TextField filterTextField = new TextField();

		filterTextField.addValueChangeListener(event -> {
			if (selectUserGroup.getSelectedItem().get().equals(EnumUserGroup.USERS)) {
				userFilter.setFilter(event.getValue());
				userFilter.refreshAll();
			} else if (selectUserGroup.getSelectedItem().get().equals(EnumUserGroup.GROUPS)) {
				groupFilter.setFilter(event.getValue());
				groupFilter.refreshAll();
			}
		});
		return filterTextField;
	}

	private Grid<UserTemplate> createGridUser() {
		String loginCaption = getI18nText("adminView.caption.login");
		String firstNameCaption = getI18nText("adminView.caption.firstName");
		String lastNameCaption = getI18nText("adminView.caption.lastName");
		String emailCaption = getI18nText("adminView.caption.email");

		userFilter = userDataProvider.withConfigurableFilter();
		Grid<UserTemplate> grid = new Grid<>();
		grid.addColumn(UserTemplate::getId).setCaption(loginCaption);
		grid.addColumn(UserTemplate::getFirstName).setCaption(firstNameCaption);
		grid.addColumn(UserTemplate::getLastName).setCaption(lastNameCaption);
		grid.addColumn(UserTemplate::getEmail).setCaption(emailCaption);
		grid.setDataProvider(userFilter);
		grid.setColumnReorderingAllowed(true);
		grid.setSizeFull();
		grid.setHeightMode(HeightMode.ROW);
		grid.setHeightByRows(14);
		infoUser.setVisible(false);	
		
		SingleSelectionModel<UserTemplate> singleSelect = (SingleSelectionModel<UserTemplate>) grid.getSelectionModel();
		singleSelect.addSingleSelectionListener(event -> {
			singleSelect.getSelectedItem().ifPresent(item -> {
				infoUser.getUserInfoTab().setData(item);
				infoUser.getUserAddGroupTab().setUser(item);
				infoUser.setVisible(true);
			});
		});
		
		return grid;
	}

	private Grid<GroupTemplate> createGridGroup() {
		String editCaption = getI18nText("adminView.caption.edit");
		String deleteCaption = getI18nText("adminView.caption.delete");
		String idCaption = getI18nText("adminView.caption.id");
		String nameCaption = getI18nText("adminView.caption.nameRoles");
		String typeCaption = getI18nText("adminView.caption.typeRoles");

		groupFilter = groupDataProvider.withConfigurableFilter();
		Grid<GroupTemplate> grid = new Grid<>();
		grid.setSizeFull();
		grid.addColumn(GroupTemplate::getId).setCaption(idCaption);
		grid.addColumn(GroupTemplate::getName).setCaption(nameCaption);
		grid.addColumn(GroupTemplate::getType).setCaption(typeCaption);
		grid.addComponentColumn(GroupTemplate::getEdit).setCaption(editCaption).setWidth(105.0);
		grid.addComponentColumn(GroupTemplate::getDelete).setCaption(deleteCaption).setWidth(105.0);
		grid.setDataProvider(groupFilter);
		return grid;
	}

	private Button createButtonCreate() {
		String nameCreateButton = getI18nText("adminView.button.nameCreateButton");

		Button createButton = new Button(nameCreateButton);
		createButton.addClickListener(event -> {
			if (selectUserGroup.getSelectedItem().get().equals(EnumUserGroup.USERS)) {
				infoUser.getUserInfoTab().setData(null);
				infoUser.setVisible(true);

			} else if (selectUserGroup.getSelectedItem().get().equals(EnumUserGroup.GROUPS)) {
				getUI().addWindow(formGroup(null));
			}
		});
		
		return createButton;
	}

	public Button getCreateButton() {
		return createButton;
	}

	public void setCreateButton(Button createButton) {
		this.createButton = createButton;
	}

	private Button buttonEditGroup(Group group) {
		Button editButton = new Button();
		editButton.addClickListener(event -> getUI().addWindow(formGroup(group)));
		return editButton;
	}

	private Button buttonDeleteGroup(Group group) {
		Button deleteButton = new Button();
		String groupWindowCaption = getI18nText("adminView.caption.groupKey");
		String notificationDeleted = getI18nText("adminView.notification.group.deleted");
		String textInfo = getI18nText("adminView.ConfirmDialog.deleteGroup.info");
		String textOKButton = getI18nText("adminView.ConfirmDialog.deleteUser.ok");
		String textCloseButton = getI18nText("adminView.ConfirmDialog.deleteUser.close");
		ClickListener okDeleteClick = event -> {
			identityService.deleteGroup(group.getId());
			groupDataProvider.refreshAll();
			Notification.show(groupWindowCaption.concat(" ").concat(group.getId()).concat(notificationDeleted), Type.WARNING_MESSAGE);
		};
		deleteButton.addClickListener(event -> {
			ConfirmDialog box = new ConfirmDialog(textInfo, textOKButton, textCloseButton, okDeleteClick);
			getUI().addWindow(box);
		});
		
		return deleteButton;
	}

	private Window formGroup(Group currentGroup) {

		String groupWindowCaption = getI18nText("adminView.caption.groupKey");
		String idCaption = getI18nText("adminView.caption.id");
		String nameCaption = getI18nText("adminView.caption.nameRoles");
		String typeCaption = getI18nText("adminView.caption.typeRoles");
		String nameSaveButton = getI18nText("adminView.button.nameSaveButton");
		String notificationChanged = getI18nText("adminView.notification.group.change");
		String notificationCreated = getI18nText("adminView.notification.group.created");

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
				Notification.show(groupWindowCaption.concat(" ").concat(group.getId()).concat(notificationCreated), Type.TRAY_NOTIFICATION);
				groupDataProvider.refreshAll();
				window.close();
				
			} else if (group.getId() != null) {
				group.setName(nameField.getValue());
				group.setType(typeField.getValue());
				identityService.saveGroup(group);
				Notification.show(groupWindowCaption.concat(" ").concat(group.getId()).concat(notificationChanged), Type.TRAY_NOTIFICATION);
				groupDataProvider.refreshAll();
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