package ru.gzpn.spc.csl.model.utils;

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
	ESTIMATE_CALCULATION("EstimateCalculation"),
	ESTIMATE_COST("EstimateCost"),
	ESTIMATE_HEAD("EstimateHead"),
	MILESTONE("Milestone"),
	OBJECT_ESTIMATE("ObjectEstimate"),
	WORK_SET("WorkSet"),
	
	USER_SETTINGS("UserSettings"),
	LOCAL_ESTIMATE_HISTORY("LocalEstimateHistory");
	private String name;
	
	Entities(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
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
		case ESTIMATE_CALCULATION:
			result = EstimateCalculation.class;
			break;
		case ESTIMATE_COST:
			result = EstimateCost.class;
			break;
		case ESTIMATE_HEAD:
			result = EstimateHead.class;
			break;
		case HPROJECT:
			result = HProject.class;
			break;
		case LOCALESTIMATE:
			result = LocalEstimate.class;
			break;
		case LOCAL_ESTIMATE_HISTORY:
			result = LocalEstimateHistory.class;
			break;
		case MILESTONE:
			result = Milestone.class;
			break;
		case OBJECT_ESTIMATE:
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
		case USER_SETTINGS:
			result = UserSettings.class;
			break;
		case WORK:
			result = Work.class;
			break;
		case WORK_SET:
			result = WorkSet.class;
			break;
		default:
			break;
		}
		
		return result;
	}
}
