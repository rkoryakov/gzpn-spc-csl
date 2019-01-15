package ru.gzpn.spc.csl.services.bl.interfaces;

import ru.gzpn.spc.csl.model.jsontypes.CreateDocSettingsJson;

public interface IDataUserSettigsService {
	public CreateDocSettingsJson getUserSettings();
	public CreateDocSettingsJson getUserSettings(String userId);
	public void save(String userId, CreateDocSettingsJson createDoc);
	public String getCurrentUser();
}
