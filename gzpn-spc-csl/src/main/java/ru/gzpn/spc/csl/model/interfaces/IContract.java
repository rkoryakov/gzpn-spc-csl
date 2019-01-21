package ru.gzpn.spc.csl.model.interfaces;

import java.time.LocalDate;
import java.util.List;

public interface IContract {
	public static final String ENTITYNAME_DOT = "contract.";
	
	public static final String FIELD_NAME = ENTITYNAME_DOT + "name";
	public static final String FIELD_CODE = ENTITYNAME_DOT + "code";
	public static final String FIELD_SIGNINGDATE = ENTITYNAME_DOT + "signingDate";
	public static final String FIELD_CUSTOMERNAME = ENTITYNAME_DOT + "customerName";
	public static final String FIELD_CUSTOMERINN = ENTITYNAME_DOT + "customerINN";
	
	public static final String FIELD_CUSTORMERBANK = ENTITYNAME_DOT + "custormerBank";
	public static final String FIELD_EXECUTORNAME = ENTITYNAME_DOT + "executorName";
	public static final String FIELD_EXECUTORINN = ENTITYNAME_DOT + "executorINN";
	public static final String FIELD_EXECUTORBANK = ENTITYNAME_DOT + "executorBank";

	/* Overridden fields */
	public static final String FIELD_ID = ENTITYNAME_DOT + IBaseEntity.FIELD_ID;
	public static final String FIELD_CHANGE_DATE = ENTITYNAME_DOT + IBaseEntity.FIELD_CHANGE_DATE;
	public static final String FIELD_CREATE_DATE = ENTITYNAME_DOT + IBaseEntity.FIELD_CREATE_DATE;
	public static final String FIELD_VERSION = ENTITYNAME_DOT + IBaseEntity.FIELD_VERSION;
	
	
	String getName();
	void setName(String name);
	
	String getCode();
	void setCode(String code);
	
	LocalDate getSigningDate();
	void setSigningDate(LocalDate signingDate);
	
	String getCustomerName();
	void setCustomerName(String customerName);
	
	String getCustomerINN();
	void setCustomerINN(String customerINN);
	
	String getCustormerBank();
	void setCustormerBank(String custormerBank);
	
	String getExecutorName();
	void setExecutorName(String executorName);
	
	String getExecutorINN();
	void setExecutorINN(String executorINN);
	
	String getExecutorBank();
	void setExecutorBank(String executorBank);
	
	void setMilestones(List<IMilestone> milestones);
	List<IMilestone> getMilestones();
}
