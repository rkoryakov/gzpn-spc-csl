package ru.gzpn.spc.csl.ui.common;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public interface I18n {
	
	public default String getI18nText(String key, MessageSource messageSource) {
		return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
	}

	public default String getI18nText(String key, MessageSource messageSource, String defaultValue) {
		return messageSource.getMessage(key, null, defaultValue, LocaleContextHolder.getLocale());
	}
	
	public default String getI18nText(String key, Object[] args, MessageSource messageSource) {
		return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
	}

	public default Locale getCurrentLocale() {
		return LocaleContextHolder.getLocale();
	}
	
}
