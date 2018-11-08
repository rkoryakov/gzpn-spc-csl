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
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import ru.gzpn.spc.csl.ui.common.ConfirmDialog;

public class UserInfoTabSheet extends TabSheet {
	
	private UserInfoTab userInfoTab;
	private UserAddGroupTab userAddGroupTab;
	private MessageSource messageSource;
	private IdentityService identityService;
	private UsersAndRolesTab usersAndRolesTab;
	private UIContainer container;

	public UserInfoTabSheet(IdentityService identityService, 
			MessageSource messageSource, 
			DataProvider<UserTemplate, String> userDataProvider, 
			DataProvider<GroupTemplate, String> groupDataProvider, 
			UIContainer container) {
		
		this.messageSource = messageSource;
		this.identityService = identityService;
		this.container = container;
		userInfoTab = new UserInfoTab(identityService, messageSource, userDataProvider, container);
		userAddGroupTab = new UserAddGroupTab(identityService, messageSource, userDataProvider, groupDataProvider);
		this.addTab(userInfoTab, "info user");
		this.addTab(userAddGroupTab, "user group");
		userInfoTab.setUserAndRolesTab(usersAndRolesTab);
	}
	
	
	private VerticalLayout createuserGroupTab() {
		VerticalLayout userGroup = new VerticalLayout();
		return userGroup;
	}

	public UserInfoTab getUserInfoTab() {
		return userInfoTab;
	}
	
	public UserAddGroupTab getUserAddGroupTab() {
		return userAddGroupTab;
	}
	
	public void setUserAndRolesTab(UsersAndRolesTab tab) {
		usersAndRolesTab = tab;
	}
	
	private String getI18nText(String key) {
		return messageSource.getMessage(key, null, VaadinSession.getCurrent().getLocale());
	}
}

class UserInfoTab extends FormLayout{
	
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
	private UsersAndRolesTab usersAndRolesTab;
	private UIContainer container;
	
	public UserInfoTab(IdentityService identityService, 
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

		final HorizontalLayout buttonLayout = new HorizontalLayout();
		saveButton = createSaveButton(userDataProvider);
		editButton = createEditButton();
		cancelButton = createCancelButton(userDataProvider);
		deleteButton = createDeleteButton(userDataProvider);
		
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

		this.addComponent(loginField);
		this.addComponent(firstNameField);
		this.addComponent(lastNameField);
		this.addComponent(emailField);
		this.addComponent(newPasswordField);
		buttonLayout.addComponents(saveButton, editButton, cancelButton, deleteButton);
		buttonLayout.setMargin(false);
		this.addComponents(buttonLayout);
		
		formBinder = createBinder();		
			
	}
	
	private Binder<User> createBinder() {
		Binder<User> binder = new Binder<>();
		
		binder.forField(loginField).asRequired("Name may not be empty").bind(User::getId, User::setId);
		binder.forField(firstNameField).asRequired("Name may not be empty").bind(User::getFirstName, User::setFirstName);
		binder.forField(lastNameField).asRequired("Name may not be empty").bind(User::getLastName, User::setLastName);
		binder.forField(emailField).asRequired("Email may not be empty")
			.withValidator(new EmailValidator("Not a valid email address"))
			.bind(User::getEmail, User::setEmail);
		binder.forField(newPasswordField).asRequired("Password may not be empty")
            .withValidator(new StringLengthValidator("Password must be at least 7 characters long", 6, null))
            .bind((usrLocal) -> "", (usrLocal, pwd) -> {
            	usrLocal.setPassword(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(pwd));
            			});
		return binder;
	}

	private Button createEditButton() {
		String nameEditButton = getI18nText("adminView.caption.edit");
		editButton = new Button(nameEditButton);
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
			saveButton.setEnabled(true);
			editButton.setEnabled(false);
			deleteButton.setEnabled(false);
			cancelButton.setEnabled(true);
			cancelButton.setVisible(true);
			container.getCreateUserAndRolesButton().setEnabled(false);
		});
		return editButton;
	}
	
	private Button createSaveButton(DataProvider<UserTemplate, String> userDataProvider) {
		String nameSaveButton = getI18nText("adminView.button.nameSaveButton");
		String userWindowCaption = getI18nText("adminView.caption.userKey");
		String notificationChanged = getI18nText("adminView.notification.user.change");
		String notificationCreated = getI18nText("adminView.notification.user.created");
		
		saveButton = new Button(nameSaveButton);
		saveButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		saveButton.setEnabled(false);
		
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
			try {
			      if (user == null) {	
						user = identityService.newUser(loginField.getValue());
						Notification.show(userWindowCaption.concat(" ").concat(user.getId()).concat(notificationCreated), Type.TRAY_NOTIFICATION);
						
					} else if (user.getId() != null) {
						Notification.show(userWindowCaption.concat(" ").concat(user.getId()).concat(notificationChanged), Type.TRAY_NOTIFICATION);
					}
			      formBinder.writeBean(user);
			    } catch (ValidationException e) {
			      Notification.show("Person could not be saved, please check error");
			    }

			formBinder.setBean(user);
			
			identityService.saveUser(user);
			userDataProvider.refreshAll();
			saveButton.setEnabled(false);
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
			deleteButton.setEnabled(true);
			container.getCreateUserAndRolesButton().setEnabled(true);
		});	
		
		return cancelButton;
	}
	
	private Button createDeleteButton(DataProvider<UserTemplate, String> userDataProvider) {
		String notificationDeleted = getI18nText("adminView.notification.user.deleted");
		String textInfo = getI18nText("adminView.ConfirmDialog.deleteUser.info");
		String textOKButton = getI18nText("adminView.ConfirmDialog.deleteUser.ok");
		String textCloseButton = getI18nText("adminView.ConfirmDialog.deleteUser.close");
		String userWindowCaption = getI18nText("adminView.caption.userKey");
		String nameDeleteButton = getI18nText("adminView.caption.delete");
		
		deleteButton = new Button(nameDeleteButton);
		deleteButton.setStyleName(ValoTheme.BUTTON_DANGER);
		deleteButton.setIcon(VaadinIcons.TRASH);
		
		ClickListener okDeleteClick = event -> {
			User user = identityService.createUserQuery().userId(loginField.getValue()).singleResult();
			identityService.deleteUser(user.getId());
			userDataProvider.refreshAll();
			Notification.show(userWindowCaption.concat(" ").concat(user.getId()).concat(notificationDeleted), Type.WARNING_MESSAGE);
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
	
	public void setUserAndRolesTab(UsersAndRolesTab tab) {
		usersAndRolesTab = tab;
	}
	
	public UsersAndRolesTab getUsersAndRolesTab() {
		return usersAndRolesTab;
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
}

class UserAddGroupTab extends VerticalLayout{
	
	private ListSelect<String> infoGroupAddUser;
	private MessageSource messageSource;
	private IdentityService identityService;
	private HorizontalLayout headerHorizont;
	private ComboBox<GroupTemplate> selectGroup;
	private DataProvider<String, String> groupForUser = createDataProvider();
	private ConfigurableFilterDataProvider<String, Void, String> groupUserIDFilter;
	
	public UserAddGroupTab(IdentityService identityService, 
			MessageSource messageSource, 
			DataProvider<UserTemplate, String> userDataProvider, 
			DataProvider<GroupTemplate, String> groupDataProvider) {
		this.messageSource = messageSource;
		this.identityService = identityService;
		infoGroupAddUser = createListSelect();
		setHeight(100, Unit.PERCENTAGE);
		selectGroup = createSelectGroup(groupDataProvider);
		headerHorizont = new HorizontalLayout();
		headerHorizont.addComponent(selectGroup);
		addComponents(headerHorizont, infoGroupAddUser);
	}

	private DataProvider<String, String> createDataProvider() {
		return DataProvider.fromFilteringCallbacks(query -> {
			List<String> groupList = identityService.createGroupQuery().groupMember(query.getFilter().orElse("admin")).list().stream().
					map(Group :: getName).collect(Collectors.toList());
			return groupList.stream();
		}, query -> {
			return (int) identityService.createGroupQuery().groupMember(query.getFilter().orElse("admin")).count();
		});
	}
	

	public void setUser(UserTemplate template) {
		User user = identityService.createUserQuery().userId(template.getId()).singleResult();
		groupUserIDFilter.setFilter(user.getId());
		groupForUser.refreshAll();
	}	
	
	private ComboBox<GroupTemplate> createSelectGroup(DataProvider<GroupTemplate, String> groupDataProvider) {

		ComboBox<GroupTemplate> groupSelect = new ComboBox<>("Select your group");
		groupSelect.setDataProvider(groupDataProvider);
		return groupSelect;
	}
	
	private ListSelect<String> createListSelect() {
		groupUserIDFilter = groupForUser.withConfigurableFilter();
		ListSelect<String> list = new ListSelect<>("Присвоенные роли пользователю");
		list.setWidth(100, Unit.PERCENTAGE);
		list.setRows(5);
		list.setDataProvider(groupUserIDFilter);
		return list;
	}
	

}