package ru.gzpn.spc.csl.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ru.gzpn.spc.csl.model.interfaces.IMark;
import ru.gzpn.spc.csl.model.interfaces.IPlanObject;

@Entity
@Table(schema = "spc_csl_schema", name = "marks", indexes = {
		@Index(name = "spc_csl_idx_mark", columnList = "name,code", unique = true)
})
public class Mark extends BaseEntity implements IMark, Serializable {
	private String name;
	@Column(length = 4)
	private String code;
	
	@OneToMany(targetEntity = PlanObject.class)
	@JoinColumn(name = "mark_id", referencedColumnName = "id")
	private List<IPlanObject> planObjects;
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public List<IPlanObject> getPlanObjects() {
		return planObjects;
	}

	@Override
	public void setPlanObjects(List<IPlanObject> planObjects) {
		this.planObjects = planObjects;
	}
}
