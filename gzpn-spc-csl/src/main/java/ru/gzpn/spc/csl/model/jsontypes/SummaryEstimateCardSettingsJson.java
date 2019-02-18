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
import ru.gzpn.spc.csl.model.interfaces.ILocalEstimate;
import ru.gzpn.spc.csl.model.interfaces.IObjectEstimate;
import ru.gzpn.spc.csl.model.interfaces.IWork;
import ru.gzpn.spc.csl.model.utils.NodeWrapper;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class SummaryEstimateCardSettingsJson implements ISettingsJson, Serializable {

	private int splitPosition;

	private NodeWrapper localEstimatesTreeGroup;
	private NodeWrapper objectEstimatesTreeGroup;
	private NodeWrapper estimateCalculationsTreeGroup;
	private NodeWrapper worksTreeGroup;
	
	private List<ColumnSettings> localEstimatesColumns;
	private List<ColumnSettings> objectEstimatesColumns;
	private List<ColumnSettings> estimateCalculationsColumns;
	private List<ColumnSettings> worksColumns;

	private List<ColumnHeaderGroup> localEstimatesColumnHeaders;
	private List<ColumnHeaderGroup> objectEstimatesColumnHeaders;
	private List<ColumnHeaderGroup> estimateCalculationsColumnHeaders;
	private List<ColumnHeaderGroup> worksColumnHeaders;
	
	private boolean showTree;

	public int getSplitPosition() {
		return splitPosition;
	}

	public void setSplitPosition(int splitPosition) {
		this.splitPosition = splitPosition;
	}

	public void setShowTree(boolean showTree) {
		this.showTree = showTree;
	}

	@JsonIgnore
	public List<ColumnHeaderGroup> getDefaultLocalEstimatesHeaders() {

		List<ColumnHeaderGroup> result = new ArrayList<>();
		List<ColumnHeaderGroup> subGroups = new ArrayList<>();
//			ColumnHeaderGroup root0 = new ColumnHeaderGroup("Root group 0");
//			ColumnHeaderGroup root1 = new ColumnHeaderGroup("Root group 1");
//			ColumnHeaderGroup root2 = new ColumnHeaderGroup("Root group 2");
//			
//			subGroups.add(new ColumnHeaderGroup("Group 1").addColumn(leftResultColumns.get(1)).addColumn(leftResultColumns.get(2)));
//			subGroups.add(new ColumnHeaderGroup("Group 2").addColumn(leftResultColumns.get(3)).addColumn(leftResultColumns.get(4)));
//			
//			root2.setChildren(subGroups);
//			root1.setChildren(Arrays.asList(root2));
//			root0.setChildren(Arrays.asList(root1));
//			result.add(root0);

		return result;
	}

	@JsonIgnore
	public List<ColumnHeaderGroup> getDefaultObjectEstimatesHeaders() {

		List<ColumnHeaderGroup> result = new ArrayList<>();
		List<ColumnHeaderGroup> subGroups = new ArrayList<>();

		return result;
	}

	@JsonIgnore
	public List<ColumnHeaderGroup> getDefaultEstimateCalculationsHeaders() {

		List<ColumnHeaderGroup> result = new ArrayList<>();
		List<ColumnHeaderGroup> subGroups = new ArrayList<>();

		return result;
	}
	
	@JsonIgnore
	public List<ColumnHeaderGroup> getDefaultWorksHeaders() {

		List<ColumnHeaderGroup> result = new ArrayList<>();
		List<ColumnHeaderGroup> subGroups = new ArrayList<>();

		return result;
	}
	
	
	@JsonIgnore
	public NodeWrapper getDefaultLocalEstimatesTreeGroup() {
		NodeWrapper root = new NodeWrapper("HProject", "name");
		root.addChild(new NodeWrapper("CProject", "name")).addChild(new NodeWrapper("Stage", "name"))
				.addChild(new NodeWrapper("PlanObject", "name"));
		return root;
	}

	@JsonIgnore
	public NodeWrapper getDefaultObjectEstimatesTreeGroup() {
		NodeWrapper root = new NodeWrapper("HProject", "name");
		root.addChild(new NodeWrapper("CProject", "name")).addChild(new NodeWrapper("Stage", "name"))
				.addChild(new NodeWrapper("PlanObject", "name"));
		return root;
	}
	
	@JsonIgnore
	public NodeWrapper getDefaultEstimateCalculationsTreeGroup() {
		NodeWrapper root = new NodeWrapper("HProject", "name");
		root.addChild(new NodeWrapper("CProject", "name")).addChild(new NodeWrapper("Stage", "name"))
				.addChild(new NodeWrapper("PlanObject", "name"));
		return root;
	}
	
	@JsonIgnore
	public NodeWrapper getDefaultWorksTreeGroup() {
		NodeWrapper root = new NodeWrapper("HProject", "name");
		root.addChild(new NodeWrapper("CProject", "name")).addChild(new NodeWrapper("Stage", "name"))
				.addChild(new NodeWrapper("PlanObject", "name"));
		return root;
	}
	
	

	@JsonIgnore
	public List<ColumnSettings> getDefaultLocalEstimatesColumns() {
		List<ColumnSettings> result = new ArrayList<>();
		String entityName = Document.class.getSimpleName();

		result.add(new ColumnSettings(entityName, ILocalEstimate.FIELD_ID, null, false, 0));
		result.add(new ColumnSettings(entityName, ILocalEstimate.FIELD_NAME, null, true, 1));
		result.add(new ColumnSettings(entityName, ILocalEstimate.FIELD_CODE, null, true, 2));
		result.add(new ColumnSettings(entityName, ILocalEstimate.FIELD_CHANGEDBY, null, true, 3));
		result.add(new ColumnSettings(entityName, ILocalEstimate.FIELD_ESTIMATEHEAD, null, true, 4));

		result.add(new ColumnSettings(entityName, ILocalEstimate.FIELD_CREATE_DATE, null, true, 6));
		result.add(new ColumnSettings(entityName, ILocalEstimate.FIELD_CHANGE_DATE, null, false, 7));
		result.add(new ColumnSettings(entityName, ILocalEstimate.FIELD_VERSION, null, false, 8));

		return result;
	}
	
	@JsonIgnore
	public List<ColumnSettings> getDefaultObjectEstimatesColumns() {
		List<ColumnSettings> result = new ArrayList<>();
		String entityName = Document.class.getSimpleName();

		result.add(new ColumnSettings(entityName, IObjectEstimate.FIELD_ID, null, false, 0));
		result.add(new ColumnSettings(entityName, IObjectEstimate.FIELD_NAME, null, true, 1));
		result.add(new ColumnSettings(entityName, IObjectEstimate.FIELD_CODE, null, true, 2));
		result.add(new ColumnSettings(entityName, IObjectEstimate.FIELD_STAGE, null, true, 3));
		result.add(new ColumnSettings(entityName, IObjectEstimate.FIELD_ESTIMATEHEAD, null, true, 4));
		result.add(new ColumnSettings(entityName, IObjectEstimate.FIELD_TOTAL, null, true, 5));

		result.add(new ColumnSettings(entityName, IObjectEstimate.FIELD_CREATE_DATE, null, true, 6));
		result.add(new ColumnSettings(entityName, IObjectEstimate.FIELD_CHANGE_DATE, null, false, 7));
		result.add(new ColumnSettings(entityName, IObjectEstimate.FIELD_VERSION, null, false, 8));

		return result;
	}
	
	@JsonIgnore
	public List<ColumnSettings> getDefaultEstimateCalculationsColumns() {
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
	public List<ColumnSettings> getDefaultWorksColumns() {
		List<ColumnSettings> result = new ArrayList<>();
		String entityName = Document.class.getSimpleName();

		result.add(new ColumnSettings(entityName, IWork.FIELD_ID, null, false, 0));
		result.add(new ColumnSettings(entityName, IWork.FIELD_NAME, null, true, 1));
		result.add(new ColumnSettings(entityName, IWork.FIELD_CODE, null, true, 2));
		result.add(new ColumnSettings(entityName, IWork.FIELD_TYPE, null, true, 3));
		result.add(new ColumnSettings(entityName, IWork.FIELD_MILESTONE, null, true, 4));

		result.add(new ColumnSettings(entityName, IWork.FIELD_CREATE_DATE, null, true, 6));
		result.add(new ColumnSettings(entityName, IWork.FIELD_CHANGE_DATE, null, false, 7));
		result.add(new ColumnSettings(entityName, IWork.FIELD_VERSION, null, false, 8));

		return result;
	}
	
	
	
	
	
	public List<ColumnHeaderGroup> getLocalEstimatesColumnHeaders() {
		if (Objects.isNull(localEstimatesColumnHeaders)) {
			localEstimatesColumnHeaders = getDefaultLocalEstimatesHeaders();
		}

		return localEstimatesColumnHeaders;
	}
	public void setLocalEstimatesColumnHeaders(List<ColumnHeaderGroup> headers) {
		localEstimatesColumnHeaders = headers;
	}
	
	public List<ColumnHeaderGroup> getObjectEstimatesColumnHeaders() {
		if (Objects.isNull(objectEstimatesColumnHeaders)) {
			objectEstimatesColumnHeaders = getDefaultObjectEstimatesHeaders();
		}

		return objectEstimatesColumnHeaders;
	}
	public void setObjectEstimatesColumnHeaders(List<ColumnHeaderGroup> headers) {
		objectEstimatesColumnHeaders = headers;
	}
	
	public List<ColumnHeaderGroup> getEstimateCalculationsColumnHeaders() {
		if (Objects.isNull(estimateCalculationsColumnHeaders)) {
			estimateCalculationsColumnHeaders = getDefaultEstimateCalculationsHeaders();
		}

		return estimateCalculationsColumnHeaders;
	}
	public void setEstimateCalculationsColumnHeaders(List<ColumnHeaderGroup> headers) {
		estimateCalculationsColumnHeaders = headers;
	}

	public List<ColumnHeaderGroup> getWorksColumnHeaders() {
		if (Objects.isNull(worksColumnHeaders)) {
			worksColumnHeaders = getDefaultWorksHeaders();
		}

		return worksColumnHeaders;
	}
	public void setWorksColumnHeaders(List<ColumnHeaderGroup> headers) {
		worksColumnHeaders = headers;
	}
	
	
	
	
	public NodeWrapper getLocalEstimatesTreeGroup() {
		if (Objects.isNull(localEstimatesTreeGroup)) {
			localEstimatesTreeGroup = getDefaultLocalEstimatesTreeGroup();
		}
		return localEstimatesTreeGroup;
	}
	public void setLocalEstimatesTreeGroup(NodeWrapper node) {
		localEstimatesTreeGroup = node;
	}
	
	public NodeWrapper getObjectEstimatesTreeGroup() {
		if (Objects.isNull(objectEstimatesTreeGroup)) {
			objectEstimatesTreeGroup = getDefaultObjectEstimatesTreeGroup();
		}
		return objectEstimatesTreeGroup;
	}
	public void setObjectEstimatesTreeGroup(NodeWrapper node) {
		objectEstimatesTreeGroup = node;
	}
	
	public NodeWrapper getEstimateCalculationsTreeGroup() {
		if (Objects.isNull(estimateCalculationsTreeGroup)) {
			estimateCalculationsTreeGroup = getDefaultEstimateCalculationsTreeGroup();
		}
		return estimateCalculationsTreeGroup;
	}
	public void getEstimateCalculationsTreeGroup(NodeWrapper node) {
		estimateCalculationsTreeGroup = node;
	}
	
	public NodeWrapper getWorksTreeGroup() {
		if (Objects.isNull(worksTreeGroup)) {
			worksTreeGroup = getDefaultWorksTreeGroup();
		}
		return worksTreeGroup;
	}
	public void setWorksTreeGroup(NodeWrapper node) {
		worksTreeGroup = node;
	}
	
	
	
	
	public List<ColumnSettings> getLocalEstimatesColumns() {
		if (Objects.isNull(localEstimatesColumns)) {
			estimateCalculationsColumns = getDefaultLocalEstimatesColumns();
		}

		return estimateCalculationsColumns;
	}
	public void setLocalEstimatesColumns(List<ColumnSettings> columns) {
		estimateCalculationsColumns = columns;
	}
	
	public List<ColumnSettings> getObjectEstimateColumns() {
		if (Objects.isNull(objectEstimatesColumns)) {
			objectEstimatesColumns = getDefaultObjectEstimatesColumns();
		}

		return objectEstimatesColumns;
	}
	public void setObjectEstimateColumns(List<ColumnSettings> columns) {
		objectEstimatesColumns = columns;
	}
	
	public List<ColumnSettings> getEstimateCalculationsColumns() {
		if (Objects.isNull(estimateCalculationsColumns)) {
			estimateCalculationsColumns = getDefaultEstimateCalculationsColumns();
		}

		return estimateCalculationsColumns;
	}
	public void setEstimateCalculationsColumns(List<ColumnSettings> columns) {
		estimateCalculationsColumns = columns;
	}
	
	public List<ColumnSettings> getWorksColumns() {
		if (Objects.isNull(worksColumns)) {
			worksColumns = getDefaultWorksColumns();
		}

		return worksColumns;
	}
	public void setWorksColumns(List<ColumnSettings> columns) {
		worksColumns = columns;
	}
	

	@Override
	@JsonIgnore
	public boolean isShownTree() {
		return showTree;
	}

	@Override
	@JsonIgnore
	public NodeWrapper getTreeSettings() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@JsonIgnore
	public List<ColumnSettings> getColumns() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@JsonIgnore
	public List<ColumnHeaderGroup> getHeaders() {
		// TODO Auto-generated method stub
		return null;
	}

}
