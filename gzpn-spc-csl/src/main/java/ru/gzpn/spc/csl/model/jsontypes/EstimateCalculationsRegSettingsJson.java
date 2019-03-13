package ru.gzpn.spc.csl.model.jsontypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ru.gzpn.spc.csl.model.Document;
import ru.gzpn.spc.csl.model.interfaces.IEstimateCalculation;
import ru.gzpn.spc.csl.model.utils.NodeWrapper;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class EstimateCalculationsRegSettingsJson implements ISettingsJson, Serializable {
	// sequence of grouping fields/entities to show in a treeGreed component
	private NodeWrapper leftTreeGroup;

	// the displayed columns
	private List<ColumnSettings> rightResultColumns;
	
	// null if there are no header groups
	private List<ColumnHeaderGroup> rightColumnHeaders;

	private boolean showTree = false;
	
	public boolean hasHeaders() {
		return rightColumnHeaders != null && !rightColumnHeaders.isEmpty();
	}
	
	@JsonIgnore
	public List<ColumnHeaderGroup> getDefaultHeaders() {
		
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
	
	public void setHeaders(List<ColumnHeaderGroup> rightColumnHeaders) {
		this.rightColumnHeaders = rightColumnHeaders;
	}

	public void setTreeSettings(NodeWrapper leftTreeGroup) {
		this.leftTreeGroup = leftTreeGroup;
	}
	
	@JsonIgnore
	public NodeWrapper getDefaultNodesHierarchy() {
		NodeWrapper root =  new NodeWrapper("HProject", "name");
			root.addChild(new NodeWrapper("CProject", "name"))
				.addChild(new NodeWrapper("Stage", "name"))
					.addChild(new NodeWrapper("PlanObject", "name"));
		return root;
	}
	
	public List<ColumnSettings> getRightResultColumns() {
		if (Objects.isNull(rightResultColumns)) {
			rightResultColumns = getDefaultColumns();
		}
		
		return rightResultColumns;
	}

	public void setRightResultColumns(List<ColumnSettings> rightResultColumns) {
		this.rightResultColumns = rightResultColumns;
	}
	
	@JsonIgnore
	public List<ColumnSettings> getDefaultColumns() {
		List<ColumnSettings> result = new ArrayList<>();
		String entityName = Document.class.getSimpleName();
		
		result.add(new ColumnSettings(entityName, IEstimateCalculation.FIELD_ID, null, false, 0));
		result.add(new ColumnSettings(entityName, IEstimateCalculation.FIELD_NAME, null, true, 1));
		result.add(new ColumnSettings(entityName, IEstimateCalculation.FIELD_CODE, null, true, 2));
		result.add(new ColumnSettings(entityName, IEstimateCalculation.FILED_HANDLER, null, true, 3));
		result.add(new ColumnSettings(entityName, IEstimateCalculation.FILED_CPROJECT, null, true, 4));
		
		result.add(new ColumnSettings(entityName, IEstimateCalculation.FIELD_CREATE_DATE, null, true, 6));
		result.add(new ColumnSettings(entityName, IEstimateCalculation.FIELD_CHANGE_DATE, null, false, 7));
		result.add(new ColumnSettings(entityName, IEstimateCalculation.FIELD_VERSION, null, false, 8));
		
		return result;
	}

	@JsonIgnore
	@Override
	public boolean isShownTree() {
		return showTree ;
	}
	@JsonIgnore
	public void setShowTree(boolean showTree) {
		this.showTree = showTree;
	}

	@JsonIgnore
	@Override
	public NodeWrapper getTreeSettings() {
		if (leftTreeGroup == null) {
			leftTreeGroup = getDefaultNodesHierarchy();
		}
		return leftTreeGroup;
	}

	@JsonIgnore
	@Override
	public List<ColumnSettings> getColumns() {
		if (Objects.isNull(rightResultColumns)) {
			rightResultColumns = getDefaultColumns();
		}
		
		return rightResultColumns;
	}

	@JsonIgnore
	@Override
	public List<ColumnHeaderGroup> getHeaders() {
		if (Objects.isNull(rightColumnHeaders)) {
			rightColumnHeaders = getDefaultHeaders();
		}
		return rightColumnHeaders;
	}
}
