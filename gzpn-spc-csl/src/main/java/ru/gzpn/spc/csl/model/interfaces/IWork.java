package ru.gzpn.spc.csl.model.interfaces;

import ru.gzpn.spc.csl.model.enums.WorkType;

public interface IWork {
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
}
