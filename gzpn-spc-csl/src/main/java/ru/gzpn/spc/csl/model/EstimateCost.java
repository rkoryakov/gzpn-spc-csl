package ru.gzpn.spc.csl.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ru.gzpn.spc.csl.model.interfaces.IEstimateCost;
import ru.gzpn.spc.csl.model.interfaces.ILocalEstimate;
import ru.gzpn.spc.csl.model.interfaces.IObjectEstimate;

@Entity
@Table(schema = "spc_csl_schema", name = "estimate_costs", 
indexes = {
		@Index(name = "spc_csl_idx_estcostoest", columnList = "oest_id"),
		@Index(name = "spc_csl_idx_estcostlest", columnList = "lest_id")
})
public class EstimateCost extends BaseEntity implements IEstimateCost, Serializable {
	private static final long serialVersionUID = -4589532334167233950L;
	
	// materials
	private BigDecimal matCustomerSupply;
	private BigDecimal matContractorSupply;
	private BigDecimal matPercentManual;
	
	// without devices
	private BigDecimal ozp;
	private BigDecimal emm;
	private BigDecimal zpm;
	
	// cost items
	private BigDecimal nr;
	private BigDecimal sp;
	private BigDecimal smrTotal;
	
	// devices
	private BigDecimal devicesTotal;
	private BigDecimal devicesPercentManual;
	private BigDecimal devicesCustomerSupply;
	private BigDecimal devicesContractorSupply;
	
	// cost items (other)
	private BigDecimal other;
	private BigDecimal total;
	private BigDecimal services;
	
	@ManyToOne(targetEntity = ObjectEstimate.class)
	@JoinColumn(name = "oest_id", referencedColumnName = "id")
	private IObjectEstimate objectEstimate;
	
	@ManyToOne(targetEntity = LocalEstimate.class)
	@JoinColumn(name = "lest_id", referencedColumnName = "id")
	private ILocalEstimate localEstimate;

	public BigDecimal getMatCustomerSupply() {
		return matCustomerSupply;
	}

	public void setMatCustomerSupply(BigDecimal matCustomerSupply) {
		this.matCustomerSupply = matCustomerSupply;
	}

	public BigDecimal getMatContractorSupply() {
		return matContractorSupply;
	}

	public void setMatContractorSupply(BigDecimal matContractorSupply) {
		this.matContractorSupply = matContractorSupply;
	}

	public BigDecimal getMatPercentManual() {
		return matPercentManual;
	}

	public void setMatPercentManual(BigDecimal matPercentManual) {
		this.matPercentManual = matPercentManual;
	}

	public BigDecimal getOzp() {
		return ozp;
	}

	public void setOzp(BigDecimal ozp) {
		this.ozp = ozp;
	}

	public BigDecimal getEmm() {
		return emm;
	}

	public void setEmm(BigDecimal emm) {
		this.emm = emm;
	}

	public BigDecimal getZpm() {
		return zpm;
	}

	public void setZpm(BigDecimal zpm) {
		this.zpm = zpm;
	}

	public BigDecimal getNr() {
		return nr;
	}

	public void setNr(BigDecimal nr) {
		this.nr = nr;
	}

	public BigDecimal getSp() {
		return sp;
	}

	public void setSp(BigDecimal sp) {
		this.sp = sp;
	}

	public BigDecimal getSmrTotal() {
		return smrTotal;
	}

	public void setSmrTotal(BigDecimal smrTotal) {
		this.smrTotal = smrTotal;
	}

	public BigDecimal getDevicesTotal() {
		return devicesTotal;
	}

	public void setDevicesTotal(BigDecimal devicesTotal) {
		this.devicesTotal = devicesTotal;
	}

	public BigDecimal getDevicesPercentManual() {
		return devicesPercentManual;
	}

	public void setDevicesPercentManual(BigDecimal devicesPercentManual) {
		this.devicesPercentManual = devicesPercentManual;
	}

	public BigDecimal getDevicesCustomerSupply() {
		return devicesCustomerSupply;
	}

	public void setDevicesCustomerSupply(BigDecimal devicesCustomerSupply) {
		this.devicesCustomerSupply = devicesCustomerSupply;
	}

	public BigDecimal getDevicesContractorSupply() {
		return devicesContractorSupply;
	}

	public void setDevicesContractorSupply(BigDecimal devicesContractorSupply) {
		this.devicesContractorSupply = devicesContractorSupply;
	}

	public BigDecimal getOther() {
		return other;
	}

	public void setOther(BigDecimal other) {
		this.other = other;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getServices() {
		return services;
	}

	public void setServices(BigDecimal services) {
		this.services = services;
	}

	public IObjectEstimate getObjectEstimate() {
		return objectEstimate;
	}

	public void setObjectEstimate(IObjectEstimate objectEstimate) {
		this.objectEstimate = objectEstimate;
	}

	public ILocalEstimate getLocalEstimate() {
		return localEstimate;
	}

	public void setLocalEstimate(ILocalEstimate localEstimate) {
		this.localEstimate = localEstimate;
	}
}
