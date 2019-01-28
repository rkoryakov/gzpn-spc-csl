package ru.gzpn.spc.csl.model.interfaces;

import ru.gzpn.spc.csl.model.jsontypes.ContractsRegSettingsJson;
import ru.gzpn.spc.csl.model.jsontypes.CreateDocSettingsJson;
import ru.gzpn.spc.csl.model.jsontypes.EstimatesRegSettingsJson;

public interface IUserSettings {

	String getUserId();

	void setUserId(String userId);

	CreateDocSettingsJson getCreateDocSettingsJson();

	void setCreateDocSettingsJson(CreateDocSettingsJson createDocSettingsJson);

	ContractsRegSettingsJson getContractsRegSettingsJson();

	void setContractsRegSettingsJson(ContractsRegSettingsJson contractsRegSettingsJson);

	EstimatesRegSettingsJson getEstimatesRegSettingsJson();

	void setEstimatesRegSettingsJson(EstimatesRegSettingsJson estimatesRegSettingsJson);
}
