package ru.gzpn.spc.csl.services.bl.interfaces;

import ru.gzpn.spc.csl.model.jsontypes.ISettingsJson;

public interface IUserSettigsService {
	public ISettingsJson getUserSettings();
	public ISettingsJson getUserSettings(String userId);
	ISettingsJson getUserSettings(String userId, ISettingsJson defaultValue);
	
	public void save(String userId, ISettingsJson createDoc);
	public String getCurrentUser();
	
}
