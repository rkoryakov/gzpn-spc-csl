package ru.gzpn.spc.csl.model.enums;

import java.util.Locale;

import org.springframework.context.MessageSource;

public enum PriceLevel {
	LEVEL_200("ru.gzpn.spc.csl.model.enums.PriceLevel.level_2000"),
	CURRENT("ru.gzpn.spc.csl.model.enums.PriceLevel.current"),
	CONTRACT("ru.gzpn.spc.csl.model.enums.PriceLevel.contract");
	
	private String i18n;
	
	private PriceLevel(String i18n) {
		this.i18n = i18n;
	}

	public String getI18n() {
		return i18n;
	}
	
	public String getText(MessageSource source, Locale locale) {
		return source.getMessage(i18n, null, locale);
	}
}
