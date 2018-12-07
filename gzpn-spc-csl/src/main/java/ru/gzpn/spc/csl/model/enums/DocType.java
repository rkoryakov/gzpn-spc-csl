package ru.gzpn.spc.csl.model.enums;

public enum DocType {
	LOCAL_ESTIMATE("ru.gzpn.spc.csl.model.enums.DocType.local_estimate"),
	SET_OF_DRAWINGS("ru.gzpn.spc.csl.model.enums.DocType.set_of_drawings");
	
	private String i18n;
	
	private DocType(String i18n) {
		this.i18n = i18n;
	}

	public String getI18n() {
		return i18n;
	}
}
