package ru.gzpn.spc.csl.model.interfaces;

public interface IWorkSet extends IBaseEntity {
	public static final String FIELD_NAME = "name";
	public static final String FIELD_CODE = "code";
	public static final String FIELD_PIR = "pir.code";
	public static final String FIELD_SMR = "smr.code";
	public static final String FIELD_PLAN_OBJECT = "planObject";
	
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
