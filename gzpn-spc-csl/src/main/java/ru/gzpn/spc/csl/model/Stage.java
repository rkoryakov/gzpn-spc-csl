package ru.gzpn.spc.csl.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import ru.gzpn.spc.csl.model.interfaces.IStage;

@Entity
@Table(schema = "spc_csl_schema", name = "stages")
public class Stage extends BaseEntity implements IStage {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
