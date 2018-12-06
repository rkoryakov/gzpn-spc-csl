package ru.gzpn.spc.csl.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ru.gzpn.spc.csl.model.enums.LocalEstimateStatus;
import ru.gzpn.spc.csl.model.interfaces.IDocument;
import ru.gzpn.spc.csl.model.interfaces.IEstimateCalculation;
import ru.gzpn.spc.csl.model.interfaces.IEstimateHead;
import ru.gzpn.spc.csl.model.interfaces.ILocalEstimate;
import ru.gzpn.spc.csl.model.interfaces.ILocalEstimateHistory;
import ru.gzpn.spc.csl.model.interfaces.IObjectEstimate;
import ru.gzpn.spc.csl.model.interfaces.IStage;
import ru.gzpn.spc.csl.model.interfaces.IWork;

@Entity
@NamedQueries({ 
})
@Table(schema="spc_csl_schema", name="local_estimates",
indexes = {
		@Index(name = "spc_csl_idx_estcode", columnList = "code", unique = true),
		@Index(name = "spc_csl_idx_estname", columnList = "name"),
		@Index(name = "spc_csl_idx_estdoc", columnList = "doc_id"),
		@Index(name = "spc_csl_idx_eststg", columnList = "stage_id"),
		@Index(name = "spc_csl_idx_estoest", columnList = "oest_id"),
		@Index(name = "spc_csl_idx_esthead", columnList = "est_head_id")
})
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class LocalEstimate extends BaseEntity implements ILocalEstimate, Serializable {
	private static final long serialVersionUID = -8027924404278676835L;
	public static final String FIELD_NAME = "name";
	public static final String FIELD_CODE = "code";
	
	@Column(length=64)
	private String code;
	private String name;
	private String changedBy;
	private String drawing;
	private LocalEstimateStatus status;
	private String comment;
	
	@OneToMany(targetEntity = Work.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "wk_id", referencedColumnName = "id")
	private List<IWork> works;

	@ManyToOne(targetEntity = Document.class)
	@JoinColumn(name = "doc_id", referencedColumnName = "id")
	private IDocument document; // designation
	
	@ManyToOne(targetEntity = Stage.class)
	@JoinColumn(name = "stage_id", referencedColumnName = "id")
	private IStage stage;
	
	@OneToMany(targetEntity = LocalEstimate.class)
	@JoinColumn(name = "esthist_id", referencedColumnName = "id")
	private List<ILocalEstimateHistory> history;
	
	@ManyToOne(targetEntity = EstimateCalculation.class)
	@JoinColumn(name = "id", referencedColumnName = "est_id")
	private IEstimateCalculation estimateCalculation;
	
	@ManyToOne(targetEntity = ObjectEstimate.class)
	@JoinColumn(name = "oest_id", referencedColumnName = "id")
	private IObjectEstimate objectEstimate;
	
	@ManyToOne(targetEntity = EstimateHead.class)
	@JoinColumn(name = "est_head_id", referencedColumnName = "id")
	private IEstimateHead estimateHead;
	
	public LocalEstimate() {
	}
	
	public LocalEstimate(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
	public LocalEstimate(String code, String name, List<IWork> works) {
		this.code = code;
		this.name = name;
		this.works = works;
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

	public List<IWork> getWorks() {
		return works;
	}

	public void setWorks(List<IWork> works) {
		this.works = works;
	}

	public IDocument getDocument() {
		return document;
	}

	public void setDocument(IDocument document) {
		this.document = document;
	}

	public String getChangedBy() {
		return changedBy;
	}

	public void setChangedBy(String changedBy) {
		this.changedBy = changedBy;
	}

	public String getDrawing() {
		return drawing;
	}

	public void setDrawing(String drawing) {
		this.drawing = drawing;
	}

	public LocalEstimateStatus getStatus() {
		return status;
	}

	public void setStatus(LocalEstimateStatus status) {
		this.status = status;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public IStage getStage() {
		return stage;
	}

	public void setStage(IStage stage) {
		this.stage = stage;
	}

	public IEstimateCalculation getEstimateCalculation() {
		return estimateCalculation;
	}

	public void setEstimateCalculation(IEstimateCalculation estimateCalculation) {
		this.estimateCalculation = estimateCalculation;
	}

	public IObjectEstimate getObjectEstimate() {
		return objectEstimate;
	}

	public void setObjectEstimate(IObjectEstimate objectEstimate) {
		this.objectEstimate = objectEstimate;
	}

	public List<ILocalEstimateHistory> getHistory() {
		return history;
	}

	public void setHistory(List<ILocalEstimateHistory> history) {
		this.history = history;
	}

	public IEstimateHead getEstimateHead() {
		return estimateHead;
	}

	public void setEstimateHead(IEstimateHead estimateHead) {
		this.estimateHead = estimateHead;
	}
}