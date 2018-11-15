package ru.gzpn.spc.csl.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.Table;

import ru.gzpn.spc.csl.model.interfaces.ILocalEstimate;
import ru.gzpn.spc.csl.model.interfaces.IPlanObject;
import ru.gzpn.spc.csl.model.interfaces.IWork;

@Entity
@NamedQueries({ 
})
@Table(schema="spc_csl_schema", name="works",
indexes = {
		@Index(name = "spc_csl_idx_wkid", columnList = "code", unique = true),
		@Index(name = "spc_csl_idx_wkname", columnList = "name"),
		@Index(name = "spc_csl_idx_wktype", columnList = "type") 
	})
public class Work extends BaseEntity implements IWork, Serializable {
	private static final long serialVersionUID = -7299274432662352949L;
	
	@Column(length=64)
	private String code;
	@Column(length=128)
	private String name;
	@Column(length=4)
	private String type;
	
	@ManyToOne(targetEntity = PlanObject.class, fetch = FetchType.LAZY)
	@JoinColumn(name="plan_obj_id", referencedColumnName="id")
	private IPlanObject planObj;

	@ManyToOne(targetEntity = LocalEstimate.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "est_id", referencedColumnName = "id")
	private ILocalEstimate localEstimate;
	
	public Work() {
	}
	
	public Work(String code, String name, String type) {
		this.code = code;
		this.name = name;
		this.type = type;
	}

	public ILocalEstimate getLocalEstimate() {
		return localEstimate;
	}

	public void setLocalEstimate(ILocalEstimate localEstimate) {
		this.localEstimate = localEstimate;
	}

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
