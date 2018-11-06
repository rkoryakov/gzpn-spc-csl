package ru.gzpn.spc.csl.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ru.gzpn.spc.csl.model.interfaces.ICProject;
import ru.gzpn.spc.csl.model.interfaces.IStage;

@Entity
@NamedQueries({ 
})
@Table(schema = "spc_csl_schema", name = "stages")
public class Stage extends BaseEntity implements IStage {
	private String name;

	@OneToMany(targetEntity = CProject.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "staget_id", referencedColumnName = "id")
	private List<ICProject> cprojects;
	
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
