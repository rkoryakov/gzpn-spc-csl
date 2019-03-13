package ru.gzpn.spc.csl.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;

public enum PriceLevel {
	LEVEL_200("ru.gzpn.spc.csl.model.enums.PriceLevel.level_2000"),
	CURRENT("ru.gzpn.spc.csl.model.enums.PriceLevel.current"),
	CONTRACT("ru.gzpn.spc.csl.model.enums.PriceLevel.contract");
	
	private String i18n;
	private String i18Value;
	
	private static final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
	static {
		messageSource.setBasename("i18n/captions");
	}
	
	private PriceLevel(String i18n) {
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
	
	public static List<PriceLevel> getAll() {
		return Arrays.asList(values())
					.stream().map(item -> (PriceLevel)item)
						.collect(Collectors.toList());
	}
	
	public static PriceLevel getByText(String caption) {
		if (caption == null) {
			return null;
		}
		return getAll().stream().filter(item -> item.getText().toLowerCase().equals(caption.toLowerCase())).findFirst().get();
	}
	
	@Override
	public String toString() {
		return getText();
	}
}
