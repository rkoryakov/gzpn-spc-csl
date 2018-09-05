package ru.gzpn.spc.csl.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ru.gzpn.spc.csl.model.interfaces.ICProject;
import ru.gzpn.spc.csl.model.interfaces.IStage;

@Entity
@Table(schema = "spc_csl_stages", name = "spc_csl_cproject")
public class CProject extends ACLBasedEntity implements ICProject {
	private String name;
	private String code;

	@ManyToOne(targetEntity = Stage.class, fetch = FetchType.LAZY)
	@JoinTable(schema = "spc_csl_stages", name = "stage_cproject", joinColumns = {
			@JoinColumn(name = "cproject_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "stage_id", referencedColumnName = "id") })
	private IStage stage;

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

}
