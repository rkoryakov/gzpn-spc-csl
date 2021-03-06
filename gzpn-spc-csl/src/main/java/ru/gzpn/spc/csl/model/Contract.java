package ru.gzpn.spc.csl.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import ru.gzpn.spc.csl.model.enums.ContractType;
import ru.gzpn.spc.csl.model.interfaces.ICProject;
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
	private ContractType contractType;
	
	@OneToMany(targetEntity = Milestone.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "cont_id", referencedColumnName = "id")
	private List<IMilestone> milestones;

	@OneToOne(targetEntity = CProject.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "cp_id", referencedColumnName = "id")
	private ICProject project;
	
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
	public LocalDate getSigningDate() {
		return signingDate;
	}
	@Override
	public void setSigningDate(LocalDate signingDate) {
		this.signingDate = signingDate;
	}
	@Override
	public String getCustomerName() {
		return customerName;
	}
	@Override
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	@Override
	public String getCustomerINN() {
		return customerINN;
	}
	@Override
	public void setCustomerINN(String customerINN) {
		this.customerINN = customerINN;
	}
	@Override
	public String getCustormerBank() {
		return custormerBank;
	}
	@Override
	public void setCustormerBank(String custormerBank) {
		this.custormerBank = custormerBank;
	}
	@Override
	public String getExecutorName() {
		return executorName;
	}
	@Override
	public void setExecutorName(String executorName) {
		this.executorName = executorName;
	}
	@Override
	public String getExecutorINN() {
		return executorINN;
	}
	@Override
	public void setExecutorINN(String executorINN) {
		this.executorINN = executorINN;
	}
	@Override
	public String getExecutorBank() {
		return executorBank;
	}
	@Override
	public void setExecutorBank(String executorBank) {
		this.executorBank = executorBank;
	}
	@Override
	public List<IMilestone> getMilestones() {
		return milestones;
	}
	@Override
	public void setMilestones(List<IMilestone> milestones) {
		this.milestones = milestones;
	}
	@Override
	public ContractType getContractType() {
		return contractType;
	}
	@Override
	public void setContractType(ContractType contractType) {
		this.contractType = contractType;
	}
	@Override
	public ICProject getProject() {
		return this.project;
	}
	@Override
	public void setProject(ICProject project) {
		this.project = project;
	}
	@Override
	public LocalDate getStartDate() {
		LocalDate result = LocalDate.MAX;
		for (IMilestone m : getMilestones()) {
			result = m.getStartDate().isBefore(result) ? m.getStartDate() : result;
		}
		return result.isEqual(LocalDate.MAX) ? LocalDate.now() : result;
	}
	@Override
	public LocalDate getEndDate() {
		LocalDate result = LocalDate.MIN;
		for (IMilestone m : getMilestones()) {
			result = m.getStartDate().isAfter(result) ? m.getStartDate() : result;
		}
		return result.isEqual(LocalDate.MIN) ? LocalDate.now() : result;
	}
	
	
	@Override
	public String toString() {
		return getCode();
	}
}
