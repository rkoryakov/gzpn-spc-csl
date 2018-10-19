package ru.gzpn.spc.csl.ui.createdoc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.gzpn.spc.csl.model.BaseEntity;

/**
 * Holds the information about the current entity(node) and grouping fields.
 * Used while creating tree structure entities where a node is an entity or 
 * is a group by some field of the current entity
 */
public abstract class NodeHolder {
	private static final Logger logger = LoggerFactory.getLogger(NodeHolder.class);
	private BaseEntity entity;
	private String groupFieldName;
	
	public void setCurrentEntity(BaseEntity entity) {
		this.entity = entity;
	}
	
	public BaseEntity getCurrentEntity() {
		return this.entity;
	}
	
	public void setCurrentGroupField(String fieldName) {
		this.groupFieldName = fieldName;
	}
	
	public String getCurrentGroupField() {
		return this.groupFieldName;
	}
}
