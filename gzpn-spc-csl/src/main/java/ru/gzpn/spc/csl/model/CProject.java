package ru.gzpn.spc.csl.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ru.gzpn.spc.csl.model.interfaces.ICProject;
import ru.gzpn.spc.csl.model.interfaces.IPhase;
import ru.gzpn.spc.csl.model.interfaces.IStage;

@Entity
@Table(schema = "spc_csl_schema", name = "capital_project", 
			indexes = {
				@Index(name = "spc_csl_idx_prjid", columnList = "projectId", unique = true),
			}
)
public class CProject extends ACLBasedEntity implements ICProject {
	private String projectId;
	private String name;

	@ManyToOne(targetEntity = Stage.class, fetch = FetchType.LAZY)
	@JoinTable(schema = "spc_csl_schema", name = "stage_2_cproject", joinColumns = {
			@JoinColumn(name = "cproject_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "stage_id", referencedColumnName = "id") })
	private IStage stage;

	@ManyToOne(targetEntity = Phase.class, fetch = FetchType.LAZY)
	@JoinTable(schema = "spc_csl_schema", name = "phase_2_cproject", joinColumns = {
			@JoinColumn(name = "cproject_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "phase_id", referencedColumnName = "id") })
	private IPhase phase;

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

}
