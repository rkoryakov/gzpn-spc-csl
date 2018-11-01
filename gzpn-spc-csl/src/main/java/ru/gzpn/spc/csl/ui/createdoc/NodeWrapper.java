package ru.gzpn.spc.csl.ui.createdoc;

import ru.gzpn.spc.csl.model.BaseEntity;

/**
 * Holds the information about the current entity(node) and grouping fields.
 * Used while creating tree structure entities where a node is an entity or 
 * is a group by some field of the current entity
 */
public class NodeWrapper {
	private String entityName;
	private String groupFiled;
	private String groupFiledValue;
	private NodeWrapper parent;
	private NodeWrapper child;
	// if the current node isn't a group then fetch entities
	private BaseEntity fetchedData;
	
	public NodeWrapper(String entityName, String groupByFiled) {
		this.entityName = entityName;
		this.groupFiled = groupByFiled;
	}
	
	public NodeWrapper(String entityName, String groupByFiledName, String groupFiledValue) {
		this.entityName = entityName;
		this.groupFiled = groupByFiledName;
		this.groupFiledValue = groupFiledValue;
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

	public String getGroupFiledValue() {
		return groupFiledValue;
	}

	public void setGroupFiledValue(String value) {
		this.groupFiledValue = value;
	}

	public NodeWrapper getParent() {
		return parent;
	}

	public void setParent(NodeWrapper parent) {
		this.parent = parent;
	}

	public NodeWrapper getChild() {
		return child;
	}

	public void setChild(NodeWrapper child) {
		this.child = child;
	}
			
	public NodeWrapper addChild(NodeWrapper child) {
		child.setParent(this);
		this.child = child;
		return child;
	}
	
	public boolean isGroup() {
		return getGroupByFiled() != null;
	}
	
	public boolean isRoot() {
		return this.parent == null;
	}
	
	public boolean hasChild() {
		return this.child != null;
	}
}