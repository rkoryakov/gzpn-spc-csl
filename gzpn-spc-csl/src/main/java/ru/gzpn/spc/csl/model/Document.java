package ru.gzpn.spc.csl.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import ru.gzpn.spc.csl.model.interfaces.IDocument;
import ru.gzpn.spc.csl.model.interfaces.ILocalEstimate;
import ru.gzpn.spc.csl.model.interfaces.IWork;

@Entity
@Table(name = "documents", schema = "spc_csl_schema", 
indexes = {
	@Index(name = "spc_csl_idx_doccode", columnList = "code", unique = true),
	@Index(name = "spc_csl_idx_docname", columnList = "name"), 
	@Index(name = "spc_csl_idx_doctype", columnList = "type"),
	@Index(name = "spc_csl_idx_doctoest", columnList = "localEstimate"),
	@Index(name = "spc_csl_idx_doctowk", columnList = "workList")
})
public class Document extends BaseEntity implements IDocument, Serializable {
	private static final long serialVersionUID = -5925781857213642590L;
	
	private String code;
	private String name;
	private DocTypeEnum type;
	
	@OneToOne(targetEntity=LocalEstimate.class)
	@JoinColumn(name = "doc_id", referencedColumnName = "id")
	ILocalEstimate localEstimate;
	
	@OneToMany(targetEntity=Work.class)
	@JoinColumn(name = "doc_id", referencedColumnName = "id")
	List<IWork> workList;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public DocTypeEnum getType() {
		return type;
	}

	public void setType(DocTypeEnum type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ILocalEstimate getLocalEstimate() {
		return localEstimate;
	}

	public void setLocalEstimate(ILocalEstimate localEstimate) {
		this.localEstimate = localEstimate;
	}

	public List<IWork> getWorkList() {
		return workList;
	}

	public void setWorkList(List<IWork> workList) {
		this.workList = workList;
	}
}
