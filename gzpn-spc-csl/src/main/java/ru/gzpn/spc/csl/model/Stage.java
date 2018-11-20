package ru.gzpn.spc.csl.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ru.gzpn.spc.csl.model.interfaces.ICProject;
import ru.gzpn.spc.csl.model.interfaces.IStage;

@Entity
@NamedQueries({
	@NamedQuery(name = "Stage.findByName", query = "SELECT s FROM Stage s WHERE s.name = ?1"),	
})
@Table(schema = "spc_csl_schema", name = "stages", indexes = {
		@Index(name = "spc_csl_idx_stagename", columnList = "name")
	})
public class Stage extends BaseEntity implements IStage, Serializable {
	private static final long serialVersionUID = 5559748809655988720L;

	public static final String FIELD_NAME = "name";

	@Column(unique = true, length = 64)
	private String name;

	@OneToMany(targetEntity = CProject.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "stage_id", referencedColumnName = "id")
	private List<ICProject> cprojects;
	
	public Stage() {
	}
	
	public Stage(String stage) {
		this.name = stage;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return "name: " + getName() + " id: " + getId();
	}

	public List<ICProject> getCprojects() {
		return cprojects;
	}

	public void setCprojects(List<ICProject> cprojects) {
		this.cprojects = cprojects;
	}
}
