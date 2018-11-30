package ru.gzpn.spc.csl.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ru.gzpn.spc.csl.model.enums.WorkType;
import ru.gzpn.spc.csl.model.interfaces.IDocument;
import ru.gzpn.spc.csl.model.interfaces.ILocalEstimate;
import ru.gzpn.spc.csl.model.interfaces.IMilestone;
import ru.gzpn.spc.csl.model.interfaces.IPlanObject;
import ru.gzpn.spc.csl.model.interfaces.IWork;

@Entity
@NamedQueries({ 
})
@Table(schema="spc_csl_schema", name="works",
indexes = {
		@Index(name = "spc_csl_idx_wkid", columnList = "code", unique = true),
		@Index(name = "spc_csl_idx_wkname", columnList = "name"),
		@Index(name = "spc_csl_idx_wktype", columnList = "type"),
		@Index(name = "spc_csl_idx_wkplnobj", columnList = "planObj"),
		@Index(name = "spc_csl_idx_wkest", columnList = "localEstimate"),
		@Index(name = "spc_csl_idx_wkdoc", columnList = "document"),
		@Index(name = "spc_csl_idx_wkmil", columnList = "milestone")
})
/**
 * Design and survey works
 */
public class Work extends BaseEntity implements IWork, Serializable {
	private static final long serialVersionUID = -7299274432662352949L;
	public static final String FIELD_NAME = "name";
	public static final String FIELD_CODE = "code";
	public static final String FIELD_TYPE = "type";
	
	@Column(length=16)
	private String code;
	private String name;
	private WorkType type;
	private LocalDate beginDate;
	private LocalDate endDate;
	
	@ManyToOne(targetEntity = PlanObject.class, fetch = FetchType.LAZY)
	@JoinColumn(name="plan_obj_id", referencedColumnName="id")
	private IPlanObject planObj;

	@ManyToOne(targetEntity = LocalEstimate.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "est_id", referencedColumnName = "id")
	private ILocalEstimate localEstimate;
	
	@OneToMany(targetEntity = Document.class)
	@JoinColumn(name = "id", referencedColumnName = "doc_id")
	private List<IDocument> documents;
	
	@ManyToOne(targetEntity = Milestone.class)
	@JoinColumn(name = "milst_id", referencedColumnName = "id")
	private IMilestone milestone;
	
	public Work() {
	}
	
	public Work(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public ILocalEstimate getLocalEstimate() {
		return localEstimate;
	}

	public void setLocalEstimate(ILocalEstimate localEstimate) {
		this.localEstimate = localEstimate;
	}

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

	public WorkType getType() {
		return type;
	}

	public void setType(WorkType type) {
		this.type = type;
	}

	public IPlanObject getPlanObj() {
		return planObj;
	}

	public void setPlanObj(IPlanObject planObj) {
		this.planObj = planObj;
	}

	public List<IDocument> getDocuments() {
		return documents;
	}

	public void setDocuments(List<IDocument> documents) {
		this.documents = documents;
	}

	public IMilestone getMilestone() {
		return milestone;
	}

	public void setMilestone(IMilestone milestone) {
		this.milestone = milestone;
	}

	public LocalDate getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(LocalDate beginDate) {
		this.beginDate = beginDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
}
