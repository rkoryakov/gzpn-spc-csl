package ru.gzpn.spc.csl.services.bl.interfaces;

import ru.gzpn.spc.csl.model.jsontypes.ISettingsJson;

public interface IUserSettigsService {

	public ISettingsJson getCreateDocUserSettings(String userId);
	ISettingsJson getCreateDocUserSettings(String userId, ISettingsJson defaultValue);
	
	public void save(String userId, ISettingsJson createDoc);
	
	public String getCurrentUser();
	ISettingsJson getContracrRegSettings(String userId, ISettingsJson defaultValue);
	ISettingsJson getContracrRegSettings(String userId);
	
	ISettingsJson getEstimatesRegSettings(String userId, ISettingsJson defaultValue);
	ISettingsJson getEstimatesRegSettings(String userId);
	
	ISettingsJson getSummaryEstimateCardSettings(String userId, ISettingsJson defaultValue);
	ISettingsJson getSummaryEstimateCardSettings(String userId);
	
	ISettingsJson getLocalEstimatesApprovalSettings(String userId, ISettingsJson defaultValue);
	ISettingsJson getLocalEstimatesApprovalSettings(String userId);
}
