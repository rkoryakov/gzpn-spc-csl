package ru.gzpn.spc.csl.model.jsontypes;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class ColumnSettings implements Serializable {
	private static final long serialVersionUID = -5285084276735085930L;
	
	private Double width;
	private String entityName;
	private String entityFieldName;
	private Integer orderIndex;
	private Boolean isShown;

	public ColumnSettings() {
	}

	public ColumnSettings(String entityName, String entityFieldName, Double width, Boolean isShown, Integer orderIndex) {
		this.width = width;
		this.entityName = entityName;
		this.entityFieldName = entityFieldName;
		this.orderIndex = orderIndex;
		this.isShown = isShown;
	}
	
	public Double getWidth() {
		return width;
	}
	public void setWidth(Double width) {
		this.width = width;
	}
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public String getEntityFieldName() {
		return entityFieldName;
	}
	public void setEntityFieldName(String entityFieldName) {
		this.entityFieldName = entityFieldName;
	}

	public int getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(int orderIndex) {
		this.orderIndex = orderIndex;
	}
	
	@JsonIgnoreProperties
	public Boolean isShown() {
		return isShown;
	}

	public void setShown(Boolean isShown) {
		this.isShown = isShown;
	}

	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((entityFieldName == null) ? 0 : entityFieldName.hashCode());
		result = prime * result + ((entityName == null) ? 0 : entityName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ColumnSettings other = (ColumnSettings) obj;
		if (entityFieldName == null) {
			if (other.entityFieldName != null)
				return false;
		} else if (!entityFieldName.equals(other.entityFieldName)) {
			return false;
		}
		if (entityName == null) {
			if (other.entityName != null)
				return false;
		} else if (!entityName.equals(other.entityName)) {
			return false;
		}
		return true;
	}
}
