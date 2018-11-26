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
		@Index(name = "spc_csl_idx_cntcode", columnList = "code" )
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
	
}
