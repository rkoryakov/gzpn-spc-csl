package ru.gzpn.spc.csl.model.interfaces;

import java.util.List;

import ru.gzpn.spc.csl.model.enums.LocalEstimateStatus;

public interface ILocalEstimate extends IBaseEntity {
	public static final String ENTITYNAME_DOT = "LocalEstimate.";
	
	public static final String FIELD_NAME = ENTITYNAME_DOT + "name";
	public static final String FIELD_CODE = ENTITYNAME_DOT + "code";
	public static final String FIELD_CHANGEDBY = ENTITYNAME_DOT + "changedBy";
	public static final String FIELD_DRAWING = ENTITYNAME_DOT + "drawing";
	public static final String FIELD_STATUS = ENTITYNAME_DOT + "status";
	public static final String FIELD_COMMENT = ENTITYNAME_DOT + "comment";
	public static final String FIELD_DOCUMENT = ENTITYNAME_DOT + "document";
	public static final String FIELD_STAGE = ENTITYNAME_DOT + "stage";
	
	public static final String FIELD_ESTIMATECALCULATION = ENTITYNAME_DOT + "estimateCalculation";
	public static final String FIELD_OBJECTESTIMATE = ENTITYNAME_DOT + "objectEstimate";
	public static final String FIELD_ESTIMATEHEAD = ENTITYNAME_DOT + "estimateHead";

	/* Overridden fields */
	public static final String FIELD_ID = ENTITYNAME_DOT + IBaseEntity.FIELD_ID;
	public static final String FIELD_CHANGE_DATE = ENTITYNAME_DOT + IBaseEntity.FIELD_CHANGE_DATE;
	public static final String FIELD_CREATE_DATE = ENTITYNAME_DOT + IBaseEntity.FIELD_CREATE_DATE;
	public static final String FIELD_VERSION = ENTITYNAME_DOT + IBaseEntity.FIELD_VERSION;
	
	String getCode();
	void setCode(String code);
	
	String getName();
	void setName(String name);
	
	List<IWork> getWorks();
	void setWorks(List<IWork> works);
	
	IDocument getDocument();
	void setDocument(IDocument document);
	
	String getChangedBy();
	void setChangedBy(String changedBy);
	
	String getDrawing();
	void setDrawing(String drawing);
	
	LocalEstimateStatus getStatus();
	void setStatus(LocalEstimateStatus status);
	
	String getComment();
	void setComment(String comment);
	
	IStage getStage();
	void setStage(IStage stage);
	
	IEstimateCalculation getEstimateCalculation();
	void setEstimateCalculation(IEstimateCalculation estimateCalculation);
	
	IObjectEstimate getObjectEstimate();
	void setObjectEstimate(IObjectEstimate objectEstimate);
	
	List<ILocalEstimateHistory> getHistory();
	void setHistory(List<ILocalEstimateHistory> history);
	
	IEstimateHead getEstimateHead();
	void setEstimateHead(IEstimateHead estimateHead);
	
	List<IEstimateCost> getEstimateCosts();
	void setEstimateCosts(List<IEstimateCost> estimateCosts);
	IMilestone getMilestone();
	
}
