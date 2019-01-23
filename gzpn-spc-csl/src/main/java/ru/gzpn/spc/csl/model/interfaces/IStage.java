package ru.gzpn.spc.csl.model.interfaces;

import java.util.List;

public interface IStage {
	
	public String getName();
	
	public void setName(String name);
	
	public List<ICProject> getCprojects();

	public void setCprojects(List<ICProject> cprojects);

	public List<ILocalEstimate> getLocalEstimates();

	public void setLocalEstimates(List<ILocalEstimate> localEstimates);

	public List<IObjectEstimate> getObjectEstimates();

	public void setObjectEstimates(List<IObjectEstimate> objectEstimates);
}
