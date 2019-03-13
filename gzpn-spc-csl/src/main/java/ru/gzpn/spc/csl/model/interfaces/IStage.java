package ru.gzpn.spc.csl.model.interfaces;

import java.util.List;

public interface IStage {
	
	public String getName();
	
	public void setName(String name);
	
	public List<ILocalEstimate> getLocalEstimates();

	public void setLocalEstimates(List<ILocalEstimate> localEstimates);

	public List<IObjectEstimate> getObjectEstimates();

	public void setObjectEstimates(List<IObjectEstimate> objectEstimates);

	String getCode();

	void setCode(String code);

	void setPlanObjects(List<IPlanObject> planObjects);

	List<IPlanObject> getPlanObjects();
}
