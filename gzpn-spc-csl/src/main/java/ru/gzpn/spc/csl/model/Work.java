package ru.gzpn.spc.csl.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.Table;

import ru.gzpn.spc.csl.model.interfaces.IPlanObject;
import ru.gzpn.spc.csl.model.interfaces.IWork;

@Entity
@NamedQueries({ 
})
@Table(schema="spc_csl_schema", name="work")
public class Work extends BaseEntity implements IWork {
	@Column(length=64)
	private String code;
	@Column(length=128)
	private String name;
	@Column(length=4)
	private String type;
	
	@ManyToOne(targetEntity = PlanObject.class, fetch = FetchType.LAZY)
	@JoinColumn(name="plan_obj_id", referencedColumnName="id", updatable=false, insertable=false)
	private IPlanObject planObj;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public IPlanObject getPlanObj() {
		return planObj;
	}

	public void setPlanObj(IPlanObject planObj) {
		this.planObj = planObj;
	}
}
