package ru.gzpn.spc.csl.model.jsontypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ru.gzpn.spc.csl.model.WorkSet;
import ru.gzpn.spc.csl.model.interfaces.IDocument;
import ru.gzpn.spc.csl.model.interfaces.IWorkSet;
import ru.gzpn.spc.csl.model.utils.NodeWrapper;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class CreateDocSettingsJson implements Serializable {
	private static final long serialVersionUID = -8491801489639919153L;
	public static final int DEFAULT_LEFT_SPLIT_POSITION = 27;
	public static final int DEFAULT_MAIN_SPLIT_POSITION = 52;
	
	// sequence of grouping fields/entities to show in a treeGreed component
	private NodeWrapper leftTreeGroup;
	private NodeWrapper rightTreeGroup;
	private int mainSplitPosition;
	private int leftSplitPosition;
	
	// the displayed columns
	private List<ColumnSettings> leftResultColumns;
	private List<ColumnSettings> rightResultColumns;
	
	// null if there are no header groups
	private List<ColumnHeaderGroup> leftColumnHeaders;
	private List<ColumnHeaderGroup> rightColumnHeaders;
	
	public boolean hasLeftColumnHeaders() {
		return leftColumnHeaders != null && !leftColumnHeaders.isEmpty();
	}
	
	public List<ColumnHeaderGroup> getLeftColumnHeaders() {
		if (Objects.isNull(leftColumnHeaders)) {
			leftColumnHeaders = getLeftDefaultColumnHeaderGroups();
		}
		return leftColumnHeaders;
	}
	
	@JsonIgnore
	public List<ColumnHeaderGroup> getLeftDefaultColumnHeaderGroups() {
		
		List<ColumnHeaderGroup> result = new ArrayList<>();
		List<ColumnHeaderGroup> subGroups = new ArrayList<>();
//		ColumnHeaderGroup root0 = new ColumnHeaderGroup("Root group 0");
//		ColumnHeaderGroup root1 = new ColumnHeaderGroup("Root group 1");
//		ColumnHeaderGroup root2 = new ColumnHeaderGroup("Root group 2");
//		
//		subGroups.add(new ColumnHeaderGroup("Group 1").addColumn(leftResultColumns.get(1)).addColumn(leftResultColumns.get(2)));
//		subGroups.add(new ColumnHeaderGroup("Group 2").addColumn(leftResultColumns.get(3)).addColumn(leftResultColumns.get(4)));
//		
//		root2.setChildren(subGroups);
//		root1.setChildren(Arrays.asList(root2));
//		root0.setChildren(Arrays.asList(root1));
//		result.add(root0);
		
		return result;
	}
	
	public void setLeftColumnHeaders(List<ColumnHeaderGroup> leftColumnHeaders) {
		this.leftColumnHeaders = leftColumnHeaders;
	}
	 
	public List<ColumnHeaderGroup> getRightColumnHeaders() {
		if (Objects.isNull(rightColumnHeaders)) {
			rightColumnHeaders = getRightDefaultColumnHeaderGroups();
		}
		return rightColumnHeaders;
	}
	
	@JsonIgnore
	public List<ColumnHeaderGroup> getRightDefaultColumnHeaderGroups() {
		
		List<ColumnHeaderGroup> result = new ArrayList<>();
		List<ColumnHeaderGroup> subGroups = new ArrayList<>();
//		ColumnHeaderGroup root0 = new ColumnHeaderGroup("Root group 0");
//		ColumnHeaderGroup root1 = new ColumnHeaderGroup("Root group 1");
//		ColumnHeaderGroup root2 = new ColumnHeaderGroup("Root group 2");
//		
//		subGroups.add(new ColumnHeaderGroup("Group 1").addColumn(leftResultColumns.get(1)).addColumn(leftResultColumns.get(2)));
//		subGroups.add(new ColumnHeaderGroup("Group 2").addColumn(leftResultColumns.get(3)).addColumn(leftResultColumns.get(4)));
//		
//		root2.setChildren(subGroups);
//		root1.setChildren(Arrays.asList(root2));
//		root0.setChildren(Arrays.asList(root1));
//		result.add(root0);
		
		return result;
	}
	
	public void setRightColumnHeaders(List<ColumnHeaderGroup> rightColumnHeaders) {
		this.rightColumnHeaders = rightColumnHeaders;
	}
	
	public NodeWrapper getLeftTreeGroup() {
		if (leftTreeGroup == null) {
			leftTreeGroup = getLeftDefaultNodesHierarchy();
		}
		return leftTreeGroup;
	}

	public void setLeftTreeGroup(NodeWrapper leftTreeGroup) {
		this.leftTreeGroup = leftTreeGroup;
	}
	
	@JsonIgnore
	public NodeWrapper getLeftDefaultNodesHierarchy() {
		NodeWrapper root =  new NodeWrapper("HProject", "name");
			root.addChild(new NodeWrapper("CProject", "name"))
				.addChild(new NodeWrapper("Stage", "name"))
					.addChild(new NodeWrapper("PlanObject", "code"));
		return root;
	}
	
	public List<ColumnSettings> getLeftResultColumns() {
		if (Objects.isNull(leftResultColumns)) {
			leftResultColumns = getLeftDefaultWorksetColumns();
		}
		
		return leftResultColumns;
	}
	
	public void setLeftResultColumns(List<ColumnSettings> lefResultColumns) {
		this.leftResultColumns = lefResultColumns;
	}
	
	@JsonIgnore
	public List<ColumnSettings> getLeftDefaultWorksetColumns() {
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
	
	public NodeWrapper getRightTreeGroup() {
		NodeWrapper result = rightTreeGroup;
		if (result == null) {
			result = getRightDefaultNodesHierarchy();
		}
		return result;
	}

	public void setRightTreeGroup(NodeWrapper rightTreeGroup) {
		this.rightTreeGroup = rightTreeGroup;
	}
	
	@JsonIgnore
	public NodeWrapper getRightDefaultNodesHierarchy() {
		return new NodeWrapper("Document");
	}
	
	public List<ColumnSettings> getRightResultColumns() {
		if (Objects.isNull(rightResultColumns)) {
			rightResultColumns = getRightDefaultDocumentsColumns();
		}
		
		return rightResultColumns;
	}

	public void setRightResultColumns(List<ColumnSettings> rightResultColumns) {
		this.rightResultColumns = rightResultColumns;
	}
	
	@JsonIgnore
	public List<ColumnSettings> getRightDefaultDocumentsColumns() {
		List<ColumnSettings> result = new ArrayList<>();
		String entityName = WorkSet.class.getSimpleName();
		
		result.add(new ColumnSettings(entityName, IDocument.FIELD_ID, null, false, 0));
		result.add(new ColumnSettings(entityName, IDocument.FIELD_NAME, null, true, 1));
		result.add(new ColumnSettings(entityName, IDocument.FIELD_CODE, null, true, 2));
		result.add(new ColumnSettings(entityName, IDocument.FIELD_TYPE, null, true, 3));
		result.add(new ColumnSettings(entityName, IDocument.FIELD_WORK, null, true, 4));
		
		result.add(new ColumnSettings(entityName, IDocument.FIELD_WORKSET, null, false, 5));
		result.add(new ColumnSettings(entityName, IDocument.FIELD_CREATE_DATE, null, false, 6));
		result.add(new ColumnSettings(entityName, IDocument.FIELD_CHANGE_DATE, null, false, 7));
		result.add(new ColumnSettings(entityName, IDocument.FIELD_VERSION, null, false, 8));
		
		return result;
	}

	public Integer getMainSplitPosition() {
		Integer result = DEFAULT_MAIN_SPLIT_POSITION;
		
		if (!Objects.isNull(mainSplitPosition) && mainSplitPosition > 1 && mainSplitPosition <= 100) {
			result = mainSplitPosition;
		}
		
		return result;
	}

	public Integer getLeftSplitPosition() {
		Integer result = DEFAULT_LEFT_SPLIT_POSITION;
		
		if (Objects.nonNull(leftSplitPosition) && leftSplitPosition > 1 && leftSplitPosition <= 100) {
			result = leftSplitPosition;
		}
		
		return result;
	}
	
	public void setLeftSplitPosition(Integer leftSplitPosition) {
		this.leftSplitPosition = leftSplitPosition;
	}

	public void setMainSplitPosition(int mainSplitPosition) {
		this.mainSplitPosition = mainSplitPosition;
	}

	public void setLeftSplitPosition(int leftSplitPosition) {
		this.leftSplitPosition = leftSplitPosition;
	}

}
