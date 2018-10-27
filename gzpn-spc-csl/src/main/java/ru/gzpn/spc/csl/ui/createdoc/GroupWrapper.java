package ru.gzpn.spc.csl.ui.createdoc;

public class GroupWrapper {
	private String entityName;
	private String groupFiled;
	
	public GroupWrapper(String entityName, String groupByFiled) {
		super();
		this.entityName = entityName;
		this.groupFiled = groupByFiled;
	}
	
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public String getGroupByFiled() {
		return groupFiled;
	}
	public void setGroupByFiled(String groupByFiled) {
		this.groupFiled = groupByFiled;
	}
}