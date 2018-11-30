package ru.gzpn.spc.csl.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import ru.gzpn.spc.csl.model.enums.DocType;
import ru.gzpn.spc.csl.model.interfaces.IDocument;
import ru.gzpn.spc.csl.model.interfaces.ILocalEstimate;
import ru.gzpn.spc.csl.model.interfaces.IWork;

@Entity
@Table(name = "documents", schema = "spc_csl_schema", 
indexes = {
	@Index(name = "spc_csl_idx_doccode", columnList = "code", unique = true),
	@Index(name = "spc_csl_idx_docname", columnList = "name"), 
	@Index(name = "spc_csl_idx_doctype", columnList = "type"),
	@Index(name = "spc_csl_idx_doctoest", columnList = "est_id"),
	@Index(name = "spc_csl_idx_doctowk", columnList = "wk_id")
})
public class Document extends BaseEntity implements IDocument, Serializable {
	private static final long serialVersionUID = -5925781857213642590L;
	
	private String code;
	private String name;
	private DocType type;
	
	@OneToOne(targetEntity = LocalEstimate.class)
	@JoinColumn(name = "est_id", referencedColumnName = "id")
	ILocalEstimate localEstimate;
	
	@ManyToOne(targetEntity = Work.class)
	@JoinColumn(name = "wk_id", referencedColumnName = "id")
	IWork work;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public DocType getType() {
		return type;
	}

	public void setType(DocType type) {
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

	public IWork getWork() {
		return work;
	}

	public void setWork(IWork work) {
		this.work = work;
	}
}
