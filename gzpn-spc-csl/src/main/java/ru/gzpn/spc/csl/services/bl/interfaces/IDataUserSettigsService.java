package ru.gzpn.spc.csl.services.bl.interfaces;

import ru.gzpn.spc.csl.model.jsontypes.CreateDocSettingsJson;

public interface IDataUserSettigsService {
	public CreateDocSettingsJson getCreateDocSettings();
	public CreateDocSettingsJson getCreateDocSettings(String userId);
}
