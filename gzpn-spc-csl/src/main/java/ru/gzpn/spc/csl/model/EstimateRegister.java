package ru.gzpn.spc.csl.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ru.gzpn.spc.csl.model.interfaces.IEstimateRegister;

@Entity
@Table(schema = "spc_csl_schema", name = "estimate_register",
indexes = {
@Index(name = "spc_csl_idx_escreg", columnList = "est_reg_id")
})
public class EstimateRegister extends BaseEntity implements IEstimateRegister {
	@Column(length=64)
	private String user;
	
	@ManyToOne(targetEntity = EstimateCalculation.class)
	@JoinColumn(name = "est_reg_id", referencedColumnName = "id")
	private List<IEstimateRegister> estimateRegisters;
	
	@Override
	public String getUser() {
		return user;
	}
	
	@Override
	public void setUser(String user) {
		this.user = user;
	}
}
