package ru.gzpn.spc.csl.model.interfaces;

import java.time.LocalDate;
import java.util.List;

import ru.gzpn.spc.csl.model.enums.WorkType;

public interface IWork extends IBaseEntity {
	
	public static final String ENTITYNAME_DOT = "work.";
	
	public static final String FIELD_NAME = ENTITYNAME_DOT + "name";
	public static final String FIELD_CODE = ENTITYNAME_DOT + "code";
	public static final String FIELD_TYPE = ENTITYNAME_DOT + "type";
	public static final String FIELD_PLAN_OBJECT = ENTITYNAME_DOT + "planObj";
	public static final String FIELD_MILESTONE = ENTITYNAME_DOT + "milestone";
	public static final String FIELD_WORKSET = ENTITYNAME_DOT + "workSet";
	public static final String FIELD_BEGINDATE = ENTITYNAME_DOT + "beginDate";
	public static final String FIELD_ENDDATE = ENTITYNAME_DOT + "endDate";
	/* Overrode fields */
	public static final String FIELD_ID = ENTITYNAME_DOT + IBaseEntity.FIELD_ID;
	public static final String FIELD_CHANGE_DATE = ENTITYNAME_DOT + IBaseEntity.FIELD_CHANGE_DATE;
	public static final String FIELD_CREATE_DATE = ENTITYNAME_DOT + IBaseEntity.FIELD_CREATE_DATE;
	public static final String FIELD_VERSION = ENTITYNAME_DOT + IBaseEntity.FIELD_VERSION;
	
	
	public ILocalEstimate getLocalEstimate();
	public void setLocalEstimate(ILocalEstimate localEstimate);

	public String getCode();
	public void setCode(String code);

	public String getName();
	public void setName(String name);

	public WorkType getType();
	public void setType(WorkType type);

	public IPlanObject getPlanObj();
	public void setPlanObj(IPlanObject planObj);

	List<IDocument> getDocuments();
	void setDocuments(List<IDocument> documents);

	IMilestone getMilestone();
	void setMilestone(IMilestone milestone);

	IWorkSet getWorkSet();
	void setWorkSet(IWorkSet workSet);

	LocalDate getBeginDate();
	void setBeginDate(LocalDate beginDate);

	LocalDate getEndDate();
	void setEndDate(LocalDate endDate);
}
