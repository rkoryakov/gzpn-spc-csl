package ru.gzpn.spc.csl.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import ru.gzpn.spc.csl.model.interfaces.ICProject;
import ru.gzpn.spc.csl.model.interfaces.IPlanObject;
import ru.gzpn.spc.csl.model.interfaces.IWork;

@Entity
@NamedQueries({ 
})
@Table(schema = "spc_csl_schema", name = "plan_objects_tree",
indexes = {
		@Index(name = "spc_csl_idx_plnid", columnList = "code", unique = true),
		@Index(name = "spc_csl_idx_plnname", columnList = "name") 
	}
)
public class PlanObject extends BaseEntity implements IPlanObject, Serializable {
	private static final long serialVersionUID = -1569405101073898860L;
	
	@Column(length=64)
	private String code;
	@Column(length=128)
	private String name;
	@Column(length=4)
	private String mark;
	
	@OneToMany
	@OrderColumn
	@JoinColumn(name="parent_id")
	private List<PlanObject> children = new LinkedList<>();
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="parent_id", insertable=false, updatable=false)
	private PlanObject parent;
	
	@ManyToOne(targetEntity = CProject.class, fetch=FetchType.LAZY)
	@JoinColumn(name="cp_id", referencedColumnName="id", insertable=false, updatable=false)
	private ICProject cproject;
	
	@OneToMany(targetEntity = Work.class)
	@JoinColumn(name="plan_obj_id", referencedColumnName="id")
	private List<IWork> workList;
	
	public PlanObject() {
	}
	
	public PlanObject(String code, String name, String mark) {
		this.code = code;
		this.name = name;
		this.mark = mark;
	}
	
	public List<IWork> getWorkList() {
		return workList;
	}

	public void setWorkList(List<IWork> workList) {
		this.workList = workList;
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

	public ICProject getCproject() {
		return cproject;
	}

	public void setCproject(ICProject cproject) {
		this.cproject = cproject;
	}

	public void setParent(PlanObject parent) {
		this.parent = parent;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}
}
