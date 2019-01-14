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

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
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

import ru.gzpn.spc.csl.ui.common.ConfirmDialogWindow;

public class UsersAndRolesVerticalLayout extends VerticalLayout {

	public static final Logger logger = LoggerFactory.getLogger(UsersAndRolesVerticalLayout.class);
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
	private static final String I18N_CAPTION_USERS = "adminView.caption.usersKey";
	private static final String I18N_CAPTION_GROUPS = "adminView.caption.groupsKey";
	
	private static final String I18N_CAPTION_COLUMN_LOGIN = "adminView.caption.login";
	private static final String I18N_CAPTION_COLUMN_FIRSTNAME = "adminView.caption.firstName";
	private static final String I18N_CAPTION_COLUMN_LASTNAME = "adminView.caption.lastName";
	private static final String I18N_CAPTION_COLUMN_EMAIL = "adminView.caption.email";
	private static final String I18N_CAPTION_COLUMN_ID = "adminView.caption.id";
	private static final String I18N_CAPTION_COLUMN_NAMEROLES = "adminView.caption.nameRoles";
	private static final String I18N_CAPTION_COLUMN_TYPEROLES = "adminView.caption.typeRoles";
	private static final String I18N_CAPTION_COLUMN_EDIT = "adminView.caption.edit";
	private static final String I18N_CAPTION_COLUMN_DELETE = "adminView.caption.delete";
	
	public UsersAndRolesVerticalLayout(IdentityService identityService, MessageSource messageSource) {
		this.identityService = identityService;
		this.messageSource = messageSource;
		panel = new HorizontalSplitPanel();
		createButton = createButtonCreate();
		UIContainer container = new UIContainer();
		container.setCreateUserAndRolesButton(createButton);
		infoUser = new UserInfoTabSheet(identityService, messageSource, userDataProvider, container);
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
						.filter(user -> user.getId().startsWith(query.getFilter().orElse(""))).collect(Collectors.toList());
			return userList.stream().map(m -> {
				UserTemplate user = new UserTemplate();
				user.setId(m.getId());
				user.setFirstName(m.getFirstName());
				user.setLastName(m.getLastName());
				user.setEmail(m.getEmail());
				user.setPassword(m.getPassword());
				return user;
			});
		}, query -> identityService.createUserQuery().list()
					.stream()
						.filter(user -> user.getId().startsWith(query.getFilter().orElse("")))
							.collect(Collectors.toList()).size());
	}
	
	
	private DataProvider<GroupTemplate, String> createGroupDataProvider() {
		return DataProvider.fromFilteringCallbacks(query -> {
			List<Group> groupList = identityService.createGroupQuery().list()
					.stream()
						.filter(group -> group.getId().startsWith(query.getFilter().orElse(""))).collect(Collectors.toList());
			return groupList.stream().map(m -> {
				GroupTemplate group = new GroupTemplate();
				group.setId(m.getId());
				group.setName(m.getName());
				group.setType(m.getType());
				group.setEdit(buttonEditGroup(m));
				group.setDelete(buttonDeleteGroup(m));
				return group;
			});
		}, query -> identityService.createGroupQuery().list()
					.stream()
						.filter(group -> group.getId().startsWith(query.getFilter().orElse("")))
							.collect(Collectors.toList()).size());
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
			String result = getI18nText(getI18nText(I18N_CAPTION_USERS));
			if (item == EnumUserGroup.GROUPS) {
				result = getI18nText(getI18nText(I18N_CAPTION_GROUPS));
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
		userFilter = userDataProvider.withConfigurableFilter();
		Grid<UserTemplate> grid = new Grid<>();
		grid.addColumn(UserTemplate::getId).setCaption(getI18nText(I18N_CAPTION_COLUMN_LOGIN));
		grid.addColumn(UserTemplate::getFirstName).setCaption(getI18nText(I18N_CAPTION_COLUMN_FIRSTNAME));
		grid.addColumn(UserTemplate::getLastName).setCaption(getI18nText(I18N_CAPTION_COLUMN_LASTNAME));
		grid.addColumn(UserTemplate::getEmail).setCaption(getI18nText(I18N_CAPTION_COLUMN_EMAIL));
		grid.setDataProvider(userFilter);
		grid.setColumnReorderingAllowed(true);
		grid.setSizeFull();
		grid.setHeightMode(HeightMode.ROW);
		grid.setHeightByRows(14);
		infoUser.setVisible(false);	
		SingleSelectionModel<UserTemplate> singleSelect = (SingleSelectionModel<UserTemplate>) grid.getSelectionModel();
		singleSelect.addSingleSelectionListener(event -> 
			singleSelect.getSelectedItem().ifPresent(item -> {
				infoUser.getUserInfoVerticalLayout().setData(item);
				infoUser.getUserAddGroupTab().setUser(item);
				infoUser.setVisible(true);
			})
		);
		
		return grid;
	}

	private Grid<GroupTemplate> createGridGroup() {
		groupFilter = groupDataProvider.withConfigurableFilter();
		Grid<GroupTemplate> grid = new Grid<>();
		grid.setSizeFull();
		grid.addColumn(GroupTemplate::getId).setCaption(getI18nText(I18N_CAPTION_COLUMN_ID));
		grid.addColumn(GroupTemplate::getName).setCaption(getI18nText(I18N_CAPTION_COLUMN_NAMEROLES));
		grid.addColumn(GroupTemplate::getType).setCaption(getI18nText(I18N_CAPTION_COLUMN_TYPEROLES));
		grid.addComponentColumn(GroupTemplate::getEdit).setCaption(getI18nText(I18N_CAPTION_COLUMN_EDIT)).setWidth(105.0);
		grid.addComponentColumn(GroupTemplate::getDelete).setCaption(getI18nText(I18N_CAPTION_COLUMN_DELETE)).setWidth(105.0);
		grid.setDataProvider(groupFilter);
		grid.setColumnReorderingAllowed(true);
		grid.setSizeFull();
		grid.setHeightMode(HeightMode.ROW);
		grid.setHeightByRows(14);
		return grid;
	}

	private Button createButtonCreate() {
		String nameCreateButton = getI18nText("adminView.button.nameCreateButton");
		Button create = new Button(nameCreateButton);
		create.addClickListener(event -> {
			if (selectUserGroup.getSelectedItem().get().equals(EnumUserGroup.USERS)) {
				infoUser.getUserInfoVerticalLayout().setData(null);
				infoUser.setVisible(true);
			} else if (selectUserGroup.getSelectedItem().get().equals(EnumUserGroup.GROUPS)) {
				getUI().addWindow(formGroup(null));
			}
		});
		return create;
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
		String textInfo = getI18nText("adminView.ConfirmDialog.deleteGroup.info");
		String textOKButton = getI18nText("adminView.ConfirmDialog.deleteUser.ok");
		String textCloseButton = getI18nText("adminView.ConfirmDialog.deleteUser.close");
		ClickListener okDeleteClick = event -> {
			identityService.deleteGroup(group.getId());
			groupDataProvider.refreshAll();
			String[] paramsForDelete = new String[] {group.getId()};
			String notificationDeleted = getI18nText("adminView.notification.user.deleted", paramsForDelete);
			Notification.show(notificationDeleted, Type.WARNING_MESSAGE);
		};
		deleteButton.addClickListener(event -> {
			ConfirmDialogWindow box = new ConfirmDialogWindow(textInfo, textOKButton, textCloseButton, okDeleteClick);
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
		//TODO: неверные данные
		String necessarylogin = getI18nText("adminView.necessary.user.login");
		String necessaryFirstName = getI18nText("adminView.necessary.user.firstName");
		String necessaryLastName = getI18nText("adminView.necessary.user.lastName");
		
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
		
		Binder<Group> groupFormBinder  = new Binder<>();
		Button saveButton = new Button(nameSaveButton);
		saveButton.setEnabled(false);
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
		//TODO: заменить константы
		groupFormBinder.forField(idField).asRequired(necessarylogin).bind(Group::getId, Group::setId);
		groupFormBinder.forField(nameField).asRequired(necessaryFirstName).bind(Group::getName, Group::setName);
		groupFormBinder.forField(typeField).asRequired(necessaryLastName).bind(Group::getType, Group::setType);

		idField.setWidth(90.0f, Unit.PERCENTAGE);
		nameField.setWidth(90.0f, Unit.PERCENTAGE);
		typeField.setWidth(90.0f, Unit.PERCENTAGE);
		content.addComponent(idField);
		content.addComponent(nameField);
		content.addComponent(typeField);
		content.addComponent(saveButton);
		window.setContent(content);
		groupFormBinder.addValueChangeListener(event -> saveButton.setEnabled(groupFormBinder.validate().isOk()));
		saveButton.addClickListener(event -> {
			Group group = identityService.createGroupQuery().groupId(idField.getValue()).singleResult();
			String[] paramsForSave;
			try {
				if (group == null) {
					group = identityService.newGroup(idField.getValue());
					paramsForSave = new String[] {group.getId()};
					String notificationCreated = getI18nText("adminView.notification.group.created", paramsForSave);
					Notification.show(notificationCreated, Type.TRAY_NOTIFICATION);
				} else if (group.getId() != null) {
					paramsForSave = new String[] {group.getId()};
					String notificationChanged = getI18nText("adminView.notification.group.change", paramsForSave);
					Notification.show(notificationChanged, Type.TRAY_NOTIFICATION);
				}
			groupFormBinder.writeBean(group);
		    } catch (ValidationException e) {
		    	paramsForSave = new String[] {group.getId()};
		    	String notificationSaveErr = getI18nText("adminView.notification.group.change", paramsForSave);
		    	Notification.show(notificationSaveErr);
		    }
			groupFormBinder.setBean(group);
			identityService.saveGroup(group);
			groupDataProvider.refreshAll();
			window.close();
		});
		return window;
	}
	
	private String getI18nText(String key) {
		return messageSource.getMessage(key, null, VaadinSession.getCurrent().getLocale());
	}
	
	private String getI18nText(String key, String[] params) {
		return messageSource.getMessage(key, params, VaadinSession.getCurrent().getLocale());
	}
}

enum EnumUserGroup {
	USERS, GROUPS;
}