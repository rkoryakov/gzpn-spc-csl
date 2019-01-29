package ru.gzpn.spc.csl.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import ru.gzpn.spc.csl.model.interfaces.IMark;

@Entity
@Table(schema = "spc_csl_schema", name = "marks")
public class Mark extends BaseEntity implements IMark {
	private String name;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}
}
