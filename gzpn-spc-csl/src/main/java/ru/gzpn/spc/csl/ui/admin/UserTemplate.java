package ru.gzpn.spc.csl.ui.admin;

import org.activiti.engine.identity.User;

import com.vaadin.ui.Button;

public interface UserTemplate extends User {
	Button getButtonEdit();
	void setButtonEdit();
	Button getButtonDelete();
	void setButtonDelete();
}
