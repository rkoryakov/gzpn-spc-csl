package ru.gzpn.spc.csl.ui.admin.project;

import ru.gzpn.spc.csl.model.interfaces.IHProject;

public interface IHProjectPresenter extends IHProject {
	
	public IHProject gethProject();
	public void sethProject(IHProject hProject);
	public String getCreateDatePresenter();
	public String getChangeDatePresenter();
}
