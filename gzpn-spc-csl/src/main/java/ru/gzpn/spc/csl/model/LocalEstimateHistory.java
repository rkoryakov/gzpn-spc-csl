package ru.gzpn.spc.csl.model;

import java.io.Serializable;
import java.time.ZonedDateTime;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.Table;

import ru.gzpn.spc.csl.model.interfaces.ILocalEstimate;
import ru.gzpn.spc.csl.model.interfaces.ILocalEstimateHistory;

@Entity
@NamedQueries({
})
@Table(schema = "spc_csl_schema", name = "local_estimates_hist",
indexes = {
		@Index(name = "spc_csl_idx_esthcode", columnList = "code", unique = true),
		@Index(name = "spc_csl_idx_esthname", columnList = "name"),
		@Index(name = "spc_csl_idx_esthdoc", columnList = "doc_id"),
		@Index(name = "spc_csl_idx_esthstg", columnList = "stage_id"),
		@Index(name = "spc_csl_idx_esthapp", columnList = "approvedBy"),
		@Index(name = "spc_csl_idx_esthid", columnList = "esthist_id")
})
public class LocalEstimateHistory extends LocalEstimate implements ILocalEstimateHistory, Serializable {
	private String approvedBy;
	private ZonedDateTime approveDate;
	
	@ManyToOne(targetEntity = LocalEstimate.class)
	@JoinColumn(name = "esthist_id", referencedColumnName = "id")
	private ILocalEstimate actual;
	
	public String getApprovedBy() {
		return approvedBy;
	}
	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}
	public ZonedDateTime getApproveDate() {
		return approveDate;
	}
	public void setApproveDate(ZonedDateTime approveDate) {
		this.approveDate = approveDate;
	}
	public ILocalEstimate getActual() {
		return actual;
	}
	public void setActual(ILocalEstimate actual) {
		this.actual = actual;
	}
}
