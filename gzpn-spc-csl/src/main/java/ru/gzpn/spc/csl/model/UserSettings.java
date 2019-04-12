package ru.gzpn.spc.csl.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import ru.gzpn.spc.csl.model.interfaces.IUserSettings;
import ru.gzpn.spc.csl.model.jsontypes.ContractCardSettingsJson;
import ru.gzpn.spc.csl.model.jsontypes.ContractsRegSettingsJson;
import ru.gzpn.spc.csl.model.jsontypes.CreateDocSettingsJson;
import ru.gzpn.spc.csl.model.jsontypes.EstimateCalculationsRegSettingsJson;
import ru.gzpn.spc.csl.model.jsontypes.LocalEstimatesApprovalJson;
import ru.gzpn.spc.csl.model.jsontypes.SummaryEstimateCardSettingsJson;

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
	@Type(type="EstimateCalculationsRegSettingsJsonType")
	private EstimateCalculationsRegSettingsJson estimatesRegSettingsJson;
	
	@Column
	@Type(type="SummaryEstimateCardSettingsJsonType")
	private SummaryEstimateCardSettingsJson summaryEstimateCardSettingsJson;
	
	@Column
	@Type(type="LocalEstimatesApprovalJsonType")
	private LocalEstimatesApprovalJson localEstimatesApprovalJson;
	
	@Type(type = "ContractCardSettingsJsonType")
	private ContractCardSettingsJson cardSettingsJson;
	
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
	public EstimateCalculationsRegSettingsJson getEstimatesRegSettingsJson() {
		return estimatesRegSettingsJson;
	}

	@Override
	public void setEstimatesRegSettingsJson(EstimateCalculationsRegSettingsJson estimatesRegSettingsJson) {
		this.estimatesRegSettingsJson = estimatesRegSettingsJson;
	}

	@Override
	public SummaryEstimateCardSettingsJson getSummaryEstimateCardSettingsJson() {
		return summaryEstimateCardSettingsJson;
	}

	@Override
	public void setSummaryEstimateCardSettingsJson(SummaryEstimateCardSettingsJson sumEstimatesSettingsJson) {
		this.summaryEstimateCardSettingsJson = sumEstimatesSettingsJson;
	}

	@Override
	public LocalEstimatesApprovalJson getLocalEstimatesApprovalJson() {
		return localEstimatesApprovalJson;
	}

	@Override
	public void setLocalEstimatesApprovalJson(LocalEstimatesApprovalJson localEstimatesApprovalJson) {
		this.localEstimatesApprovalJson = localEstimatesApprovalJson;
	}

	@Override
	public ContractCardSettingsJson getContractCardSettingsJson() {
		return cardSettingsJson;
	}

	@Override
	public void setContractCardSettingsJson(ContractCardSettingsJson cardSettingsJson) {
		this.cardSettingsJson = cardSettingsJson;
	}
}
