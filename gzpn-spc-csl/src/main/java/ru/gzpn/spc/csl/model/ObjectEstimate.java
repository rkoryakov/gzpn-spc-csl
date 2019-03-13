package ru.gzpn.spc.csl.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ru.gzpn.spc.csl.model.interfaces.IEstimateCalculation;
import ru.gzpn.spc.csl.model.interfaces.IEstimateCost;
import ru.gzpn.spc.csl.model.interfaces.IEstimateHead;
import ru.gzpn.spc.csl.model.interfaces.ILocalEstimate;
import ru.gzpn.spc.csl.model.interfaces.IObjectEstimate;
import ru.gzpn.spc.csl.model.interfaces.IStage;
@Entity
@Table(schema = "spc_csl_schema", name = "object_estimates",
indexes = {
		@Index(name = "spc_csl_idx_oestcode", columnList = "code", unique = true),
		@Index(name = "spc_csl_idx_oestname", columnList = "name"),
		@Index(name = "spc_csl_idx_oeststg", columnList = "stage_id"),
		@Index(name = "spc_csl_idx_oesthead", columnList = "est_head_id"),
		@Index(name = "spc_csl_idx_oestcalc", columnList = "est_calc_id")
})
public class ObjectEstimate extends BaseEntity implements IObjectEstimate, Serializable {
	private static final long serialVersionUID = 6947581167393866791L;

	@Column(length=64)
	private String code;
	private String name;
	private BigDecimal total;
	private BigDecimal smr;
	private BigDecimal devices;
	private BigDecimal other;
	private BigDecimal salariesFunds;
	
	@OneToMany(targetEntity = LocalEstimate.class)
	@JoinColumn(name = "oest_id", referencedColumnName = "id")
	private List<ILocalEstimate> localEstimates;
	
	@ManyToOne(targetEntity = Stage.class)
	@JoinColumn(name = "stage_id", referencedColumnName = "id")
	private IStage stage;

	@ManyToOne(targetEntity = EstimateCalculation.class)
	@JoinColumn(name = "est_calc_id", referencedColumnName = "id")
	private IEstimateCalculation estimateCalculation;
	
	@ManyToOne(targetEntity = EstimateHead.class)
	@JoinColumn(name = "est_head_id", referencedColumnName = "id")
	private IEstimateHead estimateHead;
	
	@OneToMany(targetEntity = EstimateCost.class)
	@JoinColumn(name = "oest_id", referencedColumnName = "id")
	private List<IEstimateCost> estimateCosts;
	
	@Override
	public String getCode() {
		return code;
	}

	@Override
	public void setCode(String code) {
		this.code = code;
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
	public BigDecimal getTotal() {
		return total;
	}

	@Override
	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	@Override
	public BigDecimal getSMR() {
		return smr;
	}

	@Override
	public void setSMR(BigDecimal smr) {
		this.smr = smr;
	}

	@Override
	public BigDecimal getDevices() {
		return devices;
	}

	@Override
	public void setDevices(BigDecimal devices) {
		this.devices = devices;
	}

	@Override
	public BigDecimal getOther() {
		return other;
	}

	@Override
	public void setOther(BigDecimal other) {
		this.other = other;
	}

	@Override
	public BigDecimal getSalariesFunds() {
		return salariesFunds;
	}

	@Override
	public void setSalariesFunds(BigDecimal salariesFunds) {
		this.salariesFunds = salariesFunds;
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
	public IStage getStage() {
		return stage;
	}

	@Override
	public void setStage(IStage stage) {
		this.stage = stage;
	}

	@Override
	public IEstimateCalculation getEstimateCalculation() {
		return estimateCalculation;
	}

	@Override
	public void setEstimateCalculation(IEstimateCalculation estimateCalculation) {
		this.estimateCalculation = estimateCalculation;
	}

	@Override
	public IEstimateHead getEstimateHead() {
		return estimateHead;
	}

	@Override
	public void setEstimateHead(IEstimateHead estimateHead) {
		this.estimateHead = estimateHead;
	}

	@Override
	public List<IEstimateCost> getEstimateCosts() {
		return estimateCosts;
	}

	@Override
	public void setEstimateCosts(List<IEstimateCost> estimateCosts) {
		this.estimateCosts = estimateCosts;
	}
}
