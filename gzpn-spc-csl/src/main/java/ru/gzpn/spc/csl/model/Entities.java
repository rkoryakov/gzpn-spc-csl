package ru.gzpn.spc.csl.model;

public enum Entities {
	HPROJECT("HProject"),
	CPROJECT("CProject"),
	PHASE("Phase"),
	STAGE("Stage"),
	USER_SETTINGS("UserSettings");
	
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
