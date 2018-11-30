package ru.gzpn.spc.csl.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ru.gzpn.spc.csl.model.interfaces.IWork;

@Entity
@Table(schema = "spc_csl_schema", name = "workset", 
indexes = {
		@Index(name = "spc_csl_idx_wkscode", columnList = "code"),
		@Index(name = "spc_csl_idx_wkscode", columnList = "name"),
		@Index(name = "spc_csl_idx_wkspir", columnList = "pir"),
		@Index(name = "spc_csl_idx_wkssmr", columnList = "smr")
})
public class WorkSet extends BaseEntity implements Serializable {
	private static final long serialVersionUID = -1489774086979019274L;
	@Column(length = 64)
	private String code;
	@Column(length = 256)
	private String name;
	@OneToMany(targetEntity = Work.class)
	@JoinColumn(name = "id",  referencedColumnName = "wkset_id")
	private List<IWork> pir;
	@OneToMany(targetEntity = Work.class)
	@JoinColumn(name = "id",  referencedColumnName = "wkset_id")
	private List<IWork> smr;
	
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
	public List<IWork> getPir() {
		return pir;
	}
	public void setPir(List<IWork> pir) {
		this.pir = pir;
	}
	public List<IWork> getSmr() {
		return smr;
	}
	public void setSmr(List<IWork> smr) {
		this.smr = smr;
	}
}
