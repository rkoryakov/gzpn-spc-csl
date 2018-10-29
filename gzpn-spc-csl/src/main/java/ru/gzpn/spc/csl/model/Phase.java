
package ru.gzpn.spc.csl.model;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import ru.gzpn.spc.csl.model.interfaces.IPhase;

@Entity
@Table(schema = "spc_csl_schema", name = "phases")
public class Phase extends BaseEntity implements IPhase {
	@Column(length=64)
	private String name;

	@OneToMany
	@OrderColumn
	@JoinColumn(name="parent_id", referencedColumnName="id")
	private List<Phase> children = new LinkedList<>();
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="parent_id", referencedColumnName="id", insertable=false, updatable=false)
	private Phase parent;
	
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
}
