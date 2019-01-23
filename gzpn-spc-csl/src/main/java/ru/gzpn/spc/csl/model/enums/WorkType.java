package ru.gzpn.spc.csl.model.enums;

import java.util.Locale;

import org.springframework.context.MessageSource;

public enum WorkType {
	
	PIR("ru.gzpn.spc.csl.model.enums.WorkType.pir"),
	SMR("ru.gzpn.spc.csl.model.enums.WorkType.smr");
	
	private String i18n;
	
	private WorkType(String i18n) {
		this.i18n = i18n;
	}

	public String getI18n() {
		return i18n;
	}
	
	public String getText(MessageSource source, Locale locale) {
		return source.getMessage(i18n, null, locale);
	}
}
