package ru.gzpn.spc.csl.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import ru.gzpn.spc.csl.model.interfaces.IUserSettings;
import ru.gzpn.spc.csl.model.jsontypes.ContractsRegSettingsJson;
import ru.gzpn.spc.csl.model.jsontypes.CreateDocSettingsJson;
import ru.gzpn.spc.csl.model.jsontypes.EstimatesRegSettingsJson;

@Entity
@Table(schema = "spc_csl_schema", name = "user_settings", indexes = {
		@Index(name = "spc_csl_idx_ussid", columnList = "userId", unique = true)
})
public class UserSettings extends BaseEntity implements IUserSettings, Serializable {
	private static final long serialVersionUID = 3134589263886817266L;

	private String userId;
	
	@Column
	@Type(type="CreateDocSettingsJsonType")
	private CreateDocSettingsJson createDocSettingsJson;
	
	@Column
	@Type(type="ContractsRegSettingsJsonType")
	private ContractsRegSettingsJson contractsRegSettingsJson;
	
	@Column
	@Type(type="EstimatesRegSettingsJsonType")
	private EstimatesRegSettingsJson estimatesRegSettingsJson;
	
	public UserSettings() {
	}
	
	@Override
	public String getUserId() {
		return userId;
	}
	
	@Override
	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public CreateDocSettingsJson getCreateDocSettingsJson() {
		return createDocSettingsJson;
	}

	@Override
	public void setCreateDocSettingsJson(CreateDocSettingsJson createDocSettingsJson) {
		this.createDocSettingsJson = createDocSettingsJson;
	}

	@Override
	public ContractsRegSettingsJson getContractsRegSettingsJson() {
		return contractsRegSettingsJson;
	}

	@Override
	public void setContractsRegSettingsJson(ContractsRegSettingsJson contractsRegSettingsJson) {
		this.contractsRegSettingsJson = contractsRegSettingsJson;
	}

	@Override
	public EstimatesRegSettingsJson getEstimatesRegSettingsJson() {
		return estimatesRegSettingsJson;
	}

	@Override
	public void setEstimatesRegSettingsJson(EstimatesRegSettingsJson estimatesRegSettingsJson) {
		estimatesRegSettingsJson = estimatesRegSettingsJson;
	}
}
