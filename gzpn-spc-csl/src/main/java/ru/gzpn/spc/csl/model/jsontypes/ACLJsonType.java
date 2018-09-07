package ru.gzpn.spc.csl.model.jsontypes;

public class ACLJsonType extends BaseJsonType {
	@Override
	public Class<?> returnedClass() {
		return ACLJson.class;
	}
}
