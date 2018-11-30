package ru.gzpn.spc.csl.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import ru.gzpn.spc.csl.model.enums.PaymentSchema;
import ru.gzpn.spc.csl.model.enums.TaxType;
import ru.gzpn.spc.csl.model.interfaces.ICProject;
import ru.gzpn.spc.csl.model.interfaces.IContract;
import ru.gzpn.spc.csl.model.interfaces.IMilestone;
import ru.gzpn.spc.csl.model.interfaces.IWork;

@Entity
@Table(schema="spc_csl_schema", name="milestone",
indexes = {
		@Index(name = "spc_csl_idx_milname", columnList = "name"),
		@Index(name = "spc_csl_idx_milcode", columnList = "code", unique = true),
		@Index(name = "spc_csl_idx_miltocp", columnList = "project"),
		@Index(name = "spc_csl_idx_miltowk", columnList = "works"),
		@Index(name = "spc_csl_idx_miltocon", columnList = "contract")
})
public class Milestone extends BaseEntity implements IMilestone, Serializable {
	private static final long serialVersionUID = 6247571288283340789L;

	private String name;
	private String code;
	
	private LocalDate startDate;
	private LocalDate endDate;
	private LocalDate deliveryDate;
	
	private BigDecimal sum;
	private TaxType taxType; // must be a directory
	private PaymentSchema paymentSchema; // must be a directory
	private Currency currency; // must be a directory
	
	@OneToOne(targetEntity=CProject.class)
	@JoinColumn(name="cp_id", referencedColumnName="id")
	private ICProject project;
	
	@OneToMany(targetEntity = Work.class)
	@JoinColumn(name = "milst_id", referencedColumnName = "id")
	private List<IWork> works;

	@ManyToOne(targetEntity = Contract.class)
	@JoinColumn(name = "cont_id", referencedColumnName = "id")
	private IContract contract;
	
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

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public ICProject getProject() {
		return project;
	}

	public void setProject(ICProject project) {
		this.project = project;
	}

	public LocalDate getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(LocalDate deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public BigDecimal getSum() {
		return sum;
	}

	public void setSum(BigDecimal sum) {
		this.sum = sum;
	}

	public TaxType getTaxType() {
		return taxType;
	}

	public void setTaxType(TaxType taxType) {
		this.taxType = taxType;
	}

	public PaymentSchema getPaymentSchema() {
		return paymentSchema;
	}

	public void setPaymentSchema(PaymentSchema paymentSchema) {
		this.paymentSchema = paymentSchema;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public List<IWork> getWorks() {
		return works;
	}

	public void setWorks(List<IWork> works) {
		this.works = works;
	}

	public IContract getContract() {
		return contract;
	}

	public void setContract(IContract contract) {
		this.contract = contract;
	}
}
