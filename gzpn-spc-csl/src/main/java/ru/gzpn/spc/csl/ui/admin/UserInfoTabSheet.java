package ru.gzpn.spc.csl.ui.admin;

import java.util.List;
import java.util.stream.Collectors;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.icons.VaadinIcons;
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

import ru.gzpn.spc.csl.ui.common.ConfirmDialogWindow;
import ru.gzpn.spc.csl.ui.common.I18n;
import ru.gzpn.spc.csl.ui.common.JoinedLayout;

@SuppressWarnings("serial")
public class UserInfoTabSheet extends TabSheet implements I18n {
	
	private UserInfoVerticalLayout userInfoVerticalLayout;
	private UserAddGroupVerticalLayout userAddGroupTab;
	private MessageSource messageSource;
	private IdentityService identityService;
	private UsersAndRolesVerticalLayout usersAndRolesTab;
	private UIContainer container;
	public static final String I18N_CAPTION_EDITINFOUSER = "adminView.caption.editInfoUser";
	public static final String I18N_CAPTION_EDITGROUPUSER = "adminView.caption.editGroupUser";

	public UserInfoTabSheet(IdentityService identityService, MessageSource messageSource, 
			DataProvider<UserTemplate, String> userDataProvider, UIContainer container) {
		this.messageSource = messageSource;
		this.identityService = identityService;
		this.container = container;
		userInfoVerticalLayout = new UserInfoVerticalLayout(identityService, messageSource, userDataProvider, container);
		userAddGroupTab = new UserAddGroupVerticalLayout(identityService, messageSource);
		this.addTab(userInfoVerticalLayout, getI18nText(I18N_CAPTION_EDITINFOUSER, messageSource));
		this.addTab(userAddGroupTab, getI18nText(I18N_CAPTION_EDITGROUPUSER, messageSource));
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
}

@SuppressWarnings("serial")
class UserInfoVerticalLayout extends VerticalLayout implements I18n {
	
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
	private Binder<User> userFormBinder;
	private UsersAndRolesVerticalLayout usersAndRolesVerticalLayout;
	private UIContainer container;
	public static final String I18N_CAPTION_PSW = "adminView.caption.passwordCaption";
	public static final String I18N_CAPTION_NEWPSW = "adminView.caption.newPasswordCaption";
	public static final String I18N_CONFIRMDIALOG_INFO_DELETEUSER = "adminView.ConfirmDialog.deleteUser.info";
	public static final String I18N_NECESSARY_USERLOGIN = "adminView.necessary.user.login";
	public static final String I18N_NECESSARY_USERFIRSTNAME = "adminView.necessary.user.firstName";
	public static final String I18N_NECESSARY_USERLASTNAME = "adminView.necessary.user.lastName";
	public static final String I18N_NECESSARY_USEREMAIL = "adminView.necessary.user.email";
	public static final String I18N_NECESSARY_USERPSW = "adminView.necessary.user.password";
	public static final String I18N_NECESSARY_EMAILVALID = "adminView.necessary.user.emailValid";
	public static final String I18N_NECESSARY_PSWVALID = "adminView.necessary.user.passwordValid";
	public static final String I18N_NOTIFICATION_USERCREATED = "adminView.notification.user.created";
	public static final String I18N_NOTIFICATION_USERCHANGED = "adminView.notification.user.change";
	public static final String I18N_NOTIFICATION_USERDELETED = "adminView.notification.user.deleted";
	
	public UserInfoVerticalLayout(IdentityService identityService, MessageSource messageSource, 
					DataProvider<UserTemplate, String> userDataProvider, UIContainer container) {
		this.messageSource = messageSource;
		this.identityService = identityService;
		this.container = container;
		this.setMargin(true);
		this.addStyleName("outlined");
		final HorizontalLayout topButtonLayout = new HorizontalLayout();
		final FormLayout infoLayout = new FormLayout();
		final HorizontalLayout buttonLayout = new HorizontalLayout();
		
		loginField = new TextField(getI18nText(UsersAndRolesVerticalLayout.I18N_CAPTION_LOGIN, messageSource), "");
		loginField.setStyleName(ValoTheme.TEXTAREA_BORDERLESS, true);
		loginField.setReadOnly(true);
		firstNameField = new TextField(getI18nText(UsersAndRolesVerticalLayout.I18N_CAPTION_FIRSTNAME, messageSource), "");
		firstNameField.setStyleName(ValoTheme.TEXTAREA_BORDERLESS, true);
		firstNameField.setReadOnly(true);
		lastNameField = new TextField(getI18nText(UsersAndRolesVerticalLayout.I18N_CAPTION_LASTNAME, messageSource), "");
		lastNameField.setStyleName(ValoTheme.TEXTAREA_BORDERLESS, true);
		lastNameField.setReadOnly(true);
		emailField = new TextField(getI18nText(UsersAndRolesVerticalLayout.I18N_CAPTION_EMAIL, messageSource), "");
		emailField.setStyleName(ValoTheme.TEXTAREA_BORDERLESS, true);
		emailField.setReadOnly(true);
		newPasswordField = new PasswordField(getI18nText(I18N_CAPTION_NEWPSW, messageSource), "");
		newPasswordField.setStyleName(ValoTheme.TEXTAREA_BORDERLESS, true);
		newPasswordField.setReadOnly(true);
		newPasswordField.setVisible(false);
		
		loginField.setWidth(90.0f, Unit.PERCENTAGE);
		firstNameField.setWidth(90.0f, Unit.PERCENTAGE);
		lastNameField.setWidth(90.0f, Unit.PERCENTAGE);
		emailField.setWidth(90.0f, Unit.PERCENTAGE);
		newPasswordField.setWidth(90.0f, Unit.PERCENTAGE);
		userFormBinder = createUserFormBinder();	
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
	
	private Binder<User> createUserFormBinder() {
		Binder<User> binder = new Binder<>();
		binder.forField(loginField).asRequired(getI18nText(I18N_NECESSARY_USERLOGIN, messageSource)).bind(User::getId, User::setId);
		binder.forField(firstNameField).asRequired(getI18nText(I18N_NECESSARY_USERFIRSTNAME, messageSource)).bind(User::getFirstName, User::setFirstName);
		binder.forField(lastNameField).asRequired(getI18nText(I18N_NECESSARY_USERLASTNAME, messageSource)).bind(User::getLastName, User::setLastName);
		binder.forField(emailField).asRequired(getI18nText(I18N_NECESSARY_USEREMAIL, messageSource))
			.withValidator(new EmailValidator(getI18nText(I18N_NECESSARY_EMAILVALID, messageSource))).bind(User::getEmail, User::setEmail);
		binder.forField(newPasswordField).asRequired(getI18nText(I18N_NECESSARY_USERPSW, messageSource))
            .withValidator(new StringLengthValidator(getI18nText(I18N_NECESSARY_PSWVALID, messageSource), 6, null))
            .bind((usrLocal) -> "", (usrLocal, pwd) -> usrLocal.setPassword(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(pwd)));
		return binder;
	}

	private Button createEditButton() {
		editButton = new Button(getI18nText(UsersAndRolesVerticalLayout.I18N_CAPTION_EDIT, messageSource));
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
			newPasswordField.setVisible(true);
			saveButton.setVisible(true);
			editButton.setEnabled(false);
			deleteButton.setEnabled(false);
			cancelButton.setEnabled(true);
			cancelButton.setVisible(true);
			container.getCreateUserAndRolesButton().setEnabled(false);
		});
		userFormBinder.addValueChangeListener(event -> saveButton.setEnabled(userFormBinder.validate().isOk()));
		return editButton;
	}
	
	private Button createSaveButton(DataProvider<UserTemplate, String> userDataProvider) {
		saveButton = new Button(getI18nText(UsersAndRolesVerticalLayout.I18N_CAPTION_BUTTON_SAVE, messageSource));
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
			
			newPasswordField.setVisible(false);
			User user = identityService.createUserQuery().userId(loginField.getValue()).singleResult();
			String[] paramsForSave;
			try {
			      if (user == null) {	
						user = identityService.newUser(loginField.getValue());
						paramsForSave = new String[] {user.getId()};
						String notificationCreated = getI18nText(I18N_NOTIFICATION_USERCREATED, paramsForSave, messageSource);
						Notification.show(notificationCreated, Type.TRAY_NOTIFICATION);
						
					} else if (user.getId() != null) {
						paramsForSave = new String[] {user.getId()};
						String notificationChanged = getI18nText(I18N_NOTIFICATION_USERCHANGED, paramsForSave, messageSource);
						Notification.show(notificationChanged, Type.TRAY_NOTIFICATION);
					}
			      userFormBinder.writeBean(user);
			    } catch (ValidationException e) {
			      paramsForSave = new String[] {user.getId()};
				  String notificationSaveErr = getI18nText(I18N_NOTIFICATION_USERCHANGED, paramsForSave, messageSource);
			      Notification.show(notificationSaveErr);
			    }
			userFormBinder.setBean(user);
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
		cancelButton = new Button(getI18nText(UsersAndRolesVerticalLayout.I18N_CAPTION_BUTTON_CLOSE, messageSource));
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
			    deleteButton.setEnabled(false);
			} else if (user.getId() != null) {
			    loginField.setValue(user.getId() == null ? "" : user.getId());
			    firstNameField.setValue(user.getFirstName() == null ? "" : user.getFirstName());
			    lastNameField.setValue(user.getLastName() == null ? "" : user.getLastName());
			    emailField.setValue(user.getEmail() == null ? "" : user.getEmail());
			    newPasswordField.setValue("");
			    deleteButton.setEnabled(true);
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
			newPasswordField.setVisible(false);
			userDataProvider.refreshAll();
			editButton.setEnabled(true);
			editButton.setVisible(true);
			cancelButton.setEnabled(false);
			cancelButton.setVisible(false);
			saveButton.setEnabled(false);
			saveButton.setVisible(false);
			container.getCreateUserAndRolesButton().setEnabled(true);
		});	
		
		return cancelButton;
	}
	
	private Button createDeleteButton(DataProvider<UserTemplate, String> userDataProvider) {
		deleteButton = new Button(getI18nText(UsersAndRolesVerticalLayout.I18N_CAPTION_DELETE, messageSource));
		deleteButton.setStyleName(ValoTheme.BUTTON_DANGER);
		deleteButton.setIcon(VaadinIcons.TRASH);
		ClickListener okDeleteClick = event -> {
			User user = identityService.createUserQuery().userId(loginField.getValue()).singleResult();
			identityService.deleteUser(user.getId());
			userDataProvider.refreshAll();
			String[] paramsForDelete = new String[] {user.getId()};
			String notificationDeleted = getI18nText(I18N_NOTIFICATION_USERDELETED, paramsForDelete, messageSource);
			Notification.show(notificationDeleted, Type.WARNING_MESSAGE);
			loginField.setValue("");
			firstNameField.setValue("");
			lastNameField.setValue("");
			emailField.setValue("");
			newPasswordField.setValue("");
		};
		deleteButton.addClickListener(event -> {
			ConfirmDialogWindow box = new ConfirmDialogWindow(getI18nText(I18N_CONFIRMDIALOG_INFO_DELETEUSER, messageSource), 
															  getI18nText(UsersAndRolesVerticalLayout.I18N_CAPTION_BUTTON_OK, messageSource), 
															  getI18nText(UsersAndRolesVerticalLayout.I18N_CAPTION_BUTTON_CLOSE, messageSource), 
															  okDeleteClick);
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
		
		if(template == null) {
			loginField.setValue("");
			firstNameField.setValue("");
			lastNameField.setValue("");
			emailField.setValue("");
			newPasswordField.setValue("");
			newPasswordField.setCaption(getI18nText(I18N_CAPTION_PSW, messageSource));
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
			newPasswordField.setVisible(true);
			saveButton.setVisible(true);
			editButton.setEnabled(false);
			editButton.setVisible(false);
			deleteButton.setEnabled(false);
			cancelButton.setEnabled(true);
			cancelButton.setVisible(true);
			container.getCreateUserAndRolesButton().setEnabled(false);
			userFormBinder.addValueChangeListener(event -> saveButton.setEnabled(userFormBinder.validate().isOk()));
		}
		else {
			loginField.setValue(template.getId() == null ? "" : template.getId());
			firstNameField.setValue(template.getFirstName() == null ? "" : template.getFirstName());
			lastNameField.setValue(template.getLastName() == null ? "" : template.getLastName());
			emailField.setValue(template.getEmail() == null ? "" : template.getEmail());
			newPasswordField.setValue("");
			newPasswordField.setCaption(getI18nText(I18N_CAPTION_NEWPSW, messageSource));
			deleteButton.setEnabled(true);
		}
	}	

}

@SuppressWarnings("serial")
class UserAddGroupVerticalLayout extends VerticalLayout implements I18n {
	
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
	public static final String I18N_NOTIFICATION_DELETEDFROMUSER = "adminView.notification.group.deletedFromUser";
	public static final String I18N_NOTIFICATION_USERADDGROUP = "adminView.notification.user.addGroup";
	public static final String I18N_NOTIFICATION_USERADDGROUPERR = "adminView.notification.user.addGroupErr";
	
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
					filter(group -> StringUtils.startsWithIgnoreCase(group.getId(), query.getFilter().orElse("")))
						.map(Group :: getId).collect(Collectors.toList());
			return groupList.stream();
		}, query -> identityService.createGroupQuery().list().stream().
					filter(group -> StringUtils.startsWithIgnoreCase(group.getId(), query.getFilter().orElse("")))
						.collect(Collectors.toList()).size());
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
		ClickListener okDeleteClick = event -> {
			identityService.deleteMembership(userID, group.getId());
			groupForUser.refreshAll();
			String[] paramsForDelete = new String[] {group.getId(), userID};
			String notificationDeleted = getI18nText(I18N_NOTIFICATION_DELETEDFROMUSER, paramsForDelete, messageSource);
			Notification.show(notificationDeleted, Type.WARNING_MESSAGE);
		};
		deleteButton.addClickListener(event -> {
			ConfirmDialogWindow box = new ConfirmDialogWindow(getI18nText(UsersAndRolesVerticalLayout.I18N_CONFIRMDIALOG_INFO_DELETEGROUP, messageSource), 
					 										  getI18nText(UsersAndRolesVerticalLayout.I18N_CAPTION_BUTTON_OK, messageSource), 
					 										  getI18nText(UsersAndRolesVerticalLayout.I18N_CAPTION_BUTTON_CLOSE, messageSource), 
					 										  okDeleteClick);
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
		groupSelect.setPlaceholder(getI18nText(UsersAndRolesVerticalLayout.I18N_SEARCHFIELD_PLACEHOLDER, messageSource));
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
				String notificationTextAdd = getI18nText(I18N_NOTIFICATION_USERADDGROUP, paramsForAdd, messageSource);
				Notification.show(notificationTextAdd, Type.WARNING_MESSAGE);
			}
			catch(Exception e) {
				String notificationTextErr = getI18nText(I18N_NOTIFICATION_USERADDGROUPERR, paramsForAdd, messageSource);
				Notification.show(notificationTextErr, Type.ERROR_MESSAGE);
			}
			selectGroup.setValue("");
		});
		return createButton;
	}
	
	private Button createEditButton() {
		Button edit = new Button(getI18nText(UsersAndRolesVerticalLayout.I18N_CAPTION_EDIT, messageSource));
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
		Button view = new Button(getI18nText(UsersAndRolesVerticalLayout.I18N_CAPTION_BUTTON_OK, messageSource));
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
		Grid<GroupTemplate> grid = new Grid<>();
		groupUserIDFilter = groupForUser.withConfigurableFilter();
		grid.setSizeFull();
		grid.addColumn(GroupTemplate::getId).setCaption(getI18nText(UsersAndRolesVerticalLayout.I18N_CAPTION_IDROLES, messageSource));
		grid.addColumn(GroupTemplate::getName).setCaption(getI18nText(UsersAndRolesVerticalLayout.I18N_CAPTION_NAMEROLES, messageSource));
		grid.addColumn(GroupTemplate::getType).setCaption(getI18nText(UsersAndRolesVerticalLayout.I18N_CAPTION_TYPEROLES, messageSource));
		grid.addComponentColumn(GroupTemplate::getDelete).setHidden(true).setId("del").setCaption(getI18nText(UsersAndRolesVerticalLayout.I18N_CAPTION_DELETE, messageSource)).setWidth(95.0);
		grid.setWidth(100, Unit.PERCENTAGE);
		grid.setDataProvider(groupUserIDFilter);
		return grid;
	}
	
}