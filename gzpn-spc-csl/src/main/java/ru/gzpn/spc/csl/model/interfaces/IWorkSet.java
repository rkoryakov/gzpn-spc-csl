package ru.gzpn.spc.csl.model.interfaces;

public interface IWorkSet extends IBaseEntity {
	public static final String FIELD_NAME = "workset.name";
	public static final String FIELD_CODE = "workset.code";
	public static final String FIELD_PIR = "workset.pir.code";
	public static final String FIELD_SMR = "workset.smr.code";
	public static final String FIELD_PLAN_OBJECT = "workset.planObject";
	/* Overrode fields */
	public static final String FIELD_ID = "workset." + IBaseEntity.FIELD_ID;
	public static final String FIELD_CHANGE_DATE = "workset." + IBaseEntity.FIELD_CHANGE_DATE;
	public static final String FIELD_CREATE_DATE = "workset." + IBaseEntity.FIELD_CREATE_DATE;
	
	public String getName();
	public void setName(String name);
	public String getCode();
	public void setCode(String code);
	public IWork getPir();
	public String getPirCaption();
	public void setPir(IWork pir);
	public IWork getSmr();
	public String getSmrCaption();
	public void setSmr(IWork smr);
	public IPlanObject getPlanObject();
	public void setPlanObject(IPlanObject planObject);
}
