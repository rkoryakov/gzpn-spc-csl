package ru.gzpn.spc.csl.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ru.gzpn.spc.csl.model.interfaces.ICProject;
import ru.gzpn.spc.csl.model.interfaces.IHProject;

@Entity
@NamedQueries({
	@NamedQuery(name = "HProject.findByCode", query = "SELECT hp FROM HProject hp WHERE hp.code = ?1"),	
})
@Table(name = "heavy_projects", schema = "spc_csl_schema", 
	indexes = {
		@Index(name = "spc_csl_idx_hprjname", columnList = "name"),
		@Index(name = "spc_csl_idx_hprjcode", columnList = "code", unique = true)
	}
)
public class HProject extends ACLBasedEntity implements IHProject, Serializable {
	private static final long serialVersionUID = 5772209969161264775L;
	
	private String name;
	private String code;
	
	@OneToMany(targetEntity = CProject.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "hp_id", referencedColumnName = "id")
	private List<ICProject> capitalProjects;
	
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

	public List<ICProject> getCapitalProjects() {
		return capitalProjects;
	}

	public void setCapitalProjects(List<ICProject> capitalProjects) {
		this.capitalProjects = capitalProjects;
	}

	@Override
	public String toString() {
		return "name: " + name + ", projectId: " + code;
	}
}
