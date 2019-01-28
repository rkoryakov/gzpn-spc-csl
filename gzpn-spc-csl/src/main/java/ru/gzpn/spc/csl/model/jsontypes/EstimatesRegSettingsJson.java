package ru.gzpn.spc.csl.model.jsontypes;

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

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class EstimatesRegSettingsJson implements ISettingsJson {
	// sequence of grouping fields/entities to show in a treeGreed component
	private NodeWrapper leftTreeGroup;

	// the displayed columns
	private List<ColumnSettings> rightResultColumns;
	
	// null if there are no header groups
	private List<ColumnHeaderGroup> rightColumnHeaders;
	
	public boolean hasRightColumnHeaders() {
		return rightColumnHeaders != null && !rightColumnHeaders.isEmpty();
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
					.addChild(new NodeWrapper("PlanObject", "name"));
		return root;
	}
	
	public List<ColumnSettings> getRightResultColumns() {
		if (Objects.isNull(rightResultColumns)) {
			rightResultColumns = getRightDefaultColumns();
		}
		
		return rightResultColumns;
	}

	public void setRightResultColumns(List<ColumnSettings> rightResultColumns) {
		this.rightResultColumns = rightResultColumns;
	}
	
	@JsonIgnore
	public List<ColumnSettings> getRightDefaultColumns() {
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
}
