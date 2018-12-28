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
	
	public Boolean isShown() {
		return isShown;
	}

	public void setShown(Boolean isShown) {
		this.isShown = isShown;
	}

	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}
}
