package ru.gzpn.spc.csl.ui.createdoc;

import java.io.Serializable;

import ru.gzpn.spc.csl.model.BaseEntity;

/**
 * Holds the information about the current entity(node) and grouping fields.
 * Used while creating tree structure entities where a node is an entity or 
 * is a group by some field of the current entity
 */
public class NodeWrapper implements Serializable {
	private static final long serialVersionUID = -6142105774113139782L;
	
	private String entityName;
	private String groupFiled;
	private Object groupFiledValue;
	private NodeWrapper parent; // parent level for query data
	private NodeWrapper child; // child level for query data
	private BaseEntity item; // fetched data if the current node isn't a group
	
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
	
	/**
	 * Caption for rendering in UI tree
	 */
	public String getNodeCaption() {
		String result = "";
		if (isGrouping()) {
			result = getGroupFiledValue().toString();
		}
		
		return result;
	}
	
	public String getEntityName() {
		return entityName;
	}
	
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	
	public String getGroupField() {
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
		return getGroupField() != null;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((entityName == null) ? 0 : entityName.hashCode());
		result = prime * result + ((groupFiled == null) ? 0 : groupFiled.hashCode());
		result = prime * result + ((groupFiledValue == null) ? 0 : groupFiledValue.hashCode());
		result = prime * result + ((item == null) ? 0 : item.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
	
		NodeWrapper other = (NodeWrapper) obj;
		
		if (entityName == null) {
			if (other.entityName != null)
				return false;
		} else if (!entityName.equals(other.entityName))
			return false;
		if (groupFiled == null) {
			if (other.groupFiled != null)
				return false;
		} else if (!groupFiled.equals(other.groupFiled))
			return false;
		if (groupFiledValue == null) {
			if (other.groupFiledValue != null)
				return false;
		} else if (!groupFiledValue.equals(other.groupFiledValue))
			return false;
		if (item == null) {
			if (other.item != null)
				return false;
		} else if (!item.equals(other.item))
			return false;
		
		return true;
	}

	@Override
	public String toString() {
		return "NodeWrapper [entityName=" + entityName + ", groupFiled=" + groupFiled + ", groupFiledValue="
				+ groupFiledValue + ", item=" + item + ", hasParent()=" + hasParent() + ", hasChild()=" + hasChild() + "]";
	}
	
	
}