package ru.gzpn.spc.csl.model.jsontypes;

import java.io.Serializable;
import java.util.List;

public class PhaseJson implements Serializable {
	private List<Ph> phases;

	public List<Ph> getPhases() {
		return phases;
	}

	public void setPhases(List<Ph> phases) {
		this.phases = phases;
	}
}

class Ph implements Serializable {
	private String id;
	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}