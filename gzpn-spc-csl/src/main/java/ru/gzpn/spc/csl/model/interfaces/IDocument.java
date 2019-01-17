package ru.gzpn.spc.csl.model.interfaces;

import java.util.List;

import ru.gzpn.spc.csl.model.enums.DocType;

public interface IDocument {
	public static final String ENTITYNAME_DOT = "workset.";
	
	public static final String FIELD_NAME = ENTITYNAME_DOT + "name";
	public static final String FIELD_CODE = ENTITYNAME_DOT + "code";
	public static final String FIELD_TYPE = ENTITYNAME_DOT + "type";
	
	String getCode();
	void setCode(String code);
	DocType getType();
	void setType(DocType type);
	String getName();
	void setName(String name);
	List<ILocalEstimate> getLocalEstimates();
	void setLocalEstimate(List<ILocalEstimate> localEstimates);
	IWork getWork();
	void setWork(IWork work);
}
