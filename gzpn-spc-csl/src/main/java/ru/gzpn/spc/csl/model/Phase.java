/**
 * 
 */
package ru.gzpn.spc.csl.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import ru.gzpn.spc.csl.model.interfaces.IPhase;
import ru.gzpn.spc.csl.model.jsontypes.PhaseJson;

@Entity
@Table(schema = "spc_csl_schema", name = "phases")
public class Phase extends BaseEntity implements IPhase {
	@Column
	@Type(type = "PhaseJsonType")
	private PhaseJson phase;

	public PhaseJson getPhase() {
		return phase;
	}

	public void setPhase(PhaseJson phase) {
		this.phase = phase;
	}

}
