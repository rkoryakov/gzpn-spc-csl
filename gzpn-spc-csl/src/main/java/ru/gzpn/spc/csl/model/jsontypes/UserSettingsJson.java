package ru.gzpn.spc.csl.model.jsontypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ru.gzpn.spc.csl.model.WorkSet;
import ru.gzpn.spc.csl.model.interfaces.IWorkSet;
import ru.gzpn.spc.csl.model.utils.NodeWrapper;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class UserSettingsJson implements Serializable {
	private static final long serialVersionUID = -8491801489639919153L;
	public static final int DEFAULT_LEFT_SPLIT_POSITION = 27;
	public static final int DEFAULT_MAIN_SPLIT_POSITION = 52;
	
	// sequence of grouping fields/entities to show in a treeGreed component
	private NodeWrapper leftTreeGroup;
	private NodeWrapper rightTreeGroup;
	private int mainSplitPosition;
	private int leftSplitPosition;
	
	// the displayed columns of the end entity in the hierarchy 
	private List<ColumnSettings> lefResultColumns;
	private List<ColumnSettings> rightResultColumns;
	
	public NodeWrapper getLeftHierarchySettings() {
		NodeWrapper result = leftTreeGroup;
		if (result == null) {
			result = getLeftDefaultNodesHierarchy();
		}
		return result;
	}
	
	public NodeWrapper getLeftDefaultNodesHierarchy() {
		NodeWrapper root =  new NodeWrapper("HProject", "name");
			root.addChild(new NodeWrapper("CProject", "name"))
				.addChild(new NodeWrapper("Stage", "name"))
					.addChild(new NodeWrapper("PlanObject", "code"));
		return root;
	}
	
	public void setLeftHierarchySettings(NodeWrapper leftTreeGrid) {
		this.leftTreeGroup = leftTreeGrid;
	}
	
	public List<ColumnSettings> getLeftWorksetColumns() {
		List<ColumnSettings> result = lefResultColumns;
		if (Objects.isNull(result)) {
			result = getLeftDefaultWorksetColumns();
		}
		
		return result;
	}
	
	private List<ColumnSettings> getLeftDefaultWorksetColumns() {
		List<ColumnSettings> result = new ArrayList<>();
		String entityName = WorkSet.class.getSimpleName();
		
		result.add(new ColumnSettings(entityName, IWorkSet.FIELD_ID, null, false, 0));
		result.add(new ColumnSettings(entityName, IWorkSet.FIELD_NAME, null, true, 1));
		result.add(new ColumnSettings(entityName, IWorkSet.FIELD_CODE, null, true, 2));
		result.add(new ColumnSettings(entityName, IWorkSet.FIELD_PIR, null, true, 3));
		result.add(new ColumnSettings(entityName, IWorkSet.FIELD_SMR, null, true, 4));
		
		result.add(new ColumnSettings(entityName, IWorkSet.FIELD_PLAN_OBJECT, null, false, 5));
		result.add(new ColumnSettings(entityName, IWorkSet.FIELD_CREATE_DATE, null, false, 6));
		result.add(new ColumnSettings(entityName, IWorkSet.FIELD_CHANGE_DATE, null, false, 7));
		result.add(new ColumnSettings(entityName, IWorkSet.FIELD_VERSION, null, false, 8));
		
		return result;
	}

	public void setLeftWorksetColumns(List<ColumnSettings> leftResultColumns) {
		this.lefResultColumns = leftResultColumns;
	}
	
	public NodeWrapper getRightHierarchySettings() {
		NodeWrapper result = rightTreeGroup;
		if (result == null) {
			result = getRightDefaultNodesHierarchy();
		}
		return result;
	}
	
	public void setRightHierarchySettings(NodeWrapper rightTreeGrid) {
		this.rightTreeGroup = rightTreeGrid;
	}
	
	public List<ColumnSettings> getRightDocumentsColumns() {
		List<ColumnSettings> result = rightResultColumns;
		
		if (Objects.isNull(result)) {
			result = getRightDefaultDocumentsColumns();
		}
		
		return result;
	}
	
	private List<ColumnSettings> getRightDefaultDocumentsColumns() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setRightDocumentsColumns(List<ColumnSettings> rightresultColumns) {
		this.rightResultColumns = rightresultColumns;
	}
	
	public NodeWrapper getRightDefaultNodesHierarchy() {
		return new NodeWrapper("Document");
	}

	public Integer getMainSplitPosition() {
		Integer result = DEFAULT_MAIN_SPLIT_POSITION;
		
		if (!Objects.isNull(mainSplitPosition) && mainSplitPosition > 1 && mainSplitPosition < 100) {
			result = mainSplitPosition;
		}
		
		return result;
	}
	
	public void setMainSplitPosition(Integer mainSplitPosition) {
		this.mainSplitPosition = mainSplitPosition;
	}

	public Integer getLeftSplitPosition() {
		Integer result = DEFAULT_LEFT_SPLIT_POSITION;
		
		if (Objects.nonNull(leftSplitPosition) && leftSplitPosition > 1) {
			result = leftSplitPosition;
		}
		
		return result;
	}

	public void setLeftSplitPosition(Integer leftSplitPosition) {
		this.leftSplitPosition = leftSplitPosition;
	}

}
