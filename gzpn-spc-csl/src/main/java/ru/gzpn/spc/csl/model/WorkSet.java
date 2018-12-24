package ru.gzpn.spc.csl.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import ru.gzpn.spc.csl.model.interfaces.IPlanObject;
import ru.gzpn.spc.csl.model.interfaces.IWork;

@Entity
@Table(schema = "spc_csl_schema", name = "workset", 
indexes = {
		@Index(name = "spc_csl_idx_wkscode", columnList = "code"),
		@Index(name = "spc_csl_idx_wkscode", columnList = "name"),
		@Index(name = "spc_csl_idx_wkpln", columnList = "plan_obj_id")
})
public class WorkSet extends BaseEntity implements Serializable {
	private static final long serialVersionUID = -1489774086979019274L;
	@Column(length = 64)
	private String code;
	@Column(length = 256)
	private String name;
	@OneToOne(targetEntity = Work.class)
	@JoinColumn(name = "pir_id", referencedColumnName = "id")
	private IWork pir;
	@OneToOne(targetEntity = Work.class)
	@JoinColumn(name = "smr_id", referencedColumnName = "id")
	private IWork smr;
	@ManyToOne(targetEntity = PlanObject.class)
	@JoinColumn(name = "plan_obj_id", referencedColumnName = "id")
	private IPlanObject planObject;
	
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
	public IWork getPir() {
		return pir;
	}
	public void setPir(IWork pir) {
		this.pir = pir;
	}
	public IWork getSmr() {
		return smr;
	}
	public void setSmr(IWork smr) {
		this.smr = smr;
	}
	public IPlanObject getPlanObject() {
		return planObject;
	}
	public void setPlanObject(IPlanObject planObject) {
		this.planObject = planObject;
	}
}
