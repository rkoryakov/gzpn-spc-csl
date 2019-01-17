package ru.gzpn.spc.csl.ui.admin.project;

import java.time.format.DateTimeFormatter;

import ru.gzpn.spc.csl.model.HProject;
import ru.gzpn.spc.csl.model.interfaces.IHProject;

public class HProjectPresenter extends HProject implements IHProjectPresenter {
	
	HProjectPresenter(IHProject ihProject){
		this.setId(ihProject.getId());
		this.setName(ihProject.getName());
		this.setCode(ihProject.getCode());
		this.setCapitalProjects(ihProject.getCapitalProjects());
		this.setCreateDate(ihProject.getCreateDate());
		this.setChangeDate(ihProject.getChangeDate());
		this.setVersion(ihProject.getVersion());
	}
	
	public String getCreateDatePresenter() {
		return DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:mm:ss").format(this.getCreateDate());
	}
	
	public String getChangeDatePresenter() {
		return DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:mm:ss").format(this.getChangeDate());
	}
}
