package ru.gzpn.spc.csl.model.enums;

public enum EstimateHeadType {
	LOCAL_ESTIMATE("ru.gzpn.spc.csl.model.enums.EstimateHeadType.LOCAL_ESTIMATE"),
	OBJECT_ESTIMATE("ru.gzpn.spc.csl.model.enums.EstimateHeadType.OBJECT_ESTIMATE");
	
	private String i18n;
	
	private EstimateHeadType(String i18n) {
		this.i18n = i18n;
	}
	
	public String getI18nKey() {
		return this.i18n;
	}
}
