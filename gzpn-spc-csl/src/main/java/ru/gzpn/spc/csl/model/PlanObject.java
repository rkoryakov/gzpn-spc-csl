package ru.gzpn.spc.csl.model;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import ru.gzpn.spc.csl.model.interfaces.IPlanObject;

@Entity
@NamedQueries({ 
})
@Table(schema = "spc_csl_schema", name = "plan_object")
public class PlanObject extends BaseEntity implements IPlanObject {

	@Column(length=64)
	private String objectId;
	@Column(length=128)
	private String name;
	
	@OneToMany
	@OrderColumn
	@JoinColumn(name="parent_id", referencedColumnName="id")
	private List<PlanObject> children = new LinkedList<>();
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="parent_id", referencedColumnName="id", insertable=false, updatable=false)
	private PlanObject parent;
	
	public String getObjectId() {
		return objectId;
	}
	
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setChildren(List<PlanObject> children) {
		this.children = children;
	}

	public PlanObject getParent() {
		return parent;
	}

	public List<PlanObject> getChildren() {
		return children;
	}
}
