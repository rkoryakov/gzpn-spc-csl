package ru.gzpn.spc.csl.model.jsontypes;

public class UserSettingsJsonType extends BaseJsonType {
	@Override
	public Class<?> returnedClass() {
		return UserSettingsJson.class;
	}
}
