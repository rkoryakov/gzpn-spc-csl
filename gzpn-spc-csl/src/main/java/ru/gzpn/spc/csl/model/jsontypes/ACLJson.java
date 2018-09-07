package ru.gzpn.spc.csl.model.jsontypes;

import java.io.Serializable;
import java.util.List;

public class ACLJson implements Serializable {
	List<String> readOnlyRoles;
	List<String> editRoles;

	public List<String> getReadOnlyRoles() {
		return readOnlyRoles;
	}

	public void setReadOnlyRoles(List<String> readOnlyRoles) {
		this.readOnlyRoles = readOnlyRoles;
	}

	public List<String> getEditRoles() {
		return editRoles;
	}

	public void setEditRoles(List<String> editRoles) {
		this.editRoles = editRoles;
	}

}
