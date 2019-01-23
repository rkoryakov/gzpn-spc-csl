package ru.gzpn.spc.csl.model.enums;

import java.util.Locale;

import org.springframework.context.MessageSource;

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

	public String getI18n() {
		return i18n;
	}
	
	public String getText(MessageSource source, Locale locale) {
		return source.getMessage(i18n, null, locale);
	}
}
