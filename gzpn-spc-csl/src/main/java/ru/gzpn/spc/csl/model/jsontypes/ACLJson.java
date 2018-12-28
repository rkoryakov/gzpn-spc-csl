package ru.gzpn.spc.csl.model.jsontypes;

import java.io.Serializable;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class ACLJson implements Serializable {
	private static final long serialVersionUID = -5207546411091576809L;
	
	Set<String> viewRoles;
	Set<String> editRoles;

	public Set<String> getReadOnlyRoles() {
		return viewRoles;
	}

	public void setReadOnlyRoles(Set<String> readOnlyRoles) {
		this.viewRoles = readOnlyRoles;
	}

	public Set<String> getEditRoles() {
		return editRoles;
	}

	public void setEditRoles(Set<String> editRoles) {
		this.editRoles = editRoles;
	}

}
