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
	
	// sequence of grouping fields/entities to show in a treeGreed component
	private NodeWrapper leftTreeGroup;
	private NodeWrapper rightTreeGroup;
	// the displayed columns of the end entity in the hierarchy 
	private List<String> leftResultColumns;
	private List<String> rightResultColumns;
	
	public NodeWrapper getLeftTreeGrid() {
		NodeWrapper result = leftTreeGroup;
		if (result == null) {
			result = getDefaultNodesHierarchyLeft();
		}
		return result;
	}
	
	public void setLeftTreeGrid(NodeWrapper leftTreeGrid) {
		this.leftTreeGroup = leftTreeGrid;
	}
	
	public NodeWrapper getRightTreeGrid() {
		NodeWrapper result = rightTreeGroup;
		if (result == null) {
			result = getDefaultHierarchyRight();
		}
		return result;
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
		return rightResultColumns;
	}
	public void setRightresultColumns(List<String> rightresultColumns) {
		this.rightResultColumns = rightresultColumns;
	}
	
	public NodeWrapper getDefaultNodesHierarchyLeft() {
		NodeWrapper root =  new NodeWrapper("HProject", "name");
		root.addChild(new NodeWrapper("CProject", "name"))
				.addChild(new NodeWrapper("Stage", "name"))
					.addChild(new NodeWrapper("PlanObject", "name"))
						.addChild(new NodeWrapper("WorkSet"));
		return root;
	}
	
	public NodeWrapper getDefaultHierarchyRight() {
		return new NodeWrapper("Document");
	}
}
