package ru.gzpn.spc.csl.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ru.gzpn.spc.csl.model.interfaces.ICProject;
import ru.gzpn.spc.csl.model.interfaces.IPhase;
import ru.gzpn.spc.csl.model.interfaces.IPlanObject;
import ru.gzpn.spc.csl.model.interfaces.IStage;

@Entity
@Table(schema = "spc_csl_schema", name = "capital_projects", 
			indexes = {
				@Index(name = "spc_csl_idx_prjid", columnList = "projectId", unique = true),
			}
)
public class CProject extends ACLBasedEntity implements ICProject, Serializable {
	private static final long serialVersionUID = 4547825496450260103L;
	
	public static final String FIELD_NAME = "name";
	public static final String FILED_PROJECT_ID = "projectId";
	
	
	private String projectId;
	private String name;
	
	@ManyToOne(targetEntity = HProject.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "hp_id", referencedColumnName = "id")
	private HProject hproject;
	
	@ManyToOne(targetEntity = Stage.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "stage_id", referencedColumnName = "id")
	private IStage stage;

	@ManyToOne(targetEntity = IPhase.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "phase_id", referencedColumnName = "id")
	private IPhase phase;

	@OneToMany(targetEntity = PlanObject.class, fetch = FetchType.LAZY)
	@JoinColumn(name="cp_id", referencedColumnName="id")
	private List<IPlanObject> planObjects;
	
	public CProject() {
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projId) {
		this.projectId = projId;
	}

	public IStage getStage() {
		return stage;
	}

	public void setStage(IStage stage) {
		this.stage = stage;
	}

	public HProject getHproject() {
		return hproject;
	}

	public void setHproject(HProject hproject) {
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

}
