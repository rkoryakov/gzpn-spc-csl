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
	private Object groupFiledValue;
	private NodeWrapper parent;
	private NodeWrapper child;
	private BaseEntity item; 
	// if the current node isn't a group then fetch entities
	private BaseEntity fetchedData;
	
	public NodeWrapper(String entityName, String groupByFiled) {
		this.entityName = entityName;
		this.groupFiled = groupByFiled;
	}
	
	public NodeWrapper(String entityName) {
		this.entityName = entityName;
		this.groupFiled = null;
	}
	
	public NodeWrapper(String entityName, String groupByFiledName, Object groupFiledValue) {
		this.entityName = entityName;
		this.groupFiled = groupByFiledName;
		this.groupFiledValue = groupFiledValue;
	}
	
	public NodeWrapper(String entityName, BaseEntity item) {
		this.entityName = entityName;
		this.setItem(item);
	}
	
	public String getEntityName() {
		return entityName;
	}
	
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	
	public String getGroupFiled() {
		return groupFiled;
	}
	
	public void setGroupFiled(String groupByFiled) {
		this.groupFiled = groupByFiled;
	}

	public Object getGroupFiledValue() {
		return groupFiledValue;
	}

	public void setGroupFiledValue(Object value) {
		this.groupFiledValue = value;
	}

	public boolean hasGroupFieldValue() {
		return getGroupFiledValue() != null;
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
			
	public BaseEntity getItem() {
		return item;
	}

	public void setItem(BaseEntity item) {
		this.item = item;
	}

	public NodeWrapper addChild(NodeWrapper child) {
		child.setParent(this);
		this.child = child;
		return child;
	}
	
	public boolean isGrouping() {
		return getGroupFiled() != null;
	}
	
	public boolean isRoot() {
		return this.parent == null;
	}
	
	public boolean hasChild() {
		return this.child != null;
	}
	
	public boolean hasParent() {
		return this.parent != null;
	}
	
	public boolean hasEntityItem() {
		return this.item != null;
	}
}