package ru.gzpn.spc.csl.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.context.MessageSource;

public enum DocType {
	LOCAL_ESTIMATE("ru.gzpn.spc.csl.model.enums.DocType.local_estimate"),
	SET_OF_DRAWINGS("ru.gzpn.spc.csl.model.enums.DocType.set_of_drawings");
	
	private String i18n;
	private String i18Value;
	
	private DocType(String i18n) {
		this.i18n = i18n;
	}

	public String getI18n() {
		return i18n;
	}
	
	public static DocType getByText(String type) {
		DocType result = LOCAL_ESTIMATE;
		if (type.equals("Комплект черчежей")) {
			result = SET_OF_DRAWINGS;
		}
		
		return result;
	}
	
	public String getText(MessageSource source, Locale locale) {
		i18Value = source.getMessage(i18n, null, locale);
		return i18Value;
	}
	
	public static List<String> getAll(MessageSource source, Locale locale) {
		return Arrays.asList(values())
					.stream().map(item -> source.getMessage(item.getI18n(), null, locale))
						.collect(Collectors.toList());
	}
}
