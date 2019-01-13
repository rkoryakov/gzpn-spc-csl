package ru.gzpn.spc.csl.model.jsontypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ColumnHeaderGroup implements Serializable {
	private static final long serialVersionUID = -3840134886286859982L;
	
	/* if null then this group is a simple column */
	String caption;
	List<ColumnHeaderGroup> children;
	/* not null if this header doesn't have child headers */
	List<ColumnSettings> columns;
	
	public ColumnHeaderGroup() {
		
	}
	
	public ColumnHeaderGroup(String caption) {
		this.caption = caption;
	}
	
	public boolean hasChildrenGroups() {
		return children != null;
	}
	
	public boolean hasColumns() {
		return columns != null;
	}
	
	public String getCaption() {
		return caption;
	}
	
	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	public List<ColumnHeaderGroup> getChildren() {
		return children;
	}
	
	public void setChildren(List<ColumnHeaderGroup> children) {
		this.children = children;
	}
	
	public List<ColumnSettings> getColumns() {
		return columns;
	}
	
	public ColumnHeaderGroup addColumn(ColumnSettings column) {
		if (Objects.isNull(columns)) {
			columns = new ArrayList<>();
		}
		columns.add(column);
		
		return this;
	}
	
	public void setColumns(List<ColumnSettings> columns) {
		this.columns = columns;
	}
}
