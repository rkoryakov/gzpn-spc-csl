package ru.gzpn.spc.csl.ui.admin;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.User;
import org.springframework.context.MessageSource;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class UserInfoTabSheet extends TabSheet {
	
	private UserInfoTab userInfoTab;
	private VerticalLayout userGroupTab;
	private MessageSource messageSource;
	private IdentityService identityService;

	public UserInfoTabSheet(IdentityService identityService, MessageSource messageSource, ClickListener updateClick) {
		this.messageSource = messageSource;
		this.identityService = identityService;
		userInfoTab = new UserInfoTab(identityService, messageSource, updateClick);
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
	private final TextField newPasswordField;
	private Button saveButton;
	private Button editButton;
	private Button deleteButton;
	private MessageSource messageSource;
	private IdentityService identityService;
	private ClickListener listener;
	
	public UserInfoTab(IdentityService identityService, MessageSource messageSource, ClickListener updateClick) {
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
		newPasswordField = new TextField(newPasswordCaption, "");
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
			if (user == null) {
				user = identityService.newUser(loginField.getValue());
				user.setFirstName(firstNameField.getValue());
				user.setLastName(lastNameField.getValue());
				user.setEmail(emailField.getValue());
				user.setPassword(newPasswordField.getValue());
				identityService.saveUser(user);
				Notification.show(userWindowCaption.concat(" ").concat(user.getId()).concat(notificationCreated), Type.TRAY_NOTIFICATION);
				
			} else if (user.getId() != null) {
				user.setFirstName(firstNameField.getValue());
				user.setLastName(lastNameField.getValue());
				user.setEmail(emailField.getValue());
				user.setPassword(newPasswordField.getValue());
				identityService.saveUser(user);
				Notification.show(userWindowCaption.concat(" ").concat(user.getId()).concat(notificationChanged), Type.TRAY_NOTIFICATION);
			}
			
			saveButton.setEnabled(false);
			editButton.setEnabled(true);
		});
		saveButton.addClickListener(updateClick);
		
		editButton.addClickListener(event -> {
			loginField.setStyleName(ValoTheme.TEXTAREA_BORDERLESS, false);
			loginField.setReadOnly(false);
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
		editButton.addClickListener(updateClick);
	}
	
	public void setData(UserTemplate template) {
		loginField.setValue(template.getId());
		firstNameField.setValue(template.getFirstName() == null ? "" : template.getFirstName());
		lastNameField.setValue(template.getLastName() == null ? "" : template.getLastName());
		emailField.setValue(template.getEmail() == null ? "" : template.getEmail());
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
