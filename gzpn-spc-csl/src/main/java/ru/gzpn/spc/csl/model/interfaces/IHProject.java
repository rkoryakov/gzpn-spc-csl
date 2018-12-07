package ru.gzpn.spc.csl.model.interfaces;

import java.util.List;

public interface IHProject {
	public String getCode();

	public void setCode(String code);

	public String getName();

	public void setName(String name);

	public List<ICProject> getCapitalProjects();

	public void setCapitalProjects(List<ICProject> capitalProjects);
}
