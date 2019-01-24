package ru.gzpn.spc.csl.model.interfaces;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import ru.gzpn.spc.csl.model.enums.TaxType;

public interface IMilestone {
	public static final String ENTITYNAME_DOT = "Milestone.";
	
	public static final String FIELD_NAME = ENTITYNAME_DOT + "name";
	public static final String FIELD_CODE = ENTITYNAME_DOT + "code";
	public static final String FIELD_STARTDATE = ENTITYNAME_DOT + "startDate";
	public static final String FIELD_ENDDATE = ENTITYNAME_DOT + "endDate";
	public static final String FIELD_PROJECT = ENTITYNAME_DOT + "project";
	
	public static final String FIELD_SUM = ENTITYNAME_DOT + "sum";
	public static final String FIELD_TAXTYPE = ENTITYNAME_DOT + "taxType";
	public static final String FIELD_CONTRACT = ENTITYNAME_DOT + "contract";
	public static final String FIELD_PPNUM = ENTITYNAME_DOT + "ppNum";
	public static final String FIELD_MILNUM = ENTITYNAME_DOT + "milNum";

	/* Overridden fields */
	public static final String FIELD_ID = ENTITYNAME_DOT + IBaseEntity.FIELD_ID;
	public static final String FIELD_CHANGE_DATE = ENTITYNAME_DOT + IBaseEntity.FIELD_CHANGE_DATE;
	public static final String FIELD_CREATE_DATE = ENTITYNAME_DOT + IBaseEntity.FIELD_CREATE_DATE;
	public static final String FIELD_VERSION = ENTITYNAME_DOT + IBaseEntity.FIELD_VERSION;
	
	public String getName();
	public void setName(String name);
	
	public String getCode();
	public void setCode(String code);
	
	public LocalDate getStartDate();
	public void setStartDate(LocalDate startDate);
	
	public LocalDate getEndDate();
	public void setEndDate(LocalDate endDate);
	
	public ICProject getProject();
	public void setProject(ICProject project);
	
	public BigDecimal getSum();
	public void setSum(BigDecimal sum);
	
	public TaxType getTaxType();
	public void setTaxType(TaxType taxType);
	
	public List<IWork> getWorks();
	public void setWorks(List<IWork> works);
	
	public IContract getContract();
	public void setContract(IContract contract);
	
	public int getPpNum();
	public void setPpNum(int ppNum);
	
	public int getMilNum();
	public void setMilNum(int milNum);
}
