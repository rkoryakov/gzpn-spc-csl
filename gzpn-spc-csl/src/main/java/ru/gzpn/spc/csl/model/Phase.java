
package ru.gzpn.spc.csl.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import ru.gzpn.spc.csl.model.interfaces.ICProject;
import ru.gzpn.spc.csl.model.interfaces.IPhase;

@Entity
@NamedQueries({
	@NamedQuery(name = "Phase.findByName", query = "SELECT p FROM Phase p WHERE p.name = ?1"),	
})
@Table(schema = "spc_csl_schema", name = "phases_tree")
public class Phase extends BaseEntity implements IPhase, Serializable {
	private static final long serialVersionUID = -3709362358146582320L;

	public static final String FIELD_NAME = "name";

	@Column(unique=true, length=64)
	private String name;

	@OneToMany
	@OrderColumn
	@JoinColumn(name="parent_id")
	private List<Phase> children = new LinkedList<>();
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="parent_id", insertable=false, updatable=false)
	private Phase parent;
	
	@OneToMany(targetEntity = CProject.class, fetch=FetchType.LAZY)
	@JoinColumn(name = "phase_id", referencedColumnName = "id")
	private List<ICProject> cprojects;
	
	public Phase() {
	}
	
	public Phase(String name, Phase parent) {
		this.setName(name);
		this.setParent(parent);
	}
	
	public Phase(String name) {
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Phase> getChildren() {
		return children;
	}

	public void setChildren(List<Phase> children) {
		this.children = children;
	}

	public Phase getParent() {
		return parent;
	}

	public List<ICProject> getCprogects() {
		return cprojects;
	}

	public void setCprogects(List<ICProject> cprogects) {
		this.cprojects = cprogects;
	}

	public void setParent(Phase parent) {
		this.parent = parent;
	}
}
