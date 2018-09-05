package ru.gzpn.spc.csl.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "spc_csl_stages", schema = "spc_csl_schema")
public class Stage extends BaseEntity {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
