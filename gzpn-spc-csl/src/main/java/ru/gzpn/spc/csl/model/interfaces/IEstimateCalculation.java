package ru.gzpn.spc.csl.model.interfaces;

import java.util.List;

public interface IEstimateCalculation extends IBaseEntity {
	public static final String ENTITYNAME_DOT = "estimatecalculation.";
	
	public static final String FIELD_NAME = ENTITYNAME_DOT + "name";
	public static final String FIELD_CODE = ENTITYNAME_DOT + "code";
	public static final String FILED_HANDLER = "handler";
	public static final String FILED_CPROJECT = "cproject";
	
	/* Overridden fields */
	public static final String FIELD_ID = ENTITYNAME_DOT + IBaseEntity.FIELD_ID;
	public static final String FIELD_CHANGE_DATE = ENTITYNAME_DOT + IBaseEntity.FIELD_CHANGE_DATE;
	public static final String FIELD_CREATE_DATE = ENTITYNAME_DOT + IBaseEntity.FIELD_CREATE_DATE;
	public static final String FIELD_VERSION = ENTITYNAME_DOT + IBaseEntity.FIELD_VERSION;
	
	public String getCode();
	public void setCode(String code);
	
	public String getName();
	public void setName(String name);
	
	public String getHandler();
	public void setHandler(String handler);
	
	public ICProject getProject();
	public void setProject(ICProject project);
	
	public List<ILocalEstimate> getEstimates();
	public void setEstimates(List<ILocalEstimate> estimates);
	
	public List<IObjectEstimate> getObjectEstimates();
	public void setObjectEstimates(List<IObjectEstimate> objectEstimates);
}
