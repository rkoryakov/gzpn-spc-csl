package ru.gzpn.spc.csl.services.bl.interfaces;

import ru.gzpn.spc.csl.model.jsontypes.UserSettingsJson;

public interface IDataUserSettigsService {
	public UserSettingsJson getUserSettings();
	public UserSettingsJson getUserSettings(String userId);
	public void save(String userId, UserSettingsJson createDoc);
	public String getCurrentUser();
}
