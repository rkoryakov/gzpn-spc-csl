package ru.gzpn.spc.csl.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import ru.gzpn.spc.csl.model.interfaces.ICProject;
import ru.gzpn.spc.csl.model.interfaces.IEstimateCalculation;
import ru.gzpn.spc.csl.model.interfaces.IHProject;
import ru.gzpn.spc.csl.model.interfaces.IMilestone;
import ru.gzpn.spc.csl.model.interfaces.IPhase;
import ru.gzpn.spc.csl.model.interfaces.IPlanObject;
import ru.gzpn.spc.csl.model.interfaces.IStage;

@Entity
@Table(schema = "spc_csl_schema", name = "capital_projects", 
indexes = {
			@Index(name = "spc_csl_idx_prjname", columnList = "name"),
			@Index(name = "spc_csl_idx_prjid", columnList = "code", unique = true),
			@Index(name = "spc_csl_idx_prjstg", columnList = "stage_id"),
			@Index(name = "spc_csl_idx_prjphs", columnList = "phase_id")
})
public class CProject extends ACLBasedEntity implements ICProject, Serializable {
	private static final long serialVersionUID = 4547825496450260103L;
	
	public static final String FIELD_NAME = "name";
	public static final String FILED_PROJECT_ID = "projectId";
	
	private String name;
	private String code;
	
	@ManyToOne(targetEntity = HProject.class)
	@JoinColumn(name = "hp_id", referencedColumnName = "id")
	private IHProject hproject;
	
	@ManyToOne(targetEntity = Stage.class)
	@JoinColumn(name = "stage_id", referencedColumnName = "id")
	private IStage stage;

	@ManyToOne(targetEntity = Phase.class)
	@JoinColumn(name = "phase_id", referencedColumnName = "id")
	private IPhase phase;

	@OneToMany(targetEntity = PlanObject.class)
	@JoinColumn(name="cp_id", referencedColumnName="id")
	private List<IPlanObject> planObjects;

	@OneToOne(targetEntity = Milestone.class)
	@JoinColumn(name="id", referencedColumnName="cp_id")
	private IMilestone milestone;
	
	@OneToMany(targetEntity = EstimateCalculation.class)
	@JoinColumn(name = "cp_id", referencedColumnName = "id")
	private List<IEstimateCalculation> estimateCalculations;
	
	public CProject() {
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public IStage getStage() {
		return stage;
	}

	public void setStage(IStage stage) {
		this.stage = stage;
	}

	public IHProject getHproject() {
		return hproject;
	}

	public void setHproject(IHProject hproject) {
		this.hproject = hproject;
	}

	public IPhase getPhase() {
		return phase;
	}

	public void setPhase(IPhase phase) {
		this.phase = phase;
	}

	public List<IPlanObject> getPlanObjects() {
		return planObjects;
	}

	public void setPlanObjects(List<IPlanObject> planObjects) {
		this.planObjects = planObjects;
	}

	public IMilestone getMilestone() {
		return milestone;
	}

	public void setMilestone(IMilestone milestone) {
		this.milestone = milestone;
	}

	public List<IEstimateCalculation> getEstimateCalculations() {
		return estimateCalculations;
	}

	public void setEstimateCalculations(List<IEstimateCalculation> estimateCalculations) {
		this.estimateCalculations = estimateCalculations;
	}
}
