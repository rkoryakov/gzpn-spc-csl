package ru.gzpn.spc.csl.model.jsontypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ColumnHeaderGroup implements Serializable {
	private static final long serialVersionUID = -3840134886286859982L;
	
	/* if null then this group is a simple column */
	private String caption;
	private boolean shown;
	
	public boolean isShown() {
		return shown;
	}

	public void setShown(boolean shown) {
		this.shown = shown;
	}

	List<ColumnHeaderGroup> children;
	/* not null if this header doesn't have child headers */
	List<ColumnSettings> columns;
	
	public ColumnHeaderGroup() {
		
	}
	
	public ColumnHeaderGroup(String caption) {
		this.caption = caption;
		this.shown = false;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((caption == null) ? 0 : caption.hashCode());
		result = prime * result + (shown ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		ColumnHeaderGroup other = (ColumnHeaderGroup) obj;
		if (caption == null) {
			if (other.caption != null)
				return false;
		} else if (!caption.equals(other.caption))
			return false;
		if (shown != other.shown)
			return false;
		return true;
	}


}
