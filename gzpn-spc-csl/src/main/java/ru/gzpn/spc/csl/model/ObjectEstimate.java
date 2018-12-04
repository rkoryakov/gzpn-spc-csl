package ru.gzpn.spc.csl.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ru.gzpn.spc.csl.model.interfaces.ILocalEstimate;
import ru.gzpn.spc.csl.model.interfaces.IObjectEstimate;
import ru.gzpn.spc.csl.model.interfaces.IStage;
@Entity
@Table(schema = "spc_csl_schema", name = "object_estimates",
indexes = {
		@Index(name = "spc_csl_idx_oestcode", columnList = "code", unique = true),
		@Index(name = "spc_csl_idx_oestname", columnList = "name"),
		@Index(name = "spc_csl_idx_oeststg", columnList = "stage_id")
})
@NamedQueries({})
public class ObjectEstimate extends BaseEntity implements IObjectEstimate, Serializable {
	private static final long serialVersionUID = 6947581167393866791L;

	@Column(length=64)
	private String code;
	private String name;
	private BigDecimal total;
	private BigDecimal SMR;
	private BigDecimal devices;
	private BigDecimal other;
	
	@OneToMany(targetEntity = LocalEstimate.class)
	@JoinColumn(name = "oest_id", referencedColumnName = "id")
	private List<ILocalEstimate> localEstimates;
	
	@ManyToOne(targetEntity = Stage.class)
	@JoinColumn(name = "stage_id", referencedColumnName = "id")
	private IStage stage;

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

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getSMR() {
		return SMR;
	}

	public void setSMR(BigDecimal sMR) {
		SMR = sMR;
	}

	public BigDecimal getDevices() {
		return devices;
	}

	public void setDevices(BigDecimal devices) {
		this.devices = devices;
	}

	public BigDecimal getOther() {
		return other;
	}

	public void setOther(BigDecimal other) {
		this.other = other;
	}

	public List<ILocalEstimate> getLocalEstimates() {
		return localEstimates;
	}

	public void setLocalEstimates(List<ILocalEstimate> localEstimates) {
		this.localEstimates = localEstimates;
	}

	public IStage getStage() {
		return stage;
	}

	public void setStage(IStage stage) {
		this.stage = stage;
	}
}
