package ru.gzpn.spc.csl.model.interfaces;

import java.util.List;

public interface IPhase {
	
	public String getName();

	public void setName(String name);

	public List<IPhase> getChildren();

	public void setChildren(List<IPhase> children);

	public IPhase getParent();

	public List<ICProject> getCprogects();

	public void setCprogects(List<ICProject> cprogects);

	public void setParent(IPhase parent);
}
