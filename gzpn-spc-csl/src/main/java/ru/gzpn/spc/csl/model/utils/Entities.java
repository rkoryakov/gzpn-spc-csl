package ru.gzpn.spc.csl.model.utils;

import ru.gzpn.spc.csl.model.BaseEntity;
import ru.gzpn.spc.csl.model.CProject;
import ru.gzpn.spc.csl.model.HProject;
import ru.gzpn.spc.csl.model.Phase;
import ru.gzpn.spc.csl.model.Stage;
import ru.gzpn.spc.csl.model.UserSettings;

public enum Entities {
	HPROJECT("HProject"),
	CPROJECT("CProject"),
	PHASE("Phase"),
	STAGE("Stage"),
	USER_SETTINGS("UserSettings"),
	PLAN_OBJECT("PlanObject"),
	WORK("Work");
	
	private String name;
	
	Entities(String name) {
		this.name= name;
	}
	
	public String getName() {
		return this.name();
	}
	
	@SuppressWarnings("unchecked")
	public static Class<BaseEntity> getEntityClass(String name) {
		@SuppressWarnings("rawtypes")
		Class result = null;
		
		switch (Entities.valueOf(name.toUpperCase())) {
		case CPROJECT:
			result = CProject.class; 
			break;
		case HPROJECT:
			result = HProject.class;
			break;
		case PHASE:
			result = Phase.class;
			break;
		case STAGE:
			result = Stage.class;
			break;
		case USER_SETTINGS:
			result = UserSettings.class;
			break;
		default:
			break;
		}
		
		return result;
	}
}
