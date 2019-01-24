package ru.gzpn.spc.csl.model.enums;

import java.util.Locale;

import org.springframework.context.MessageSource;

import ru.gzpn.spc.csl.model.BaseEntity;
import ru.gzpn.spc.csl.model.CProject;
import ru.gzpn.spc.csl.model.Contract;
import ru.gzpn.spc.csl.model.Document;
import ru.gzpn.spc.csl.model.EstimateCalculation;
import ru.gzpn.spc.csl.model.EstimateCost;
import ru.gzpn.spc.csl.model.EstimateHead;
import ru.gzpn.spc.csl.model.HProject;
import ru.gzpn.spc.csl.model.LocalEstimate;
import ru.gzpn.spc.csl.model.LocalEstimateHistory;
import ru.gzpn.spc.csl.model.Milestone;
import ru.gzpn.spc.csl.model.ObjectEstimate;
import ru.gzpn.spc.csl.model.Phase;
import ru.gzpn.spc.csl.model.PlanObject;
import ru.gzpn.spc.csl.model.Stage;
import ru.gzpn.spc.csl.model.UserSettings;
import ru.gzpn.spc.csl.model.Work;
import ru.gzpn.spc.csl.model.WorkSet;

public enum Entities {
	HPROJECT("HProject"),
	CPROJECT("CProject"),
	PHASE("Phase"),
	STAGE("Stage"),
	PLANOBJECT("PlanObject"),
	WORK("Work"),
	LOCALESTIMATE("LocalEstimate"),
	CONTRACT("Contract"),
	DOCUMENT("Document"),
	ESTIMATECALCULATION("EstimateCalculation"),
	ESTIMATECOST("EstimateCost"),
	ESTIMATEHEAD("EstimateHead"),
	MILESTONE("Milestone"),
	OBJECTESTIMATE("ObjectEstimate"),
	WORKSET("WorkSet"),
	
	USERSETTINGS("UserSettings"),
	LOCALESTIMATEHISTORY("LocalEstimateHistory");
	
	private static final String ENTITIES_PREFIX = "Entities.";
	private String name;
	private String i18n;
	
	Entities(String name) {
		this.name = name;
		this.i18n = ENTITIES_PREFIX + name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getI18n() {
		return i18n;
	}
	
	public String getEntityText(MessageSource source, Locale locale) {
		return source.getMessage(i18n, null, locale);
	}
	
	public static String getEntityFieldText(String field, MessageSource source, Locale locale) {
		return source.getMessage(ENTITIES_PREFIX + field, null, field, locale);
	}
	
	@SuppressWarnings("unchecked")
	public static Class<BaseEntity> getEntityClass(String name) {
		@SuppressWarnings("rawtypes")
		Class result = null;
		
		switch (Entities.valueOf(name.toUpperCase())) {
		case CONTRACT:
			result = Contract.class;
			break;
		case CPROJECT:
			result = CProject.class;
			break;
		case DOCUMENT:
			result = Document.class;
			break;
		case ESTIMATECALCULATION:
			result = EstimateCalculation.class;
			break;
		case ESTIMATECOST:
			result = EstimateCost.class;
			break;
		case ESTIMATEHEAD:
			result = EstimateHead.class;
			break;
		case HPROJECT:
			result = HProject.class;
			break;
		case LOCALESTIMATE:
			result = LocalEstimate.class;
			break;
		case LOCALESTIMATEHISTORY:
			result = LocalEstimateHistory.class;
			break;
		case MILESTONE:
			result = Milestone.class;
			break;
		case OBJECTESTIMATE:
			result = ObjectEstimate.class;
			break;
		case PHASE:
			result = Phase.class;
			break;
		case PLANOBJECT:
			result = PlanObject.class;
			break;
		case STAGE:
			result = Stage.class;
			break;
		case USERSETTINGS:
			result = UserSettings.class;
			break;
		case WORK:
			result = Work.class;
			break;
		case WORKSET:
			result = WorkSet.class;
			break;
		default:
			break;
		}
		
		return result;
	}
}
