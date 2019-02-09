package ru.gzpn.spc.csl.model.interfaces;

import java.math.BigDecimal;
import java.util.List;

public interface IObjectEstimate extends IBaseEntity {
	public static final String ENTITYNAME_DOT = "IObjectEstimate.";
	
	public static final String FIELD_NAME = ENTITYNAME_DOT + "name";
	public static final String FIELD_CODE = ENTITYNAME_DOT + "code";
	public static final String FIELD_STAGE = ENTITYNAME_DOT + "stage";
	public static final String FIELD_SALARIES_FUNDS =  ENTITYNAME_DOT + "salariesFunds";
	public static final String FIELD_OTHER = ENTITYNAME_DOT + "other";
	public static final String FIELD_DEVICES = ENTITYNAME_DOT + "devices";
	public static final String FIELD_SMR = ENTITYNAME_DOT + "smr";
	public static final String FIELD_TOTAL = ENTITYNAME_DOT + "total";

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
	
	void setSalariesFunds(BigDecimal salariesFunds);
	BigDecimal getSalariesFunds();

	void setOther(BigDecimal other);
	BigDecimal getOther();

	void setDevices(BigDecimal devices);
	BigDecimal getDevices();

	void setSMR(BigDecimal smr);
	BigDecimal getSMR();
	
	void setTotal(BigDecimal total);
	BigDecimal getTotal();
	
	void setEstimateCosts(List<IEstimateCost> estimateCosts);
	List<IEstimateCost> getEstimateCosts();

	void setEstimateHead(IEstimateHead estimateHead);
	IEstimateHead getEstimateHead();

	void setEstimateCalculation(IEstimateCalculation estimateCalculation);
	IEstimateCalculation getEstimateCalculation();

	void setStage(IStage stage);
	IStage getStage();

	void setLocalEstimates(List<ILocalEstimate> localEstimates);
	List<ILocalEstimate> getLocalEstimates();
}
