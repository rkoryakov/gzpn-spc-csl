package ru.gzpn.spc.csl.model.interfaces;

import java.util.List;

public interface IWorkSet extends IBaseEntity {
	public static final String ENTITYNAME_DOT = "workset.";
	
	public static final String FIELD_NAME = ENTITYNAME_DOT + "name";
	public static final String FIELD_CODE = ENTITYNAME_DOT + "code";
	public static final String FIELD_PIR = ENTITYNAME_DOT + "pir";
	public static final String FIELD_SMR = ENTITYNAME_DOT + "smr";
	public static final String FIELD_PLAN_OBJECT = ENTITYNAME_DOT + "planObject";
	/* Overridden fields */
	public static final String FIELD_ID = ENTITYNAME_DOT + IBaseEntity.FIELD_ID;
	public static final String FIELD_CHANGE_DATE = ENTITYNAME_DOT + IBaseEntity.FIELD_CHANGE_DATE;
	public static final String FIELD_CREATE_DATE = ENTITYNAME_DOT + IBaseEntity.FIELD_CREATE_DATE;
	public static final String FIELD_VERSION = ENTITYNAME_DOT + IBaseEntity.FIELD_VERSION;
	
	public String getName();
	public void setName(String name);
	public String getCode();
	public void setCode(String code);
	public IWork getPir();
	public void setPir(IWork pir);
	public IWork getSmr();
	public void setSmr(IWork smr);
	public IPlanObject getPlanObject();
	public void setPlanObject(IPlanObject planObject);
	List<IDocument> getDocuments();
	void setDocuments(List<IDocument> documents);
}
