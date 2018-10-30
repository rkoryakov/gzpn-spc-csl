package ru.gzpn.spc.csl.ui.admin;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.User;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
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
	private VerticalLayout userGroupTab;
	private MessageSource messageSource;
	private IdentityService identityService;

	public UserInfoTabSheet(IdentityService identityService, MessageSource messageSource, DataProvider<UserTemplate, String> userDataProvider) {
		this.messageSource = messageSource;
		this.identityService = identityService;
		userInfoTab = new UserInfoTab(identityService, messageSource, userDataProvider);
		userGroupTab = createuserGroupTab();
		this.addTab(userInfoTab, "info user");
		this.addTab(userGroupTab, "user group");
	}
		
	private VerticalLayout createuserGroupTab() {
		VerticalLayout userGroup = new VerticalLayout();
		return userGroup;
	}

	public UserInfoTab getUserInfoTab() {
		return userInfoTab;
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
	private Button deleteButton;
	private MessageSource messageSource;
	private IdentityService identityService;
	private ClickListener listener;
	
	public UserInfoTab(IdentityService identityService, MessageSource messageSource, DataProvider<UserTemplate, String> userDataProvider) {
		this.messageSource = messageSource;
		this.identityService = identityService;
		
		String userWindowCaption = getI18nText("adminView.caption.userKey");
		String loginCaption = getI18nText("adminView.caption.login");
		String firstNameCaption = getI18nText("adminView.caption.firstName");
		String lastNameCaption = getI18nText("adminView.caption.lastName");
		String emailCaption = getI18nText("adminView.caption.email");
		String newPasswordCaption = getI18nText("adminView.caption.newPasswordCaption");
		String passwordCaption = getI18nText("adminView.caption.passwordCaption");
		String nameSaveButton = getI18nText("adminView.button.nameSaveButton");
		String nameEditButton = getI18nText("adminView.caption.edit");
		String nameDeleteButton = getI18nText("adminView.caption.delete");
		String notificationChanged = getI18nText("adminView.notification.user.change");
		String notificationCreated = getI18nText("adminView.notification.user.created");
		
		this.setMargin(true);
		this.addStyleName("outlined");

		final HorizontalLayout buttonLayout = new HorizontalLayout();
		saveButton = new Button(nameSaveButton);
		saveButton.setEnabled(false);
		editButton = new Button(nameEditButton);
		deleteButton = new Button(nameDeleteButton);
		
		
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
		buttonLayout.addComponents(saveButton, editButton, deleteButton);
		buttonLayout.setMargin(false);
		this.addComponents(buttonLayout);
		
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
		
		
		editButton.addClickListener(event -> {
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
			
		});
		
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
			      binder.writeBean(user);
			    } catch (ValidationException e) {
			      Notification.show("Person could not be saved, " +
			        "please check error messages for each field.");
			    }

			binder.setBean(user);
			
			//
			identityService.saveUser(user);
			userDataProvider.refreshAll();
			saveButton.setEnabled(false);
			editButton.setEnabled(true);
		});
		
		String notificationDeleted = getI18nText("adminView.notification.user.deleted");
		String textInfo = getI18nText("adminView.ConfirmDialog.deleteUser.info");
		String textOKButton = getI18nText("adminView.ConfirmDialog.deleteUser.ok");
		String textCloseButton = getI18nText("adminView.ConfirmDialog.deleteUser.close");
		
		ClickListener okDeleteClick = event -> {
			User user = identityService.createUserQuery().userId(loginField.getValue()).singleResult();
			identityService.deleteUser(user.getId());
			userDataProvider.refreshAll();
			Notification.show(userWindowCaption.concat(" ").concat(user.getId()).concat(notificationDeleted), Type.WARNING_MESSAGE);
		};
		
		deleteButton.addClickListener(event -> {
			ConfirmDialog box = new ConfirmDialog(textInfo, textOKButton, textCloseButton, okDeleteClick);
			getUI().addWindow(box);
		});
		
		/*deleteButton.addClickListener(event -> {
			loginField.setValue("");
			firstNameField.setValue("");
			lastNameField.setValue("");
			emailField.setValue("");
			newPasswordField.setValue("");
		});*/
			
	}
	
	public void setData(UserTemplate template) {
		if(template == null) {
			loginField.setValue("");
			firstNameField.setValue("");
			lastNameField.setValue("");
			emailField.setValue("");
			newPasswordField.setValue("");
		}
		else {
			loginField.setValue(template.getId() == null ? "" : template.getId());
			firstNameField.setValue(template.getFirstName() == null ? "" : template.getFirstName());
			lastNameField.setValue(template.getLastName() == null ? "" : template.getLastName());
			emailField.setValue(template.getEmail() == null ? "" : template.getEmail());
			newPasswordField.setValue(template.getPassword() == null ? "" : template.getPassword());
		}
	}

	public Button getSaveButton() {
		return saveButton;
	}

	public void setSaveButton(Button saveButton) {
		this.saveButton = saveButton;
	}

	public Button getEditButton() {
		return editButton;
	}

	public void setEditButton(Button editButton) {
		this.editButton = editButton;
	}

	public Button getDeleteButton() {
		return deleteButton;
	}

	public void setDeleteButton(Button deleteButton) {
		this.deleteButton = deleteButton;
	}

	public TextField getLoginField() {
		return loginField;
	}

	public TextField getFirstNameField() {
		return firstNameField;
	}

	public TextField getLastNameField() {
		return lastNameField;
	}

	public TextField getEmailField() {
		return emailField;
	}

	public TextField getNewPasswordField() {
		return newPasswordField;
	}
	
	private String getI18nText(String key) {
		return messageSource.getMessage(key, null, VaadinSession.getCurrent().getLocale());
	}
	
}
