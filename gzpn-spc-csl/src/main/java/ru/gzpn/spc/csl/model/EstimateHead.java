package ru.gzpn.spc.csl.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ru.gzpn.spc.csl.model.enums.EstimateHeadType;
import ru.gzpn.spc.csl.model.interfaces.IEstimateHead;
import ru.gzpn.spc.csl.model.interfaces.ILocalEstimate;
import ru.gzpn.spc.csl.model.interfaces.IObjectEstimate;

@Entity
@Table(schema = "spc_csl_schema", name = "estimate_heads",
indexes = {
		@Index(name = "spc_csl_idx_estheadcode", columnList = "code")
})
public class EstimateHead extends BaseEntity implements IEstimateHead, Serializable {
	private static final long serialVersionUID = 2800273438988506260L;
	
	@Column(length=16)
	private String code;
	private String name;
	private Float number;
	private EstimateHeadType type;
	
	@OneToMany(targetEntity = LocalEstimate.class)
	@JoinColumn(name = "est_head_id", referencedColumnName = "id")
	private List<ILocalEstimate> localEstimates;
	@OneToMany(targetEntity = ObjectEstimate.class)
	@JoinColumn(name = "est_head_id", referencedColumnName = "id")
	private List<IObjectEstimate> objectEstimates;
	
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
	public Float getNumber() {
		return number;
	}
	public void setNumber(Float number) {
		this.number = number;
	}
	public EstimateHeadType getType() {
		return type;
	}
	public void setType(EstimateHeadType type) {
		this.type = type;
	}
	public List<ILocalEstimate> getLocalEstimates() {
		return localEstimates;
	}
	public void setLocalEstimates(List<ILocalEstimate> localEstimates) {
		this.localEstimates = localEstimates;
	}
	public List<IObjectEstimate> getObjectEstimates() {
		return objectEstimates;
	}
	public void setObjectEstimates(List<IObjectEstimate> objectEstimates) {
		this.objectEstimates = objectEstimates;
	}
}
