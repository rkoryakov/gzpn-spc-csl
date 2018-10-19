package ru.gzpn.spc.csl.model.interfaces;

import java.util.Map;

public interface IUserSettings {
	public Map<String, String> getSettings();
	public void setSettings(Map<String, String> settings);
}
