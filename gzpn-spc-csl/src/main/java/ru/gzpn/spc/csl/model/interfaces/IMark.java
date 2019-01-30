package ru.gzpn.spc.csl.model.interfaces;

import java.util.List;

public interface IMark {

	void setName(String name);
	String getName();
	
	List<IPlanObject> getPlanObjects();
	void setPlanObjects(List<IPlanObject> planObjects);

}
