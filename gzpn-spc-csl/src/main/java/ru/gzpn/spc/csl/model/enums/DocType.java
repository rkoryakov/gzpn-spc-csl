package ru.gzpn.spc.csl.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;

public enum DocType {
	LOCAL_ESTIMATE("ru.gzpn.spc.csl.model.enums.DocType.local_estimate"),
	SET_OF_DRAWINGS("ru.gzpn.spc.csl.model.enums.DocType.set_of_drawings");
	
	private String i18n;
	private String i18Value;
	
	private static final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
	static {
		messageSource.setBasename("i18n/captions");
	}
	
	private DocType(String i18n) {
		this.i18n = i18n;
	}

	public String getI18n() {
		return i18n;
	}
	
	public String getText() {
		i18Value = messageSource.getMessage(i18n, null, LocaleContextHolder.getLocale());
		return i18Value;
	}
	
	public static List<String> getAllTexts() {
		return Arrays.asList(values())
					.stream().map(item -> messageSource.getMessage(item.getI18n(), null, LocaleContextHolder.getLocale()))
						.collect(Collectors.toList());
	}
	
	public static List<DocType> getAll() {
		return Arrays.asList(values())
					.stream().map(item -> (DocType)item)
						.collect(Collectors.toList());
	}
	
	@Override
	public String toString() {
		return getText();
	}
}
