package ru.gzpn.spc.csl.model.utils;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vaadin.data.provider.SortOrder;

import ru.gzpn.spc.csl.model.BaseEntity;
import ru.gzpn.spc.csl.model.enums.Entities;
import ru.gzpn.spc.csl.model.interfaces.IBaseEntity;

/**
 * Holds the information about the current entity(node) and grouping fields.
 * Used while creating tree structure entities where a node is an entity or 
 * is a group by some field of the current entity
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class NodeWrapper implements Serializable {
	private static final long serialVersionUID = -6142105774113139782L;
	public static final Logger logger = LoggerFactory.getLogger(NodeWrapper.class);

	private String entityName;
	private String groupFiled;
	
	@JsonIgnore
	private Object groupFiledValue;
	
	@JsonIgnore
	private NodeWrapper parent; // parent level for query data
	
	private NodeWrapper child; // child level for query data
	
	@JsonIgnore
	private BaseEntity item; // fetched data if the current node isn't a group
	@JsonIgnore
	private Long id;
	@JsonIgnore
	private List<SortOrder<String>> sortOrdersForChildren;
	@JsonIgnore
	private NodeFilter filterForChildren;
	@JsonIgnore
	private int hashCode;

	protected NodeWrapper() {
	}
	
	public NodeWrapper(String entityName, String groupByFiled) {
		this();
		this.entityName = entityName;
		this.groupFiled = groupByFiled;
	}
	
	public NodeWrapper(String entityName) {
		this();
		this.entityName = entityName;
		this.groupFiled = null;
	}
	
	public NodeWrapper(String entityName, String groupByFiledName, Object groupFiledValue) {
		this();
		this.entityName = entityName;
		this.groupFiled = groupByFiledName;
		this.groupFiledValue = groupFiledValue;
	}
	
	public NodeWrapper(String entityName, String groupByFiledName, Object groupFiledValue, Long id) {
		this();
		this.entityName = entityName;
		this.groupFiled = groupByFiledName;
		this.groupFiledValue = groupFiledValue;
		this.id = id;
	}
	
	public NodeWrapper(String entityName, BaseEntity item, Long id) {
		this();
		this.entityName = entityName;
		this.setItem(item);
		this.id = id;
	}
	
	public void generateHashCode() {
		int result = 1;
		
		result = result ^ ((entityName == null) ? 10 : entityName.hashCode());
		result = result ^ ((groupFiled == null) ? 10 : groupFiled.hashCode());
		result = result ^ ((groupFiledValue == null) ? 10 : groupFiledValue.toString().hashCode());
		result = result ^ ((item == null) ? 10 : item.hashCode());
		
		if (hasParent()) {
			result = result ^ getParent().hashCode;
		}
		
		this.hashCode = result;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Caption for rendering in UI tree
	 */
	@JsonIgnore
	public String getNodeCaption() {
		String result = "";
		if (isGrouping()) {
			result = getGroupFiledValue().toString();
		}
		
		return result;
	}
	
	/**
	 * Represent Entity name + field name
	 */
	@JsonIgnore
	public String getNodeSettingsCaption(MessageSource messageSource, Locale locale) {
		Entities entity = Entities.valueOf(entityName.toUpperCase());
		String entityCaption = entity.getText(messageSource, locale);
		String fieldCaption = "";
		
		switch (groupFiled) {
		case IBaseEntity.FIELD_ID:
		case IBaseEntity.FIELD_VERSION:
		case IBaseEntity.FIELD_CHANGE_DATE:
		case IBaseEntity.FIELD_CREATE_DATE:
			fieldCaption = messageSource.getMessage("Entities.BaseEntity." + groupFiled, null, groupFiled, locale);
		}
		
		fieldCaption = messageSource.getMessage("Entities." + entityName  + "." + groupFiled, null, groupFiled, locale);
		return entityCaption + " - " + fieldCaption;
	}
	
	public String getEntityName() {
		return entityName;
	}
	
	@JsonIgnore
	public Entities getEntityEnum() {
		return Entities.valueOf(getEntityName().toUpperCase());
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

	@JsonIgnore
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
	
	@JsonIgnore
	public boolean isGrouping() {
		return getGroupField() != null;
	}
	
	@JsonIgnore
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
	
	public boolean hasId() {
		return this.id != null && this.id != -1;
	}
	
	@JsonIgnore
	public List<SortOrder<String>> getSortOredersForChildren() {
		return sortOrdersForChildren;
	}
	
	@JsonIgnore
	public void setSortOredersForChildren(List<SortOrder<String>> sortOrdersForChildren) {
		this.sortOrdersForChildren = sortOrdersForChildren;
	}
	
	public NodeFilter getFilterForChildren() {
		return filterForChildren;
	}

	public void setFilterForChildren(NodeFilter filterForChildren) {
		this.filterForChildren = filterForChildren;
	}

	@Override
	public int hashCode() {
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
	
		NodeWrapper other = (NodeWrapper) obj;
	
		if (entityName == null) {
			if (other.entityName != null) {
				return false;
			}
		} else if (!entityName.equals(other.entityName)) {
			return false;
		}
		if (groupFiled == null) {
			if (other.groupFiled != null) {
				return false;
			}
		} else if (!groupFiled.equals(other.groupFiled)) {
			return false;
		}
		if (groupFiledValue == null) {
			if (other.groupFiledValue != null) {
				return false;
			}
		} else if (!groupFiledValue.equals(other.groupFiledValue)) {
			return false;
		}
		if (item == null) {
			if (other.item != null)
				return false;
		} else if (!item.equals(other.item)) {
			return false;
		}
		
		return other.hashCode() == this.hashCode();
	}

	@Override
	public String toString() {
		return "NodeWrapper [entityName=" + entityName + ", groupFiled=" + groupFiled + ", groupFiledValue="
				+ groupFiledValue + ", item=" + item + ", hasParent()=" + hasParent() + ", hasChild()=" + hasChild() + "]";
	}
}