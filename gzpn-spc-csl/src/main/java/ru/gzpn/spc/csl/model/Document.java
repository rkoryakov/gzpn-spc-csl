package ru.gzpn.spc.csl.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ru.gzpn.spc.csl.model.enums.DocType;
import ru.gzpn.spc.csl.model.interfaces.IDocument;
import ru.gzpn.spc.csl.model.interfaces.ILocalEstimate;
import ru.gzpn.spc.csl.model.interfaces.IWork;
import ru.gzpn.spc.csl.model.interfaces.IWorkSet;

@Entity
@Table(name = "documents", schema = "spc_csl_schema", 
indexes = {
	@Index(name = "spc_csl_idx_doccode", columnList = "code"),
	@Index(name = "spc_csl_idx_docname", columnList = "name"), 
	@Index(name = "spc_csl_idx_doctype", columnList = "type"),
	@Index(name = "spc_csl_idx_doctowk", columnList = "wk_id"),
	@Index(name = "spc_csl_idx_doctows", columnList = "ws_id")
})
public class Document extends BaseEntity implements IDocument, Serializable {
	private static final long serialVersionUID = -5925781857213642590L;
	
	private String code;
	private String name;
	private DocType type;
	
	@OneToMany(targetEntity = LocalEstimate.class)
	@JoinColumn(name = "doc_id", referencedColumnName = "id")
	List<ILocalEstimate> localEstimates;
	
	@ManyToOne(targetEntity = Work.class)
	@JoinColumn(name = "wk_id", referencedColumnName = "id")
	IWork work;

	@ManyToOne(targetEntity = WorkSet.class)
	@JoinColumn(name = "ws_id", referencedColumnName = "id")
	IWorkSet workset;
	
	@Override
	public String getCode() {
		return code;
	}

	@Override
	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public DocType getType() {
		return type;
	}

	@Override
	public void setType(DocType type) {
		this.type = type;
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
	public List<ILocalEstimate> getLocalEstimates() {
		return localEstimates;
	}

	@Override
	public void setLocalEstimates(List<ILocalEstimate> localEstimates) {
		this.localEstimates = localEstimates;
	}
	
	@Override
	public IWork getWork() {
		return work;
	}

	@Override
	public void setWork(IWork work) {
		this.work = work;
	}

	@Override
	public IWorkSet getWorkset() {
		return workset;
	}

	@Override
	public void setWorkset(IWorkSet workset) {
		this.workset = workset;
	}
}
