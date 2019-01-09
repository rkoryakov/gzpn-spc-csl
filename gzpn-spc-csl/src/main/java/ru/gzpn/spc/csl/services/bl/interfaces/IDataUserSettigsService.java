package ru.gzpn.spc.csl.services.bl.interfaces;

import ru.gzpn.spc.csl.model.jsontypes.UserSettingsJson;

public interface IDataUserSettigsService {
	public UserSettingsJson getCreateDocSettings();
	public UserSettingsJson getCreateDocSettings(String userId);
	public void saveCreateDocSettingsJson(String userId, UserSettingsJson createDoc);
	public String getCurrentUser();
}
