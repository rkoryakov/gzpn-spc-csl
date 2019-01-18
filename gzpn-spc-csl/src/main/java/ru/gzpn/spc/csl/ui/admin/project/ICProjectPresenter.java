package ru.gzpn.spc.csl.ui.admin.project;

import ru.gzpn.spc.csl.model.interfaces.ICProject;

public interface ICProjectPresenter extends ICProject {
	
	public ICProject getcProject();
	public void setcProject(ICProject cProject);
	public String getCreateDatePresenter();
	public String getChangeDatePresenter();
	public String getStageCaption();
	public String getPhaseCaption();
	public String getMilestoneCaption();
	public String getHProjectCaption();
}
