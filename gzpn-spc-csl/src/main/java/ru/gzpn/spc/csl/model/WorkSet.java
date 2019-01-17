package ru.gzpn.spc.csl.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import ru.gzpn.spc.csl.model.interfaces.IDocument;
import ru.gzpn.spc.csl.model.interfaces.IPlanObject;
import ru.gzpn.spc.csl.model.interfaces.IWork;
import ru.gzpn.spc.csl.model.interfaces.IWorkSet;

@Entity
@Table(schema = "spc_csl_schema", name = "workset", 
indexes = {
		@Index(name = "spc_csl_idx_wkscode", columnList = "code"),
		@Index(name = "spc_csl_idx_wkscode", columnList = "name"),
		@Index(name = "spc_csl_idx_wkpln", columnList = "plan_obj_id"),
		@Index(name = "spc_csl_idx_wkpln", columnList = "pir_id"),
		@Index(name = "spc_csl_idx_wkpln", columnList = "smr_id")
})
public class WorkSet extends BaseEntity implements IWorkSet, Comparable<IWorkSet>, Serializable {
	private static final long serialVersionUID = -1489774086979019274L;
	
	@Column(length = 64)
	private String code;
	
	@Column(length = 256)
	private String name;
	
	@OneToOne(targetEntity = Work.class)
	@JoinColumn(name = "pir_id", referencedColumnName = "id")
	private IWork pir;
	
	@OneToOne(targetEntity = Work.class)
	@JoinColumn(name = "smr_id", referencedColumnName = "id")
	private IWork smr;
	
	@ManyToOne(targetEntity = PlanObject.class)
	@JoinColumn(name = "plan_obj_id", referencedColumnName = "id")
	private IPlanObject planObject;
	
	@OneToMany(targetEntity = Document.class)
	@JoinColumn(name = "ws_id", referencedColumnName = "id")
	private List<IDocument> documents;
	
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
	public IWork getPir() {
		return pir;
	}
	
	@Override
	public String getPirCaption() {
		String result = "---";
		if (Objects.nonNull(getPir())) {
			result = getPir().getCode();
		}
		return result;
	}
	
	@Override
	public void setPir(IWork pir) {
		this.pir = pir;
	}
	
	@Override
	public IWork getSmr() {
		return smr;
	}
	
	@Override
	public String getSmrCaption() {
		String result = "---";
		if (Objects.nonNull(getSmr())) {
			result = getSmr().getCode();
		}
		return result;
	}
	
	@Override
	public void setSmr(IWork smr) {
		this.smr = smr;
	}
	
	@Override
	public IPlanObject getPlanObject() {
		return planObject;
	}
	
	@Override
	public void setPlanObject(IPlanObject planObject) {
		this.planObject = planObject;
	}
	
	@Override
	public List<IDocument> getDocuments() {
		return documents;
	}
	
	@Override
	public void setDocuments(List<IDocument> documents) {
		this.documents = documents;
	}

	@Override
	public String toString() {
		return "WorkSet [code=" + code + ", name=" + name + ", pir=" + pir + ", smr=" + smr + ", planObject=" + planObject + "]";
	}
	
	@Override
	public int compareTo(IWorkSet o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
