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
import ru.gzpn.spc.csl.model.interfaces.IWorkSet;

@Entity
@NamedQueries({ 
})
@Table(schema="spc_csl_schema", name="works",
indexes = {
		@Index(name = "spc_csl_idx_wkcode", columnList = "code", unique = true),
		@Index(name = "spc_csl_idx_wkname", columnList = "name"),
		@Index(name = "spc_csl_idx_wktype", columnList = "type"),
		@Index(name = "spc_csl_idx_wkplnobj", columnList = "plan_obj_id"),
		@Index(name = "spc_csl_idx_wkest", columnList = "lest_id"),
		@Index(name = "spc_csl_idx_wkmil", columnList = "milst_id")
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
	@JoinColumn(name = "lest_id", referencedColumnName = "id")
	private ILocalEstimate localEstimate;
	
	@OneToMany(targetEntity = Document.class)
	@JoinColumn(name = "wk_id", referencedColumnName = "id")
	private List<IDocument> documents;
	
	@ManyToOne(targetEntity = Milestone.class)
	@JoinColumn(name = "milst_id", referencedColumnName = "id")
	private IMilestone milestone;
	
	@ManyToOne(targetEntity = WorkSet.class)
	@JoinColumn(name = "wkset_id", referencedColumnName = "id")
	private IWorkSet workSet;
	
	public Work() {
	}
	
	public Work(String code, String name, WorkType workType) {
		this.code = code;
		this.name = name;
		this.type = workType;
	}

	@Override
	public ILocalEstimate getLocalEstimate() {
		return localEstimate;
	}

	@Override
	public void setLocalEstimate(ILocalEstimate localEstimate) {
		this.localEstimate = localEstimate;
	}

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
	public WorkType getType() {
		return type;
	}

	@Override
	public void setType(WorkType type) {
		this.type = type;
	}

	@Override
	public IPlanObject getPlanObj() {
		return planObj;
	}

	@Override
	public void setPlanObj(IPlanObject planObj) {
		this.planObj = planObj;
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
	public IMilestone getMilestone() {
		return milestone;
	}

	@Override
	public void setMilestone(IMilestone milestone) {
		this.milestone = milestone;
	}

	@Override
	public IWorkSet getWorkSet() {
		return workSet;
	}
	
	@Override
	public void setWorkSet(IWorkSet workSet) {
		this.workSet = workSet;
	}

	@Override
	public LocalDate getBeginDate() {
		return beginDate;
	}

	@Override
	public void setBeginDate(LocalDate beginDate) {
		this.beginDate = beginDate;
	}

	@Override
	public LocalDate getEndDate() {
		return endDate;
	}

	@Override
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
}
