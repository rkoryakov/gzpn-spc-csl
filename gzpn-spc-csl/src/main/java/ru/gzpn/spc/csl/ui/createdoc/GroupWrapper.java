package ru.gzpn.spc.csl.ui.createdoc;

public class GroupWrapper {
	private String entityName;
	private String groupFiled;
	private String value;
	private GroupWrapper parent;
	private GroupWrapper child;
	
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public GroupWrapper getParent() {
		return parent;
	}

	public void setParent(GroupWrapper parent) {
		this.parent = parent;
	}

	public GroupWrapper getChild() {
		return child;
	}

	public void setChild(GroupWrapper child) {
		this.child = child;
	}
			
	public GroupWrapper addChild(GroupWrapper child) {
		return this.child = child;
	}
	
	public boolean isRoot() {
		return this.parent == null;
	}
	
	public boolean hasChild() {
		return this.child != null;
	}
}