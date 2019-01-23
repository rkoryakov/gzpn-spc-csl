package ru.gzpn.spc.csl.model.interfaces;

import java.util.List;

import ru.gzpn.spc.csl.model.enums.DocType;

public interface IDocument extends IBaseEntity {
	public static final String ENTITYNAME_DOT = "document.";
	
	public static final String FIELD_NAME = ENTITYNAME_DOT + "name";
	public static final String FIELD_CODE = ENTITYNAME_DOT + "code";
	public static final String FIELD_TYPE = ENTITYNAME_DOT + "type";
	public static final String FIELD_WORKSET = ENTITYNAME_DOT + "workset";
	public static final String FIELD_WORK = ENTITYNAME_DOT + "work";
	/* Overridden fields */
	public static final String FIELD_ID = ENTITYNAME_DOT + IBaseEntity.FIELD_ID;
	public static final String FIELD_CHANGE_DATE = ENTITYNAME_DOT + IBaseEntity.FIELD_CHANGE_DATE;
	public static final String FIELD_CREATE_DATE = ENTITYNAME_DOT + IBaseEntity.FIELD_CREATE_DATE;
	public static final String FIELD_VERSION = ENTITYNAME_DOT + IBaseEntity.FIELD_VERSION;
	
	String getCode();
	void setCode(String code);
	DocType getType();
	void setType(DocType type);
	String getName();
	void setName(String name);
	List<ILocalEstimate> getLocalEstimates();
	void setLocalEstimates(List<ILocalEstimate> localEstimates);
	IWork getWork();
	void setWork(IWork work);
	void setWorkset(IWorkSet workset);
	IWorkSet getWorkset();
}
