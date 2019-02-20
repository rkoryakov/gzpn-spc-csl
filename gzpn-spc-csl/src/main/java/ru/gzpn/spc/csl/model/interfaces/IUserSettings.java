package ru.gzpn.spc.csl.model.interfaces;

import ru.gzpn.spc.csl.model.jsontypes.ContractsRegSettingsJson;
import ru.gzpn.spc.csl.model.jsontypes.CreateDocSettingsJson;
import ru.gzpn.spc.csl.model.jsontypes.EstimateCalculationsRegSettingsJson;
import ru.gzpn.spc.csl.model.jsontypes.LocalEstimatesApprovalJson;
import ru.gzpn.spc.csl.model.jsontypes.SummaryEstimateCardSettingsJson;

public interface IUserSettings {

	String getUserId();

	void setUserId(String userId);

	CreateDocSettingsJson getCreateDocSettingsJson();

	void setCreateDocSettingsJson(CreateDocSettingsJson createDocSettingsJson);

	ContractsRegSettingsJson getContractsRegSettingsJson();

	void setContractsRegSettingsJson(ContractsRegSettingsJson contractsRegSettingsJson);

	EstimateCalculationsRegSettingsJson getEstimatesRegSettingsJson();

	void setEstimatesRegSettingsJson(EstimateCalculationsRegSettingsJson estimatesRegSettingsJson);

	SummaryEstimateCardSettingsJson getSummaryEstimateCardSettingsJson();

	void setSummaryEstimateCardSettingsJson(SummaryEstimateCardSettingsJson sumEstimatesSettingsJson);

	LocalEstimatesApprovalJson getLocalEstimatesApprovalJson();

	void setLocalEstimatesApprovalJson(LocalEstimatesApprovalJson localEstimatesApprovalJson);
}
