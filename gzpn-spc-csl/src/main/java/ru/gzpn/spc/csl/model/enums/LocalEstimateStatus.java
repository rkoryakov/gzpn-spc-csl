package ru.gzpn.spc.csl.model.enums;

public enum LocalEstimateStatus {
	IN_WORK("ru.gzpn.spc.csl.model.enums.LocalEstimateStatus.in_work"),
	IN_COORDINATION("ru.gzpn.spc.csl.model.enums.LocalEstimateStatus.in_coordination"),
	HAS_REMARKS("ru.gzpn.spc.csl.model.enums.LocalEstimateStatus.has_remarks"),
	APPROVED("ru.gzpn.spc.csl.model.enums.LocalEstimateStatus.approved");
	
	private String i18nKey;
	
	private LocalEstimateStatus(String i18key) {
		this.i18nKey = i18key;
	}
}
