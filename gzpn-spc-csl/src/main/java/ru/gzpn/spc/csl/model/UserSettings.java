package ru.gzpn.spc.csl.model;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import ru.gzpn.spc.csl.model.interfaces.IUserSettings;
import ru.gzpn.spc.csl.model.jsontypes.ProjectTreeSettingsJson;

@Entity
@Table(schema = "spc_csl_schema", name = "user_settings", indexes = {
		@Index(name = "spc_csl_idx_ussid", columnList = "userId", unique = true)
})
public class UserSettings extends BaseEntity implements IUserSettings {

	private String userId;

	@Column
	@Type(type="ProjectTreeSettingsJsonType")
	private ProjectTreeSettingsJson treeSettings;
	
	@Override
	public Map<String, String> getSettings() {
		return null;
	}

	@Override
	public void setSettings(Map<String, String> settings) {
		
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
