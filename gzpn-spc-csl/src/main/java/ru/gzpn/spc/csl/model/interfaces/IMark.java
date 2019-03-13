package ru.gzpn.spc.csl.model.interfaces;

import java.util.List;

public interface IMark {
	public static final String ENTITYNAME_DOT = "Mark.";
	
	public static final String FIELD_NAME = ENTITYNAME_DOT + "name";
	public static final String FIELD_CODE = ENTITYNAME_DOT + "code";
	
	/* Overridden fields */
	public static final String FIELD_ID = ENTITYNAME_DOT + IBaseEntity.FIELD_ID;
	public static final String FIELD_CHANGE_DATE = ENTITYNAME_DOT + IBaseEntity.FIELD_CHANGE_DATE;
	public static final String FIELD_CREATE_DATE = ENTITYNAME_DOT + IBaseEntity.FIELD_CREATE_DATE;
	public static final String FIELD_VERSION = ENTITYNAME_DOT + IBaseEntity.FIELD_VERSION;
	
	void setName(String name);
	String getName();
	
	List<IPlanObject> getPlanObjects();
	void setPlanObjects(List<IPlanObject> planObjects);
	String getCode();
	void setCode(String code);

}
