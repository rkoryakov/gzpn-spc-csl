package ru.gzpn.spc.csl.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ru.gzpn.spc.csl.model.interfaces.IMark;
import ru.gzpn.spc.csl.model.interfaces.IPlanObject;

@Entity
@Table(schema = "spc_csl_schema", name = "marks")
public class Mark extends BaseEntity implements IMark {
	private String name;
	
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
	public List<IPlanObject> getPlanObjects() {
		return planObjects;
	}

	@Override
	public void setPlanObjects(List<IPlanObject> planObjects) {
		this.planObjects = planObjects;
	}
}
