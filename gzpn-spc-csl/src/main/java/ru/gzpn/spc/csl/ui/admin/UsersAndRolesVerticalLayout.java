package ru.gzpn.spc.csl.ui.admin;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Alignment;
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
import ru.gzpn.spc.csl.ui.common.I18n;

@SuppressWarnings("serial")
public class UsersAndRolesVerticalLayout extends VerticalLayout implements I18n {
	
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
	public static final String I18N_CAPTION_USERS = "adminView.caption.usersKey";
	public static final String I18N_CAPTION_GROUPS = "adminView.caption.groupsKey";
	public static final String I18N_CAPTION_GROUP = "adminView.caption.groupKey";
	public static final String I18N_CAPTION_LOGIN = "adminView.caption.login";
	public static final String I18N_CAPTION_FIRSTNAME = "adminView.caption.firstName";
	public static final String I18N_CAPTION_LASTNAME = "adminView.caption.lastName";
	public static final String I18N_CAPTION_EMAIL = "adminView.caption.email";
	public static final String I18N_CAPTION_IDROLES = "adminView.caption.id";
	public static final String I18N_CAPTION_NAMEROLES = "adminView.caption.nameRoles";
	public static final String I18N_CAPTION_TYPEROLES = "adminView.caption.typeRoles";
	public static final String I18N_CAPTION_EDIT = "adminView.caption.edit";
	public static final String I18N_CAPTION_DELETE = "adminView.caption.delete";
	public static final String I18N_CAPTION_BUTTON_CREATE = "adminView.button.nameCreateButton";
	public static final String I18N_CAPTION_BUTTON_OK = "adminView.ConfirmDialog.deleteUser.ok";
	public static final String I18N_CAPTION_BUTTON_CLOSE = "adminView.ConfirmDialog.deleteUser.close";
	public static final String I18N_CAPTION_BUTTON_SAVE = "adminView.button.nameSaveButton";
	public static final String I18N_CONFIRMDIALOG_INFO_DELETEGROUP = "adminView.ConfirmDialog.deleteGroup.info";
	public static final String I18N_NECESSARY_GROUPID = "adminView.necessary.group.id";
	public static final String I18N_NECESSARY_GROUPNAME = "adminView.necessary.group.name";
	public static final String I18N_NECESSARY_GROUPTYPE = "adminView.necessary.group.type";
	public static final String I18N_NOTIFICATION_GROUPCREATED = "adminView.notification.group.created";
	public static final String I18N_NOTIFICATION_GROUPCHANGED = "adminView.notification.group.change";
	public static final String I18N_NOTIFICATION_GROUPDELETED = "adminView.notification.group.deleted";
	public static final String I18N_SEARCHFIELD_PLACEHOLDER = "adminView.placeholder.searchfield";
	
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
		headerHorizont.setDefaultComponentAlignment(Alignment.BOTTOM_LEFT);
		headerHorizont.addComponents(searchUserGroup, selectUserGroup, createButton);
		headerHorizont.setMargin(marginForHeader);
		
		resultPage.addComponents(headerHorizont, gridUser, gridGroup);
		resultPage.setMargin(false);
		panel.setSplitPosition(70, Unit.PERCENTAGE);
		panel.setMaxSplitPosition(70, Unit.PERCENTAGE);
		panel.setMinSplitPosition(545, Unit.PIXELS);
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
						.filter(user -> filterUser(user, query.getFilter().orElse("")))
							.collect(Collectors.toList());
			
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
						.filter(user -> filterUser(user, query.getFilter().orElse("")))
							.collect(Collectors.toList()).size());
	}
	
	private boolean filterUser(User user, String filter) {
		return StringUtils.startsWithIgnoreCase(user.getId(), filter) ||
			   StringUtils.startsWithIgnoreCase(user.getFirstName(), filter) ||
			   StringUtils.startsWithIgnoreCase(user.getLastName(), filter);
	}

	private DataProvider<GroupTemplate, String> createGroupDataProvider() {
		return DataProvider.fromFilteringCallbacks(query -> {
			List<Group> groupList = identityService.createGroupQuery().list()
					.stream()
						.filter(group -> filterGroup(group, query.getFilter().orElse("")))
							.collect(Collectors.toList());
			
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
						.filter(group -> filterGroup(group, query.getFilter().orElse("")))
							.collect(Collectors.toList()).size());
	}
	
	private boolean filterGroup(Group group, String filter) {
		return StringUtils.startsWithIgnoreCase(group.getId(), filter) ||
			   StringUtils.startsWithIgnoreCase(group.getName(), filter) ||
			   StringUtils.startsWithIgnoreCase(group.getType(), filter);
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
			String result = getI18nText(I18N_CAPTION_USERS, messageSource);
			if (item == EnumUserGroup.GROUPS) {
				result = getI18nText(I18N_CAPTION_GROUPS, messageSource);
			}
			return result;
		});

		comboBox.addSelectionListener(event -> {
			if (event.getSelectedItem().get().equals(EnumUserGroup.USERS)) {
				gridUser.setVisible(true);
				gridGroup.setVisible(false);
				infoUser.setVisible(true);
				panel.setLocked(false);
				panel.setMaxSplitPosition(70, Unit.PERCENTAGE);
				panel.setSplitPosition(70, Unit.PERCENTAGE);
			} else if (event.getSelectedItem().get().equals(EnumUserGroup.GROUPS)) {
				gridUser.setVisible(false);
				gridGroup.setVisible(true);
				infoUser.setVisible(false);
				panel.setMaxSplitPosition(100, Unit.PERCENTAGE);
				panel.setSplitPosition(100, Unit.PERCENTAGE);
				panel.setLocked(true);
			}
			searchUserGroup.setValue("");
		});
		return comboBox;
	}

	private TextField createSearchUserGroup() {
		TextField filterTextField = new TextField();
		filterTextField.setWidth(240, Unit.PIXELS);
		filterTextField.setPlaceholder(getI18nText(I18N_SEARCHFIELD_PLACEHOLDER, messageSource));
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
		grid.addColumn(UserTemplate::getId).setCaption(getI18nText(I18N_CAPTION_LOGIN, messageSource));
		grid.addColumn(UserTemplate::getFirstName).setCaption(getI18nText(I18N_CAPTION_FIRSTNAME, messageSource));
		grid.addColumn(UserTemplate::getLastName).setCaption(getI18nText(I18N_CAPTION_LASTNAME, messageSource));
		grid.addColumn(UserTemplate::getEmail).setCaption(getI18nText(I18N_CAPTION_EMAIL, messageSource));
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
		grid.addColumn(GroupTemplate::getId).setCaption(getI18nText(I18N_CAPTION_IDROLES, messageSource));
		grid.addColumn(GroupTemplate::getName).setCaption(getI18nText(I18N_CAPTION_NAMEROLES, messageSource));
		grid.addColumn(GroupTemplate::getType).setCaption(getI18nText(I18N_CAPTION_TYPEROLES, messageSource));
		grid.addComponentColumn(GroupTemplate::getEdit).setCaption(getI18nText(I18N_CAPTION_EDIT, messageSource)).setWidth(105.0);
		grid.addComponentColumn(GroupTemplate::getDelete).setCaption(getI18nText(I18N_CAPTION_DELETE, messageSource)).setWidth(105.0);
		grid.setDataProvider(groupFilter);
		grid.setColumnReorderingAllowed(true);
		grid.setSizeFull();
		grid.setHeightMode(HeightMode.ROW);
		grid.setHeightByRows(14);
		return grid;
	}

	private Button createButtonCreate() {
		Button create = new Button(getI18nText(I18N_CAPTION_BUTTON_CREATE, messageSource));
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
		ClickListener okDeleteClick = event -> {
			identityService.deleteGroup(group.getId());
			groupDataProvider.refreshAll();
			String[] paramsForDelete = new String[] {group.getId()};
			String notificationDeleted = getI18nText(I18N_NOTIFICATION_GROUPDELETED, paramsForDelete, messageSource);
			Notification.show(notificationDeleted, Type.WARNING_MESSAGE);
		};
		deleteButton.addClickListener(event -> {
			ConfirmDialogWindow box = new ConfirmDialogWindow(getI18nText(I18N_CONFIRMDIALOG_INFO_DELETEGROUP, messageSource), 
															  getI18nText(I18N_CAPTION_BUTTON_OK, messageSource), 
															  getI18nText(I18N_CAPTION_BUTTON_CLOSE, messageSource), okDeleteClick);
			getUI().addWindow(box);
		});
		return deleteButton;
	}

	private Window formGroup(Group currentGroup) {
		final Window window = new Window(getI18nText(I18N_CAPTION_GROUP, messageSource));
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
		Button saveButton = new Button(getI18nText(I18N_CAPTION_BUTTON_SAVE, messageSource));
		saveButton.setEnabled(false);
		if (currentGroup != null) {
			idField = new TextField(getI18nText(I18N_CAPTION_IDROLES, messageSource), currentGroup.getId());
			idField.setReadOnly(true);
			nameField = new TextField(getI18nText(I18N_CAPTION_NAMEROLES, messageSource), currentGroup.getName());
			typeField = new TextField(getI18nText(I18N_CAPTION_TYPEROLES, messageSource), currentGroup.getType());
		}
		else {
			idField = new TextField(getI18nText(I18N_CAPTION_IDROLES, messageSource), "");
			nameField = new TextField(getI18nText(I18N_CAPTION_NAMEROLES, messageSource), "");
			typeField = new TextField(getI18nText(I18N_CAPTION_TYPEROLES, messageSource), "");
		}
		groupFormBinder.forField(idField).asRequired(getI18nText(I18N_NECESSARY_GROUPID, messageSource)).bind(Group::getId, Group::setId);
		groupFormBinder.forField(nameField).asRequired(getI18nText(I18N_NECESSARY_GROUPNAME, messageSource)).bind(Group::getName, Group::setName);
		groupFormBinder.forField(typeField).asRequired(getI18nText(I18N_NECESSARY_GROUPTYPE, messageSource)).bind(Group::getType, Group::setType);
		
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
					String notificationCreated = getI18nText(I18N_NOTIFICATION_GROUPCREATED, paramsForSave, messageSource);
					Notification.show(notificationCreated, Type.TRAY_NOTIFICATION);
				} else if (group.getId() != null) {
					paramsForSave = new String[] {group.getId()};
					String notificationChanged = getI18nText(I18N_NOTIFICATION_GROUPCHANGED, paramsForSave, messageSource);
					Notification.show(notificationChanged, Type.TRAY_NOTIFICATION);
				}
			groupFormBinder.writeBean(group);
		    } catch (ValidationException e) {
		    	paramsForSave = new String[] {group.getId()};
		    	String notificationSaveErr = getI18nText(I18N_NOTIFICATION_GROUPCHANGED, paramsForSave, messageSource);
		    	Notification.show(notificationSaveErr);
		    }
			groupFormBinder.setBean(group);
			identityService.saveGroup(group);
			groupDataProvider.refreshAll();
			window.close();
		});
		return window;
	}
}

enum EnumUserGroup {
	USERS, GROUPS;
}