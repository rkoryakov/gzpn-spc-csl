package ru.gzpn.spc.csl.model.interfaces;

import java.util.List;

public interface ICProject extends IBaseEntity{
	public static final String ENTITYNAME_DOT = "cprojects.";
	
	public static final String FIELD_NAME = ENTITYNAME_DOT + "name";
	public static final String FIELD_CODE = ENTITYNAME_DOT + "code";
	public static final String FILED_HPROJECT = "hproject";
	public static final String FILED_STAGE = "stage";
	public static final String FILED_PHASE = "phase";
	public static final String FILED_MILESTONE = "milestone";
	/* Overrode fields */
	public static final String FIELD_ID = ENTITYNAME_DOT + IBaseEntity.FIELD_ID;
	public static final String FIELD_CHANGE_DATE = ENTITYNAME_DOT + IBaseEntity.FIELD_CHANGE_DATE;
	public static final String FIELD_CREATE_DATE = ENTITYNAME_DOT + IBaseEntity.FIELD_CREATE_DATE;
	public static final String FIELD_VERSION = ENTITYNAME_DOT + IBaseEntity.FIELD_VERSION;
	
	public String getName();

	public void setName(String name);

	public String getCode();

	public void setCode(String code);

	public IStage getStage();

	public void setStage(IStage stage);

	public IHProject getHproject();

	public void setHproject(IHProject hproject);

	public IPhase getPhase();

	public void setPhase(IPhase phase);

	public List<IPlanObject> getPlanObjects();

	public void setPlanObjects(List<IPlanObject> planObjects);

	public IMilestone getMilestone();

	public void setMilestone(IMilestone milestone);

	public List<IEstimateCalculation> getEstimateCalculations();

	public void setEstimateCalculations(List<IEstimateCalculation> estimateCalculations);

}
