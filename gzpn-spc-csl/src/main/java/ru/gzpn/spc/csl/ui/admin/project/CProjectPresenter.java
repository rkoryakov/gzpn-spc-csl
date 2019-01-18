package ru.gzpn.spc.csl.ui.admin.project;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

import ru.gzpn.spc.csl.model.CProject;
import ru.gzpn.spc.csl.model.interfaces.ICProject;

@SuppressWarnings("serial")
public class CProjectPresenter extends CProject implements ICProjectPresenter {

	private ICProject cProject;
	
	CProjectPresenter(ICProject icProject){
		this.setId(icProject.getId());
		this.setName(icProject.getName());
		this.setCode(icProject.getCode());
		this.setStage(icProject.getStage());
		this.setPhase(icProject.getPhase());
		this.setHproject(icProject.getHproject());
		this.setMilestone(icProject.getMilestone());
		this.setCreateDate(icProject.getCreateDate());
		this.setChangeDate(icProject.getChangeDate());
		this.setVersion(icProject.getVersion());
		this.setPlanObjects(icProject.getPlanObjects());
		this.setEstimateCalculations(icProject.getEstimateCalculations());
		this.cProject = icProject;
	}
	
	@Override
	public ICProject getcProject() {
		return cProject;
	}
	
	@Override
	public void setcProject(ICProject cProject) {
		this.cProject = cProject;
	}

	@Override
	public String getCreateDatePresenter() {
		return DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:mm:ss").format(this.getCreateDate());
	}
	
	@Override
	public String getChangeDatePresenter() {
		return DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:mm:ss").format(this.getChangeDate());
	}
	
	@Override
	public String getStageCaption() {
		String result = "---";
		if (Objects.nonNull(getStage())) {
			result = getStage().getName();
		}
		return result;
	}
	
	@Override
	public String getPhaseCaption() {
		String result = "---";
		if (Objects.nonNull(getPhase())) {
			result = getPhase().getName();
		}
		return result;
	}
	
	@Override
	public String getMilestoneCaption() {
		String result = "---";
		if (Objects.nonNull(getMilestone())) {
			result = getMilestone().getCode();
		}
		return result;
	}
	
	@Override
	public String getHProjectCaption() {
		String result = "---";
		if (Objects.nonNull(getHproject())) {
			result = getHproject().getCode();
		}
		return result;
	}
}
