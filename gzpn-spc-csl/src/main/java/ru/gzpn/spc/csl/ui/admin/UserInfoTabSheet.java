package ru.gzpn.spc.csl.ui.admin;

import java.util.List;
import java.util.stream.Collectors;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import ru.gzpn.spc.csl.ui.common.ConfirmDialog;
import ru.gzpn.spc.csl.ui.common.JoinedLayout;

public class UserInfoTabSheet extends TabSheet {
	
	private UserInfoVerticalLayout userInfoVerticalLayout;
	private UserAddGroupVerticalLayout userAddGroupTab;
	private MessageSource messageSource;
	private IdentityService identityService;
	private UsersAndRolesVerticalLayout usersAndRolesTab;
	private UIContainer container;

	public UserInfoTabSheet(IdentityService identityService, 
			MessageSource messageSource, 
			DataProvider<UserTemplate, String> userDataProvider, 
			DataProvider<GroupTemplate, String> groupDataProvider, 
			UIContainer container) {
		this.messageSource = messageSource;
		this.identityService = identityService;
		this.container = container;
		userInfoVerticalLayout = new UserInfoVerticalLayout(identityService, messageSource, userDataProvider, container);
		userAddGroupTab = new UserAddGroupVerticalLayout(identityService, messageSource);
		String editInfoUser = getI18nText("adminView.caption.editInfoUser");
		String editGroupUser = getI18nText("adminView.caption.editGroupUser");
		this.addTab(userInfoVerticalLayout, editInfoUser);
		this.addTab(userAddGroupTab, editGroupUser);
		userInfoVerticalLayout.setUserAndRolesVerticalLayout(usersAndRolesTab);
		
	}
	
	public UserInfoVerticalLayout getUserInfoVerticalLayout() {
		return userInfoVerticalLayout;
	}
	
	public UserAddGroupVerticalLayout getUserAddGroupTab() {
		return userAddGroupTab;
	}
	
	public void setUserAndRolesTab(UsersAndRolesVerticalLayout tab) {
		usersAndRolesTab = tab;
	}
	
	private String getI18nText(String key) {
		return messageSource.getMessage(key, null, VaadinSession.getCurrent().getLocale());
	}
}

class UserInfoVerticalLayout extends VerticalLayout {
	
	private final TextField loginField;
	private final TextField firstNameField;
	private final TextField lastNameField;
	private final TextField emailField;
	private final PasswordField newPasswordField;
	private Button saveButton;
	private Button editButton;
	private Button cancelButton;
	private Button deleteButton;
	private MessageSource messageSource;
	private IdentityService identityService;
	private Binder<User> formBinder;
	private UsersAndRolesVerticalLayout usersAndRolesVerticalLayout;
	private UIContainer container;
	
	public UserInfoVerticalLayout(IdentityService identityService, 
					MessageSource messageSource, 
					DataProvider<UserTemplate, String> userDataProvider, 
					UIContainer container) {
		this.messageSource = messageSource;
		this.identityService = identityService;
		this.container = container;
		String loginCaption = getI18nText("adminView.caption.login");
		String firstNameCaption = getI18nText("adminView.caption.firstName");
		String lastNameCaption = getI18nText("adminView.caption.lastName");
		String emailCaption = getI18nText("adminView.caption.email");
		String newPasswordCaption = getI18nText("adminView.caption.newPasswordCaption");
		this.setMargin(true);
		this.addStyleName("outlined");
		final HorizontalLayout topButtonLayout = new HorizontalLayout();
		final FormLayout infoLayout = new FormLayout();
		final HorizontalLayout buttonLayout = new HorizontalLayout();
		
		loginField = new TextField(loginCaption, "");
		loginField.setStyleName(ValoTheme.TEXTAREA_BORDERLESS, true);
		loginField.setReadOnly(true);
		firstNameField = new TextField(firstNameCaption, "");
		firstNameField.setStyleName(ValoTheme.TEXTAREA_BORDERLESS, true);
		firstNameField.setReadOnly(true);
		lastNameField = new TextField(lastNameCaption, "");
		lastNameField.setStyleName(ValoTheme.TEXTAREA_BORDERLESS, true);
		lastNameField.setReadOnly(true);
		emailField = new TextField(emailCaption, "");
		emailField.setStyleName(ValoTheme.TEXTAREA_BORDERLESS, true);
		emailField.setReadOnly(true);
		newPasswordField = new PasswordField(newPasswordCaption, "");
		newPasswordField.setStyleName(ValoTheme.TEXTAREA_BORDERLESS, true);
		newPasswordField.setReadOnly(true);
		loginField.setWidth(90.0f, Unit.PERCENTAGE);
		firstNameField.setWidth(90.0f, Unit.PERCENTAGE);
		lastNameField.setWidth(90.0f, Unit.PERCENTAGE);
		emailField.setWidth(90.0f, Unit.PERCENTAGE);
		newPasswordField.setWidth(90.0f, Unit.PERCENTAGE);
		formBinder = createBinder();	
		saveButton = createSaveButton(userDataProvider);
		editButton = createEditButton();
		cancelButton = createCancelButton(userDataProvider);
		deleteButton = createDeleteButton(userDataProvider);
		topButtonLayout.setSizeFull();
		topButtonLayout.addComponents(editButton, cancelButton);
		topButtonLayout.setComponentAlignment(editButton, Alignment.MIDDLE_RIGHT);
		topButtonLayout.setComponentAlignment(cancelButton, Alignment.MIDDLE_RIGHT);
		topButtonLayout.setMargin(false);
		this.addComponent(topButtonLayout);
		infoLayout.setSizeFull();
		infoLayout.addComponent(loginField);
		infoLayout.addComponent(firstNameField);
		infoLayout.addComponent(lastNameField);
		infoLayout.addComponent(emailField);
		infoLayout.addComponent(newPasswordField);
		infoLayout.setMargin(false);
		this.addComponent(infoLayout);
		buttonLayout.setSizeFull();
		buttonLayout.addComponents(saveButton, deleteButton);
		buttonLayout.setComponentAlignment(saveButton, Alignment.MIDDLE_RIGHT);
		buttonLayout.setComponentAlignment(deleteButton, Alignment.MIDDLE_RIGHT);
		buttonLayout.setMargin(false);
		this.addComponents(buttonLayout);	
	}
	
	private Binder<User> createBinder() {
		Binder<User> binder = new Binder<>();
		String necessarylogin = getI18nText("adminView.necessary.user.login");
		String necessaryFirstName = getI18nText("adminView.necessary.user.firstName");
		String necessaryLastName = getI18nText("adminView.necessary.user.lastName");
		String necessaryEmail = getI18nText("adminView.necessary.user.email");
		String necessaryPassword = getI18nText("adminView.necessary.user.password");
		String emailValid = getI18nText("adminView.necessary.user.emailValid");
		String passwordValid = getI18nText("adminView.necessary.user.passwordValid");
		binder.forField(loginField).asRequired(necessarylogin).bind(User::getId, User::setId);
		binder.forField(firstNameField).asRequired(necessaryFirstName).bind(User::getFirstName, User::setFirstName);
		binder.forField(lastNameField).asRequired(necessaryLastName).bind(User::getLastName, User::setLastName);
		binder.forField(emailField).asRequired(necessaryEmail)
			.withValidator(new EmailValidator(emailValid)).bind(User::getEmail, User::setEmail);
		binder.forField(newPasswordField).asRequired(necessaryPassword)
            .withValidator(new StringLengthValidator(passwordValid, 6, null))
            .bind((usrLocal) -> "", (usrLocal, pwd) -> usrLocal.setPassword(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(pwd)));
		return binder;
	}

	private Button createEditButton() {
		String nameEditButton = getI18nText("adminView.caption.edit");
		editButton = new Button(nameEditButton);
		editButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
		editButton.addClickListener(event -> {
			editButton.setEnabled(false);
			editButton.setVisible(false);
			if(loginField.getValue().isEmpty()) {
				loginField.setStyleName(ValoTheme.TEXTAREA_BORDERLESS, false);
				loginField.setReadOnly(false);
			}
			firstNameField.setStyleName(ValoTheme.TEXTAREA_BORDERLESS, false);
			firstNameField.setReadOnly(false);
			lastNameField.setStyleName(ValoTheme.TEXTAREA_BORDERLESS, false);
			lastNameField.setReadOnly(false);
			emailField.setStyleName(ValoTheme.TEXTAREA_BORDERLESS, false);
			emailField.setReadOnly(false);
			newPasswordField.setStyleName(ValoTheme.TEXTAREA_BORDERLESS, false);
			newPasswordField.setReadOnly(false);

			saveButton.setVisible(true);
			editButton.setEnabled(false);
			deleteButton.setEnabled(false);
			cancelButton.setEnabled(true);
			cancelButton.setVisible(true);
			container.getCreateUserAndRolesButton().setEnabled(false);
		});
		formBinder.addValueChangeListener(event -> saveButton.setEnabled(formBinder.validate().isOk()));
		return editButton;
	}
	
	private Button createSaveButton(DataProvider<UserTemplate, String> userDataProvider) {
		String nameSaveButton = getI18nText("adminView.button.nameSaveButton");
		saveButton = new Button(nameSaveButton);
		saveButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
		saveButton.setEnabled(false);
		saveButton.setVisible(false);
		saveButton.addClickListener(event -> {
			loginField.setStyleName(ValoTheme.TEXTAREA_BORDERLESS, true);
			loginField.setReadOnly(true);
			firstNameField.setStyleName(ValoTheme.TEXTAREA_BORDERLESS, true);
			firstNameField.setReadOnly(true);
			lastNameField.setStyleName(ValoTheme.TEXTAREA_BORDERLESS, true);
			lastNameField.setReadOnly(true);
			emailField.setStyleName(ValoTheme.TEXTAREA_BORDERLESS, true);
			emailField.setReadOnly(true);
			newPasswordField.setStyleName(ValoTheme.TEXTAREA_BORDERLESS, true);
			newPasswordField.setReadOnly(true);
			User user = identityService.createUserQuery().userId(loginField.getValue()).singleResult();
			String[] paramsForSave;
			try {
			      if (user == null) {	
						user = identityService.newUser(loginField.getValue());
						paramsForSave = new String[] {user.getId()};
						String notificationCreated = getI18nText("adminView.notification.user.created", paramsForSave);
						Notification.show(notificationCreated, Type.TRAY_NOTIFICATION);
						
					} else if (user.getId() != null) {
						paramsForSave = new String[] {user.getId()};
						String notificationChanged = getI18nText("adminView.notification.user.change", paramsForSave);
						Notification.show(notificationChanged, Type.TRAY_NOTIFICATION);
					}
			      formBinder.writeBean(user);
			    } catch (ValidationException e) {
			      paramsForSave = new String[] {user.getId()};
				  String notificationSaveErr = getI18nText("adminView.notification.user.change", paramsForSave);
			      Notification.show(notificationSaveErr);
			    }
			formBinder.setBean(user);
			identityService.saveUser(user);
			userDataProvider.refreshAll();
			saveButton.setEnabled(false);
			saveButton.setVisible(false);
			editButton.setVisible(true);
			editButton.setEnabled(true);
			deleteButton.setEnabled(true);
			cancelButton.setEnabled(false);
			cancelButton.setVisible(false);
			container.getCreateUserAndRolesButton().setEnabled(true);
		});
		return saveButton;
	}
	
	private Button createCancelButton(DataProvider<UserTemplate, String> userDataProvider) {
		String textCloseButton = getI18nText("adminView.ConfirmDialog.deleteUser.close");
		cancelButton = new Button(textCloseButton);
		cancelButton.setEnabled(false);
		cancelButton.setVisible(false);
		cancelButton.addClickListener(event -> {
			User user = identityService.createUserQuery().userId(loginField.getValue()).singleResult();
			if (user == null) {	
			    loginField.setValue("");
			    firstNameField.setValue("");
			    lastNameField.setValue("");
			    emailField.setValue("");
			    newPasswordField.setValue("");
			} else if (user.getId() != null) {
			    loginField.setValue(user.getId() == null ? "" : user.getId());
			    firstNameField.setValue(user.getFirstName() == null ? "" : user.getFirstName());
			    lastNameField.setValue(user.getLastName() == null ? "" : user.getLastName());
			    emailField.setValue(user.getEmail() == null ? "" : user.getEmail());
			    newPasswordField.setValue("");
			}
			
			loginField.setStyleName(ValoTheme.TEXTAREA_BORDERLESS, true);
			loginField.setReadOnly(true);
			firstNameField.setStyleName(ValoTheme.TEXTAREA_BORDERLESS, true);
			firstNameField.setReadOnly(true);
			lastNameField.setStyleName(ValoTheme.TEXTAREA_BORDERLESS, true);
			lastNameField.setReadOnly(true);
			emailField.setStyleName(ValoTheme.TEXTAREA_BORDERLESS, true);
			emailField.setReadOnly(true);
			newPasswordField.setStyleName(ValoTheme.TEXTAREA_BORDERLESS, true);
			newPasswordField.setReadOnly(true);
	
			userDataProvider.refreshAll();
			editButton.setEnabled(true);
			editButton.setVisible(true);
			cancelButton.setEnabled(false);
			cancelButton.setVisible(false);
			saveButton.setEnabled(false);
			saveButton.setVisible(false);
			deleteButton.setEnabled(true);
			container.getCreateUserAndRolesButton().setEnabled(true);
		});	
		
		return cancelButton;
	}
	
	private Button createDeleteButton(DataProvider<UserTemplate, String> userDataProvider) {
		String textInfo = getI18nText("adminView.ConfirmDialog.deleteUser.info");
		String textOKButton = getI18nText("adminView.ConfirmDialog.deleteUser.ok");
		String textCloseButton = getI18nText("adminView.ConfirmDialog.deleteUser.close");
		String nameDeleteButton = getI18nText("adminView.caption.delete");
		deleteButton = new Button(nameDeleteButton);
		deleteButton.setStyleName(ValoTheme.BUTTON_DANGER);
		deleteButton.setIcon(VaadinIcons.TRASH);
	
		ClickListener okDeleteClick = event -> {
			User user = identityService.createUserQuery().userId(loginField.getValue()).singleResult();
			identityService.deleteUser(user.getId());
			userDataProvider.refreshAll();
			String[] paramsForDelete = new String[] {user.getId()};
			String notificationDeleted = getI18nText("adminView.notification.user.deleted", paramsForDelete);
			Notification.show(notificationDeleted, Type.WARNING_MESSAGE);
			loginField.setValue("");
			firstNameField.setValue("");
			lastNameField.setValue("");
			emailField.setValue("");
			newPasswordField.setValue("");
		};
		
		deleteButton.addClickListener(event -> {
			ConfirmDialog box = new ConfirmDialog(textInfo, textOKButton, textCloseButton, okDeleteClick);
			getUI().addWindow(box);
		});	
		return deleteButton;
	}

	public void setUserAndRolesVerticalLayout(UsersAndRolesVerticalLayout verticalLayout) {
		usersAndRolesVerticalLayout = verticalLayout;
	}

	public UsersAndRolesVerticalLayout getUsersAndRolesVerticalLayout() {
		return usersAndRolesVerticalLayout;
	}

	public void setData(UserTemplate template) {
		String newPasswordCaption = getI18nText("adminView.caption.newPasswordCaption");
		String passwordCaption = getI18nText("adminView.caption.passwordCaption");
		
		if(template == null) {
			loginField.setValue("");
			firstNameField.setValue("");
			lastNameField.setValue("");
			emailField.setValue("");
			newPasswordField.setValue("");
			newPasswordField.setCaption(passwordCaption);
			deleteButton.setEnabled(false);

			editButton.setEnabled(false);
			editButton.setVisible(false);
			if(loginField.getValue().isEmpty()) {
				loginField.setStyleName(ValoTheme.TEXTAREA_BORDERLESS, false);
				loginField.setReadOnly(false);
			}
			firstNameField.setStyleName(ValoTheme.TEXTAREA_BORDERLESS, false);
			firstNameField.setReadOnly(false);
			lastNameField.setStyleName(ValoTheme.TEXTAREA_BORDERLESS, false);
			lastNameField.setReadOnly(false);
			emailField.setStyleName(ValoTheme.TEXTAREA_BORDERLESS, false);
			emailField.setReadOnly(false);
			newPasswordField.setStyleName(ValoTheme.TEXTAREA_BORDERLESS, false);
			newPasswordField.setReadOnly(false);

			saveButton.setVisible(true);
			editButton.setEnabled(false);
			deleteButton.setEnabled(false);
			cancelButton.setEnabled(true);
			cancelButton.setVisible(true);
			container.getCreateUserAndRolesButton().setEnabled(false);
			formBinder.addValueChangeListener(event -> saveButton.setEnabled(formBinder.validate().isOk()));
		}
		else {
			loginField.setValue(template.getId() == null ? "" : template.getId());
			firstNameField.setValue(template.getFirstName() == null ? "" : template.getFirstName());
			lastNameField.setValue(template.getLastName() == null ? "" : template.getLastName());
			emailField.setValue(template.getEmail() == null ? "" : template.getEmail());
			newPasswordField.setValue("");
			newPasswordField.setCaption(newPasswordCaption);
		}
		
	}	

	private String getI18nText(String key) {
		return messageSource.getMessage(key, null, VaadinSession.getCurrent().getLocale());
	}
	
	private String getI18nText(String key, String[] params) {
		return messageSource.getMessage(key, params, VaadinSession.getCurrent().getLocale());
	}
}


class UserAddGroupVerticalLayout extends VerticalLayout {
	
	private MessageSource messageSource;
	private IdentityService identityService;
	private HorizontalLayout headerHorizont;
	private ComboBox<String> selectGroup;
	private Button addGroupButton;
	private DataProvider<String, String> selectGroupProvider = createSelectGroupProvider();
	private ConfigurableFilterDataProvider<String, Void, String> selectGroupFilter;
	private Button editButton;
	private Button viewButton;
	private DataProvider<GroupTemplate, String> groupForUser = createDataProvider();
	private ConfigurableFilterDataProvider<GroupTemplate, Void, String> groupUserIDFilter;
	private UserTemplate currentUser;
	private Grid<GroupTemplate> gridGroupAddUser;
	private JoinedLayout<AbstractComponent, AbstractComponent> joinedComponent;
	
	public UserAddGroupVerticalLayout(IdentityService identityService, 
			MessageSource messageSource) {
		this.messageSource = messageSource;
		this.identityService = identityService;
		setHeight(100, Unit.PERCENTAGE);
		selectGroup = createSelectGroup(selectGroupProvider);
		addGroupButton = createAddGroupButton();
		editButton = createEditButton();
		viewButton = createViewButton();
		joinedComponent = new JoinedLayout<>(selectGroup, addGroupButton);
		headerHorizont = new HorizontalLayout();
		joinedComponent.setVisible(false);
		joinedComponent.setEnabled(false);
		viewButton.setVisible(false);
		viewButton.setEnabled(false);
		headerHorizont.setSizeFull();
		
		headerHorizont.addComponents(joinedComponent, editButton, viewButton);
		headerHorizont.setComponentAlignment(editButton, Alignment.MIDDLE_RIGHT);
		headerHorizont.setComponentAlignment(viewButton, Alignment.MIDDLE_RIGHT);
		gridGroupAddUser = createGroupAddUser();
		addComponents(headerHorizont, gridGroupAddUser);
	}

	private DataProvider<String, String> createSelectGroupProvider() {
		return DataProvider.fromFilteringCallbacks(query -> {
			List<String> groupList = identityService.createGroupQuery().list().stream().
					filter(group -> group.getId().startsWith(query.getFilter().orElse("")) 
									|| group.getName().startsWith(query.getFilter().orElse(""))
						).map(Group :: getId).collect(Collectors.toList());
			return groupList.stream();
			
		}, query -> identityService.createGroupQuery().list().stream().
					filter(group -> group.getId().startsWith(query.getFilter().orElse(""))
								|| group.getName().startsWith(query.getFilter().orElse(""))
					).collect(Collectors.toList()).size());
	}

	private DataProvider<GroupTemplate, String> createDataProvider() {
		return DataProvider.fromFilteringCallbacks(query -> {
			List<Group> groupList = identityService.createGroupQuery().groupMember(query.getFilter().orElse("admin")).list();
			return groupList.stream().map(m -> {
				GroupTemplate group = new GroupTemplate();
				group.setId(m.getId());
				group.setName(m.getName());
				group.setType(m.getType());
				group.setDelete(buttonDeleteGroupMemberUser(query.getFilter().orElse("admin"), m));
				return group;
			});
		}, query -> (int) identityService.createGroupQuery().groupMember(query.getFilter().orElse("admin")).count());
	}

	private Button buttonDeleteGroupMemberUser(String userID, Group group) {
		Button deleteButton = new Button();
		String textInfo = getI18nText("adminView.ConfirmDialog.deleteGroup.info");
		String textOKButton = getI18nText("adminView.ConfirmDialog.deleteUser.ok");
		String textCloseButton = getI18nText("adminView.ConfirmDialog.deleteUser.close");
		ClickListener okDeleteClick = event -> {
			identityService.deleteMembership(userID, group.getId());
			groupForUser.refreshAll();
			String[] paramsForDelete = new String[] {group.getId(), userID};
			String notificationDeleted = getI18nText("adminView.notification.group.deletedFromUser", paramsForDelete);
			Notification.show(notificationDeleted, Type.WARNING_MESSAGE);
		};
		deleteButton.addClickListener(event -> {
			ConfirmDialog box = new ConfirmDialog(textInfo, textOKButton, textCloseButton, okDeleteClick);
			getUI().addWindow(box);
		});
		return deleteButton;
	}


	public void setUser(UserTemplate template) {
		currentUser = template;
		User user = identityService.createUserQuery().userId(currentUser.getId()).singleResult();
		groupForUser.refreshAll();
		groupUserIDFilter.setFilter(user.getId());
	}	
	
	private ComboBox<String> createSelectGroup(DataProvider<String, String> selectGroupProvider) {
		ComboBox<String> groupSelect = new ComboBox<>();
		groupSelect.setDataProvider(selectGroupProvider);
		groupSelect.setPageLength(5);
		groupSelect.addSelectionListener(event -> {
			selectGroupProvider.refreshAll();
			selectGroupFilter = selectGroupProvider.withConfigurableFilter();
		});
		groupSelect.addValueChangeListener(event -> {
			selectGroupProvider.refreshAll();
			selectGroupFilter.setFilter(groupSelect.getValue());
			selectGroupFilter = selectGroupProvider.withConfigurableFilter();
		});
		return groupSelect;
	}
	
	private Button createAddGroupButton() {
		Button createButton = new Button();
		createButton.setIcon(VaadinIcons.PLUS);
		createButton.addClickListener(event -> {
			String[] paramsForAdd = new String[] {selectGroup.getValue(), currentUser.getId()};
			try {
				identityService.createMembership(currentUser.getId(), selectGroup.getValue());
				groupForUser.refreshAll();
				String notificationTextAdd = getI18nText("adminView.notification.user.addGroup", paramsForAdd);
				Notification.show(notificationTextAdd, Type.WARNING_MESSAGE);
			}
			catch(Exception e) {
				String notificationTextErr = getI18nText("adminView.notification.user.addGroupErr", paramsForAdd);
				Notification.show(notificationTextErr, Type.WARNING_MESSAGE);
			}
		});
		return createButton;
	}
	
	private Button createEditButton() {
		String nameEditButton = getI18nText("adminView.caption.edit");
		Button edit = new Button(nameEditButton);
		edit.setStyleName(ValoTheme.BUTTON_PRIMARY);
		edit.addClickListener(event ->{
			joinedComponent.setVisible(true);
			joinedComponent.setEnabled(true);
			viewButton.setVisible(true);
			viewButton.setEnabled(true);
			edit.setVisible(false);
			edit.setEnabled(false);
			gridGroupAddUser.getColumn("del").setHidden(false);
		});
		return edit;
	}
	
	private Button createViewButton() {
		String nameViewButton = getI18nText("adminView.caption.view");
		Button view = new Button(nameViewButton);
		view.addClickListener(event ->{
			joinedComponent.setVisible(false);
			joinedComponent.setEnabled(false);
			view.setVisible(false);
			view.setEnabled(false);
			editButton.setVisible(true);
			editButton.setEnabled(true);
			gridGroupAddUser.getColumn("del").setHidden(true);
		});
		return view;
	}
	
	private Grid<GroupTemplate> createGroupAddUser() {
		String deleteCaption = getI18nText("adminView.caption.delete");
		String idCaption = getI18nText("adminView.caption.id");
		String nameCaption = getI18nText("adminView.caption.nameRoles");
		String typeCaption = getI18nText("adminView.caption.typeRoles");
		Grid<GroupTemplate> grid = new Grid<>();
		groupUserIDFilter = groupForUser.withConfigurableFilter();
		grid.setSizeFull();
		grid.addColumn(GroupTemplate::getId).setCaption(idCaption);
		grid.addColumn(GroupTemplate::getName).setCaption(nameCaption);
		grid.addColumn(GroupTemplate::getType).setCaption(typeCaption);
		grid.addComponentColumn(GroupTemplate::getDelete).setHidden(true).setId("del").setCaption(deleteCaption).setWidth(95.0);
		grid.setWidth(100, Unit.PERCENTAGE);
		grid.setDataProvider(groupUserIDFilter);
		return grid;
	}
	
	private String getI18nText(String key) {
		return messageSource.getMessage(key, null, VaadinSession.getCurrent().getLocale());
	}
	
	private String getI18nText(String key, String[] params) {
		return messageSource.getMessage(key, params, VaadinSession.getCurrent().getLocale());
	}
}