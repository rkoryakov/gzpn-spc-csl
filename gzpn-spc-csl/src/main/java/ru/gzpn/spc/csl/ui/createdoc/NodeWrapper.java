package ru.gzpn.spc.csl.ui.createdoc;

public class NodeWrapper {
	private String entityName;
	private String groupByFiled;
	
	public NodeWrapper(String entityName, String groupByFiled) {
		super();
		this.entityName = entityName;
		this.groupByFiled = groupByFiled;
	}
	
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public String getGroupByFiled() {
		return groupByFiled;
	}
	public void setGroupByFiled(String groupByFiled) {
		this.groupByFiled = groupByFiled;
	}
}