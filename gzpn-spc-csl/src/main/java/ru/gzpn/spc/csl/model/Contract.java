package ru.gzpn.spc.csl.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import ru.gzpn.spc.csl.model.interfaces.IContract;

@Entity
@Table(schema="spc_csl_schema", name="contract",
indexes = {
		@Index(name = "spc_csl_idx_cntname", columnList = "name", unique = true),
		@Index(name = "spc_csl_idx_cntcode", columnList = "code", unique = true)
})
public class Contract extends BaseEntity implements IContract, Serializable {
	private static final long serialVersionUID = 3446433626324831206L;
	
	private String name;
	private String code;
	private LocalDate signingDate;
	
	private String customerName;
	private String customerINN;
	private String custormerBank;
	
	private String executorName;
	private String executorINN;
	private String executorBank;
}
