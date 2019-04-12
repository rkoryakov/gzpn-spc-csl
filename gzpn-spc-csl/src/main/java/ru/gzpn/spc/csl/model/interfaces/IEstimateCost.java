package ru.gzpn.spc.csl.model.interfaces;

import java.math.BigDecimal;

import ru.gzpn.spc.csl.model.enums.PriceLevel;

public interface IEstimateCost extends IBaseEntity {
	
	public static final String ENTITYNAME_DOT = "EstimateCost.";
	
	public static final String FIELD_MAT_CUSTOMER_SUPPLY = ENTITYNAME_DOT + "matCustomerSupply";
	public static final String FIELD_MAT_CONTRACTOR_SUPPLY = ENTITYNAME_DOT + "matContractorSupply";
	public static final String FIELD_MAT_PERCENT_MANUAL_SUPPLY = ENTITYNAME_DOT + "matPercentManual";

	public static final String FIELD_OZP = ENTITYNAME_DOT + "ozp";
	public static final String FIELD_EMM = ENTITYNAME_DOT + "emm";
	public static final String FIELD_ZPM = ENTITYNAME_DOT + "zpm";
	
	public static final String FIELD_NR = ENTITYNAME_DOT + "nr";
	public static final String FIELD_SP = ENTITYNAME_DOT + "sp";
	public static final String FIELD_SMR_TOTAL = ENTITYNAME_DOT + "smrTotal";
	
	public static final String FIELD_DEVICES_TOTAL = ENTITYNAME_DOT + "devicesTotal";
	public static final String FIELD_DEVICES_PERCENT_MANUAL = ENTITYNAME_DOT + "devicesPercentManual";
	public static final String FIELD_DEVICES_CUSTOMER_SUPPLY = ENTITYNAME_DOT + "devicesCustomerSupply";
	public static final String FIELD_DEVICES_CONTRACTOR_SUPPLY = ENTITYNAME_DOT + "devicesContractorSupply";
	
	public static final String FIELD_OTHER = ENTITYNAME_DOT + "other";
	public static final String FIELD_TOTAL = ENTITYNAME_DOT + "total";
	public static final String FIELD_SERVICES = ENTITYNAME_DOT + "services";
	
	public static final String FIELD_OBJECTESTIMATE = ENTITYNAME_DOT + "objectEstimate";
	public static final String FIELD_LOCALESTIMATE = ENTITYNAME_DOT + "localEstimate";
	
	public static final String FIELD_PRICELEVEL = ENTITYNAME_DOT + "priceLevel";
	
	/* Overridden fields */
	public static final String FIELD_ID = ENTITYNAME_DOT + IBaseEntity.FIELD_ID;
	public static final String FIELD_CHANGE_DATE = ENTITYNAME_DOT + IBaseEntity.FIELD_CHANGE_DATE;
	public static final String FIELD_CREATE_DATE = ENTITYNAME_DOT + IBaseEntity.FIELD_CREATE_DATE;
	public static final String FIELD_VERSION = ENTITYNAME_DOT + IBaseEntity.FIELD_VERSION;
	
	public BigDecimal getMatCustomerSupply();
	public void setMatCustomerSupply(BigDecimal matCustomerSupply);

	public BigDecimal getMatContractorSupply();
	public void setMatContractorSupply(BigDecimal matContractorSupply);

	public BigDecimal getMatPercentManual();
	public void setMatPercentManual(BigDecimal matPercentManual);

	public BigDecimal getOzp();
	public void setOzp(BigDecimal ozp);

	public BigDecimal getEmm();
	public void setEmm(BigDecimal emm);

	public BigDecimal getZpm();
	public void setZpm(BigDecimal zpm);

	public BigDecimal getNr();
	public void setNr(BigDecimal nr);

	public BigDecimal getSp();
	public void setSp(BigDecimal sp);

	public BigDecimal getSmrTotal();
	public void setSmrTotal(BigDecimal smrTotal);

	public BigDecimal getDevicesTotal();
	public void setDevicesTotal(BigDecimal devicesTotal);

	public BigDecimal getDevicesPercentManual();
	public void setDevicesPercentManual(BigDecimal devicesPercentManual);

	public BigDecimal getDevicesCustomerSupply();
	public void setDevicesCustomerSupply(BigDecimal devicesCustomerSupply);

	public BigDecimal getDevicesContractorSupply();
	public void setDevicesContractorSupply(BigDecimal devicesContractorSupply);

	public BigDecimal getOther();
	public void setOther(BigDecimal other);

	public BigDecimal getTotal();
	public void setTotal(BigDecimal total);

	public BigDecimal getServices();
	public void setServices(BigDecimal services);

	public IObjectEstimate getObjectEstimate();
	public void setObjectEstimate(IObjectEstimate objectEstimate);

	public ILocalEstimate getLocalEstimate();
	public void setLocalEstimate(ILocalEstimate localEstimate);
	
	PriceLevel getPriceLevel();
	void setPriceLevel(PriceLevel priceLevel);
	
	BigDecimal getOverhead();
	void setOverhead(BigDecimal overhead);
	
	BigDecimal getEstimateProfit();
	void setEstimateProfit(BigDecimal estimateProfit);
}
