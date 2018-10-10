package ru.gzpn.spc.csl.ui.admin;

import org.activiti.engine.identity.Group;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;

public class GroupTemplate implements Group {
	
	private String id;
	private String name;
	private String type;
	private Button edit;
	private Button delete;
	
	public GroupTemplate() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

}
