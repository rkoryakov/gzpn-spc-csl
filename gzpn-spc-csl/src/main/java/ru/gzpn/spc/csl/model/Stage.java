package ru.gzpn.spc.csl.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ru.gzpn.spc.csl.model.interfaces.ILocalEstimate;
import ru.gzpn.spc.csl.model.interfaces.IObjectEstimate;
import ru.gzpn.spc.csl.model.interfaces.IPlanObject;
import ru.gzpn.spc.csl.model.interfaces.IStage;

@Entity
@Table(schema = "spc_csl_schema", name = "stages", indexes = {
		@Index(name = "spc_csl_idx_stagename", columnList = "name")
})
public class Stage extends BaseEntity implements IStage, Serializable {
	private static final long serialVersionUID = 5559748809655988720L;

	public static final String FIELD_NAME = "name";

	@Column(unique = true, length = 64)
	private String name;
	@Column(unique = true, length = 8)
	private String code;
	
	@OneToMany(targetEntity = PlanObject.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "stage_id", referencedColumnName = "id")
	private List<IPlanObject> planObjects;
	
	@OneToMany(targetEntity = LocalEstimate.class)
	@JoinColumn(name = "stage_id", referencedColumnName = "id")
	private List<ILocalEstimate> localEstimates;
	
	@OneToMany(targetEntity = ObjectEstimate.class)
	@JoinColumn(name = "stage_id", referencedColumnName = "id")
	private List<IObjectEstimate> objectEstimates;
	
	public Stage() {
	}
	
	public Stage(String stage) {
		this.name = stage;
	}
	
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
	public String toString() {
		return "name: " + getName() + " id: " + getId();
	}

	@Override
	public List<ILocalEstimate> getLocalEstimates() {
		return localEstimates;
	}

	@Override
	public void setLocalEstimates(List<ILocalEstimate> localEstimates) {
		this.localEstimates = localEstimates;
	}

	@Override
	public List<IObjectEstimate> getObjectEstimates() {
		return objectEstimates;
	}

	@Override
	public void setObjectEstimates(List<IObjectEstimate> objectEstimates) {
		this.objectEstimates = objectEstimates;
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
