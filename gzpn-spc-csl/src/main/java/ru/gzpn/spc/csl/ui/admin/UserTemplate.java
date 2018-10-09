package ru.gzpn.spc.csl.ui.admin;

import org.activiti.engine.identity.User;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;

public class UserTemplate implements User {
	private String id;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private Button edit;
	private Button delete;
	
	public UserTemplate() {
		super();
	}
	
	public Button getEdit() {
		edit.setSizeFull();
		edit.setIcon(VaadinIcons.EDIT);
		return edit;
	}
	public void setEdit(Button edit) {
		this.edit = edit;
	}
	public Button getDelete() {
		delete.setSizeFull();
		delete.setIcon(VaadinIcons.TRASH);
		return delete;
	}
	public void setDelete(Button delete) {
		this.delete = delete;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public boolean isPictureSet() {
		return false;
	}

	
}
