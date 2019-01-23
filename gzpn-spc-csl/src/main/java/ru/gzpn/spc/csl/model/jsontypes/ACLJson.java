package ru.gzpn.spc.csl.model.jsontypes;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class ACLJson implements Serializable {
	private static final long serialVersionUID = -5207546411091576809L;
	
	Set<String> roles;
//	Set<String> editRoles;

	public Set<String> getRoles() {
		if(roles == null) {
			roles = new HashSet<>();
		}
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

//	public Set<String> getEditRoles() {
//		return editRoles;
//	}
//
//	public void setEditRoles(Set<String> editRoles) {
//		this.editRoles = editRoles;
//	}
}
