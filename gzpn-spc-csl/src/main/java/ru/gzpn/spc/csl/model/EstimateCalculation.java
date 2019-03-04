package ru.gzpn.spc.csl.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ru.gzpn.spc.csl.model.interfaces.ICProject;
import ru.gzpn.spc.csl.model.interfaces.IEstimateCalculation;
import ru.gzpn.spc.csl.model.interfaces.ILocalEstimate;
import ru.gzpn.spc.csl.model.interfaces.IObjectEstimate;

@Entity
@Table(schema = "spc_csl_schema", name = "estimate_calculations", 
indexes = {
			@Index(name = "spc_csl_idx_escname", columnList = "name"),
			@Index(name = "spc_csl_idx_esccode", columnList = "code"),
			@Index(name = "spc_csl_idx_eschandler", columnList = "handler"),
			@Index(name = "spc_csl_idx_escproj", columnList = "cp_id")
})
public class EstimateCalculation extends BaseEntity implements IEstimateCalculation, Serializable {
	private static final long serialVersionUID = -8670063022967614874L;
	@Column(length=32)
	private String code;
	private String name;
	private String handler;
	
	@ManyToOne(targetEntity = CProject.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "cp_id", referencedColumnName = "id")
	private ICProject project;

	@OneToMany(targetEntity = LocalEstimate.class)
	@JoinColumn(name = "est_calc_id", referencedColumnName = "id")
	private List<ILocalEstimate> localEstimates;
	
	@OneToMany(targetEntity = ObjectEstimate.class)
	@JoinColumn(name = "est_calc_id", referencedColumnName = "id")
	private List<IObjectEstimate> objectEstimates;
	
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

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public ICProject getProject() {
		return project;
	}

	public void setProject(ICProject project) {
		this.project = project;
	}

	public List<ILocalEstimate> getEstimates() {
		return localEstimates;
	}

	public void setEstimates(List<ILocalEstimate> estimates) {
		this.localEstimates = estimates;
	}

	public List<IObjectEstimate> getObjectEstimates() {
		return objectEstimates;
	}

	public void setObjectEstimates(List<IObjectEstimate> objectEstimates) {
		this.objectEstimates = objectEstimates;
	}

	@Override
	public String toString() {
		return "EstimateCalculation [code=" + code + ", name=" + name + ", handler=" + handler + ", project=" + project
				+ ", localEstimates=" + localEstimates + ", objectEstimates=" + objectEstimates + "]";
	}
	
	
}
