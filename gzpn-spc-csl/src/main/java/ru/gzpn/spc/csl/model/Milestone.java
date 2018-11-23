package ru.gzpn.spc.csl.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import ru.gzpn.spc.csl.model.interfaces.ICProject;
import ru.gzpn.spc.csl.model.interfaces.IMilestone;
import ru.gzpn.spc.csl.model.interfaces.IWork;

@Entity
@Table(schema="spc_csl_schema", name="milestone",
indexes = {
		@Index(name = "spc_csl_idx_milname", columnList = "name", unique = true)
})
public class Milestone extends BaseEntity implements IMilestone, Serializable {
	private static final long serialVersionUID = 6247571288283340789L;

	@Column(length=256)
	private String name;
	@Column(length=256)
	private String code;
	
	private LocalDate startDate;
	private LocalDate endDate;
	
	@OneToOne(targetEntity=CProject.class)
	@JoinColumn(name="id", referencedColumnName="cp_id")
	private ICProject project;
	
	private LocalDate deliveryDate;
	private BigDecimal sum;
	private TaxType taxType; // must be a directory
	private PaymentSchema paymentSchema; // must be a directory
	private Currency currency; // must be a directory
	
	@ManyToOne(targetEntity = Work.class)
	@JoinColumn(name = "wk_id", referencedColumnName = "id")
	private IWork work;
}
