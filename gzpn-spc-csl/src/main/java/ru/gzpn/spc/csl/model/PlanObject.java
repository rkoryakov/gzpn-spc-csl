package ru.gzpn.spc.csl.model;

import java.io.Serializable;
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
import ru.gzpn.spc.csl.model.interfaces.IWorkSet;

@Entity
@NamedQueries({ 
})
@Table(schema = "spc_csl_schema", name = "plan_objects_tree",
indexes = {
		@Index(name = "spc_csl_idx_plncode", columnList = "code", unique = true),
		@Index(name = "spc_csl_idx_plnname", columnList = "name"),
		@Index(name = "spc_csl_idx_plncprj", columnList = "cp_id"),
		@Index(name = "spc_csl_idx_plnpar", columnList = "parent_id")
	}
)
public class PlanObject extends BaseEntity implements IPlanObject, Serializable {
	private static final long serialVersionUID = -1569405101073898860L;
	
	@Column(length = 64)
	private String code;
	private String name;
	@Column(length = 4)
	private String mark;
	
	@OneToMany(targetEntity = PlanObject.class)
	@OrderColumn
	@JoinColumn(name="parent_id", referencedColumnName = "id")
	private List<IPlanObject> children;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = PlanObject.class)
	@JoinColumn(name = "parent_id", insertable = false, updatable = false)
	private IPlanObject parent;
	
	@ManyToOne(targetEntity = CProject.class, fetch=FetchType.LAZY)
	@JoinColumn(name = "cp_id", referencedColumnName = "id", insertable = false, updatable = false)
	private ICProject cproject;
	
	@OneToMany(targetEntity = Work.class)
	@JoinColumn(name = "plan_obj_id", referencedColumnName = "id")
	private List<IWork> works;
	
	@OneToMany(targetEntity = WorkSet.class)
	@JoinColumn(name = "plan_obj_id", referencedColumnName = "id")
	private List<IWorkSet> workset;
	
	public PlanObject() {
	}
	
	public PlanObject(String code, String name, String mark) {
		this.code = code;
		this.name = name;
		this.mark = mark;
	}
	
	public List<IWork> getWorkList() {
		return works;
	}

	public void setWorkList(List<IWork> workList) {
		this.works = workList;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setChildren(List<IPlanObject> children) {
		this.children = children;
	}

	public IPlanObject getParent() {
		return parent;
	}

	public List<IPlanObject> getChildren() {
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

	public List<IWorkSet> getWorkset() {
		return workset;
	}

	public void setWorkset(List<IWorkSet> workset) {
		this.workset = workset;
	}
}
