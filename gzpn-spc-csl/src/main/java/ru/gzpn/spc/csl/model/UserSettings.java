package ru.gzpn.spc.csl.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import ru.gzpn.spc.csl.model.interfaces.IUserSettings;
import ru.gzpn.spc.csl.model.jsontypes.UserSettingsJson;

@Entity
@NamedQueries({
	@NamedQuery(name = "UserSettings.findByUserId", query = "SELECT u FROM UserSettings u WHERE u.userId = ?1")
})
@Table(schema = "spc_csl_schema", name = "user_settings", indexes = {
		@Index(name = "spc_csl_idx_ussid", columnList = "userId", unique = true)
})
public class UserSettings extends BaseEntity implements IUserSettings, Serializable {
	private static final long serialVersionUID = 3134589263886817266L;

	private String userId;
	
	@Column
	@Type(type="UserSettingsJsonType")
	private UserSettingsJson createDocSettingsJson;
	
	public UserSettings() {
	}
	
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public UserSettingsJson getDocSettingsJson() {
		return createDocSettingsJson;
	}

	public void setDocSettingsJson(UserSettingsJson docSettingsJson) {
		this.createDocSettingsJson = docSettingsJson;
	}
}
