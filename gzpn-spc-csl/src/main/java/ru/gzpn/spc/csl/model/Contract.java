package ru.gzpn.spc.csl.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ru.gzpn.spc.csl.model.interfaces.IContract;
import ru.gzpn.spc.csl.model.interfaces.IMilestone;

@Entity
@Table(schema="spc_csl_schema", name="contract",
indexes = {
		@Index(name = "spc_csl_idx_cntname", columnList = "name", unique = true),
		@Index(name = "spc_csl_idx_cntcode", columnList = "code")
})
public class Contract extends BaseEntity implements IContract, Serializable {
	private static final long serialVersionUID = 3446433626324831206L;
	
	private String name;
	private String code;
	
	private LocalDate signingDate;
	
	private String customerName;
	@Column(length = 16)
	private String customerINN;
	private String custormerBank;
	private String executorName;
	@Column(length = 16)
	private String executorINN;
	private String executorBank;
	
	@OneToMany(targetEntity = Milestone.class)
	@JoinColumn(name = "cont_id", referencedColumnName = "id")
	private List<IMilestone> milestones;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public LocalDate getSigningDate() {
		return signingDate;
	}

	public void setSigningDate(LocalDate signingDate) {
		this.signingDate = signingDate;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerINN() {
		return customerINN;
	}

	public void setCustomerINN(String customerINN) {
		this.customerINN = customerINN;
	}

	public String getCustormerBank() {
		return custormerBank;
	}

	public void setCustormerBank(String custormerBank) {
		this.custormerBank = custormerBank;
	}

	public String getExecutorName() {
		return executorName;
	}

	public void setExecutorName(String executorName) {
		this.executorName = executorName;
	}

	public String getExecutorINN() {
		return executorINN;
	}

	public void setExecutorINN(String executorINN) {
		this.executorINN = executorINN;
	}

	public String getExecutorBank() {
		return executorBank;
	}

	public void setExecutorBank(String executorBank) {
		this.executorBank = executorBank;
	}

	public List<IMilestone> getMilestones() {
		return milestones;
	}

	public void setMilestones(List<IMilestone> milestones) {
		this.milestones = milestones;
	}
}
