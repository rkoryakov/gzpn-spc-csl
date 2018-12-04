package ru.gzpn.spc.csl.model.enums;

public enum TaxType {
	NOT_INCLUDING_18_PERC(18, "ru.gzpn.spc.csl.model.enums.TaxType.NOT_INCLUDING_18_PERC"),
	INCLUDING_18_PERC(18, "ru.gzpn.spc.csl.model.enums.TaxType.INCLUDING_18_PERC"),
	NO(0, "ru.gzpn.spc.csl.model.enums.TaxType.NO");
	
	private String i18n;
	private float percent;
	
	private TaxType(float percent, String i18n) {
		this.i18n = i18n;
		this.percent = percent;
	}
}
