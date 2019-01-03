package ru.gzpn.spc.csl.model.interfaces;

import java.util.List;

import ru.gzpn.spc.csl.model.PlanObject;

public interface IPlanObject extends IBaseEntity {
	
	public String getCode();
	public void setCode(String code);
	
	public String getName();
	public void setName(String name);
	
	public List<IWork> getWorkList();
	public void setWorkList(List<IWork> workList);

	public List<IPlanObject> getChildren();
	public void setChildren(List<IPlanObject> children);

	public IPlanObject getParent();
	public void setParent(PlanObject parent);
	
	public ICProject getCproject();
	public void setCproject(ICProject cproject);

	public String getMark();
	public void setMark(String mark);

	public List<IWorkSet> getWorkset();
	public void setWorkset(List<IWorkSet> workset);
	
}
