package ru.gzpn.spc.csl.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ru.gzpn.spc.csl.model.interfaces.ICProject;
import ru.gzpn.spc.csl.model.interfaces.IHProject;

@Entity
@Table(name = "havy_projects", schema = "spc_csl_schema", indexes = {
		@Index(name = "spc_csl_idx_prjid", columnList = "projectId", unique = true),
		@Index(name = "spc_csl_idx_prjname", columnList = "name") })
public class HProject extends ACLBasedEntity implements IHProject {
	private String projectId;
	private String name;
	@OneToMany(targetEntity = CProject.class, cascade = CascadeType.ALL)
	@JoinTable(schema = "spc_csl_schema", name = "hproject_2_cproject", joinColumns = {
			@JoinColumn(name = "hp_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "cp_id", referencedColumnName = "id") })
	private List<ICProject> capitalProjects;

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ICProject> getCapitalProjects() {
		return capitalProjects;
	}

	public void setCapitalProjects(List<ICProject> capitalProjects) {
		this.capitalProjects = capitalProjects;
	}

}
