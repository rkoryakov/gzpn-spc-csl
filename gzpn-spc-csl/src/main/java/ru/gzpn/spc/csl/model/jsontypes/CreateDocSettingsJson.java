package ru.gzpn.spc.csl.model.jsontypes;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ru.gzpn.spc.csl.ui.createdoc.NodeWrapper;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class CreateDocSettingsJson implements Serializable {
	private static final long serialVersionUID = -8491801489639919153L;
	// sequence of grouping fields/entities to show in treeGreed component
	private NodeWrapper leftTreeGroup;
	private NodeWrapper rightTreeGroup;
	// the showing columns of the end entity in hierarchy 
	private List<String> leftResultColumns;
	private List<String> rightresultColumns;
	
	public NodeWrapper getLeftTreeGrid() {
		return leftTreeGroup;
	}
	public void setLeftTreeGrid(NodeWrapper leftTreeGrid) {
		this.leftTreeGroup = leftTreeGrid;
	}
	public NodeWrapper getRightTreeGrid() {
		return rightTreeGroup;
	}
	public void setRightTreeGrid(NodeWrapper rightTreeGrid) {
		this.rightTreeGroup = rightTreeGrid;
	}
	public List<String> getLeftResultColumns() {
		return leftResultColumns;
	}
	public void setLeftResultColumns(List<String> leftResultColumns) {
		this.leftResultColumns = leftResultColumns;
	}
	public List<String> getRightresultColumns() {
		return rightresultColumns;
	}
	public void setRightresultColumns(List<String> rightresultColumns) {
		this.rightresultColumns = rightresultColumns;
	}
}
