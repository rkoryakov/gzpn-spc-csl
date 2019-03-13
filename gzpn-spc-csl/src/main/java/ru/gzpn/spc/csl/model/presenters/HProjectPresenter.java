package ru.gzpn.spc.csl.model.presenters;

import java.time.format.DateTimeFormatter;

import ru.gzpn.spc.csl.model.HProject;
import ru.gzpn.spc.csl.model.interfaces.IHProject;
import ru.gzpn.spc.csl.model.presenters.interfaces.IHProjectPresenter;

@SuppressWarnings("serial")
public class HProjectPresenter extends HProject implements IHProjectPresenter {
	
	private IHProject hProject;
	
	public HProjectPresenter(IHProject ihProject){
		this.setId(ihProject.getId());
		this.setName(ihProject.getName());
		this.setCode(ihProject.getCode());
		this.setCapitalProjects(ihProject.getCapitalProjects());
		this.setCreateDate(ihProject.getCreateDate());
		this.setChangeDate(ihProject.getChangeDate());
		this.setVersion(ihProject.getVersion());
		this.hProject = ihProject;
	}
	
	@Override
	public IHProject getHProject() {
		return hProject;
	}

	@Override
	public void setHProject(IHProject hProject) {
		this.hProject = hProject;
	}
	
	@Override
	public String getCreateDatePresenter() {
		return DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss").format(this.getCreateDate());
	}
	
	@Override
	public String getChangeDatePresenter() {
		return DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss").format(this.getChangeDate());
	}
}
