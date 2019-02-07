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
import ru.gzpn.spc.csl.model.interfaces.IEstimateCost;
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
		@Index(name = "spc_csl_idx_esthead", columnList = "est_head_id"),
		@Index(name = "spc_csl_idx_estcalc", columnList = "est_calc_id")
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
	@JoinColumn(name = "lest_id", referencedColumnName = "id")
	private List<IWork> works;

	@ManyToOne(targetEntity = Document.class)
	@JoinColumn(name = "doc_id", referencedColumnName = "id")
	private IDocument document; // designation
	
	@ManyToOne(targetEntity = Stage.class)
	@JoinColumn(name = "stage_id", referencedColumnName = "id")
	private IStage stage;
	
	@OneToMany(targetEntity = LocalEstimateHistory.class)
	@JoinColumn(name = "esthist_id", referencedColumnName = "id")
	private List<ILocalEstimateHistory> history;
	
	@ManyToOne(targetEntity = EstimateCalculation.class)
	@JoinColumn(name = "est_calc_id", referencedColumnName = "id")
	private IEstimateCalculation estimateCalculation;
	
	@ManyToOne(targetEntity = ObjectEstimate.class)
	@JoinColumn(name = "oest_id", referencedColumnName = "id")
	private IObjectEstimate objectEstimate;
	
	@ManyToOne(targetEntity = EstimateHead.class)
	@JoinColumn(name = "est_head_id", referencedColumnName = "id")
	private IEstimateHead estimateHead;
	
	@OneToMany(targetEntity = EstimateCost.class)
	@JoinColumn(name = "lest_id", referencedColumnName = "id")
	private List<IEstimateCost> estimateCosts;
	
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
	public List<IWork> getWorks() {
		return works;
	}

	@Override
	public void setWorks(List<IWork> works) {
		this.works = works;
	}

	@Override
	public IDocument getDocument() {
		return document;
	}

	@Override
	public void setDocument(IDocument document) {
		this.document = document;
	}

	@Override
	public String getChangedBy() {
		return changedBy;
	}

	@Override
	public void setChangedBy(String changedBy) {
		this.changedBy = changedBy;
	}

	@Override
	public String getDrawing() {
		return drawing;
	}

	@Override
	public void setDrawing(String drawing) {
		this.drawing = drawing;
	}

	@Override
	public LocalEstimateStatus getStatus() {
		return status;
	}

	@Override
	public void setStatus(LocalEstimateStatus status) {
		this.status = status;
	}

	@Override
	public String getComment() {
		return comment;
	}

	@Override
	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public IStage getStage() {
		return stage;
	}

	@Override
	public void setStage(IStage stage) {
		this.stage = stage;
	}

	@Override
	public IEstimateCalculation getEstimateCalculation() {
		return estimateCalculation;
	}
	
	@Override
	public void setEstimateCalculation(IEstimateCalculation estimateCalculation) {
		this.estimateCalculation = estimateCalculation;
	}

	@Override
	public IObjectEstimate getObjectEstimate() {
		return objectEstimate;
	}

	@Override
	public void setObjectEstimate(IObjectEstimate objectEstimate) {
		this.objectEstimate = objectEstimate;
	}

	@Override
	public List<ILocalEstimateHistory> getHistory() {
		return history;
	}

	@Override
	public void setHistory(List<ILocalEstimateHistory> history) {
		this.history = history;
	}

	@Override
	public IEstimateHead getEstimateHead() {
		return estimateHead;
	}

	@Override
	public void setEstimateHead(IEstimateHead estimateHead) {
		this.estimateHead = estimateHead;
	}

	@Override
	public List<IEstimateCost> getEstimateCosts() {
		return estimateCosts;
	}

	@Override
	public void setEstimateCosts(List<IEstimateCost> estimateCosts) {
		this.estimateCosts = estimateCosts;
	}
}