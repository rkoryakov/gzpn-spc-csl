package ru.gzpn.spc.csl.model.interfaces;

import java.util.List;

import ru.gzpn.spc.csl.model.PlanObject;

public interface IPlanObject extends IBaseEntity {
	public static final String ENTITYNAME_DOT = "PlanObject.";
	
	public static final String FIELD_NAME = ENTITYNAME_DOT + "name";
	public static final String FIELD_CODE = ENTITYNAME_DOT + "code";
	public static final String FIELD_CPROJECT = ENTITYNAME_DOT + "cproject";
	/* Overridden fields */
	public static final String FIELD_ID = ENTITYNAME_DOT + IBaseEntity.FIELD_ID;
	public static final String FIELD_CHANGE_DATE = ENTITYNAME_DOT + IBaseEntity.FIELD_CHANGE_DATE;
	public static final String FIELD_CREATE_DATE = ENTITYNAME_DOT + IBaseEntity.FIELD_CREATE_DATE;
	public static final String FIELD_VERSION = ENTITYNAME_DOT + IBaseEntity.FIELD_VERSION;
	
	
	public String getCode();
	public void setCode(String code);
	
	public String getName();
	public void setName(String name);
	
	public List<IWork> getWorkList();
	public void setWorkList(List<IWork> workList);

	public List<IPlanObject> getChildren();
	public void setChildren(List<IPlanObject> children);

	public IPlanObject getParent();
	public void setParent(PlanObject parent);
	
	public ICProject getCproject();
	public void setCproject(ICProject cproject);

	public IMark getMark();
	public void setMark(IMark mark);

	public List<IWorkSet> getWorkset();
	public void setWorkset(List<IWorkSet> workset);
	
}
