package ru.gzpn.spc.csl.model;

public enum Entities {
	HPROJECT("HProject"),
	CPROJECT("CProject"),
	PHASE("Phase"),
	STAGE("Stage");
	
	private String name;
	
	Entities(String name) {
		this.name= name;
	}
	
	public String getName() {
		return this.name();
	}
}
