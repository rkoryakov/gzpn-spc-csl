package ru.gzpn.spc.csl.model.jsontypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ru.gzpn.spc.csl.model.EstimateCalculation;
import ru.gzpn.spc.csl.model.EstimateCost;
import ru.gzpn.spc.csl.model.LocalEstimate;
import ru.gzpn.spc.csl.model.ObjectEstimate;
import ru.gzpn.spc.csl.model.interfaces.IEstimateCalculation;
import ru.gzpn.spc.csl.model.interfaces.IEstimateCost;
import ru.gzpn.spc.csl.model.interfaces.ILocalEstimate;
import ru.gzpn.spc.csl.model.interfaces.IObjectEstimate;
import ru.gzpn.spc.csl.model.utils.NodeWrapper;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class SummaryEstimateCardSettingsJson implements ISettingsJson, Serializable {

	private int splitPosition;

	private NodeWrapper localEstimatesTreeGroup;
	private NodeWrapper objectEstimatesTreeGroup;
	private NodeWrapper estimateCalculationsTreeGroup;
	private NodeWrapper estimateCostTreeGroup;
	
	private List<ColumnSettings> localEstimatesColumns;
	private List<ColumnSettings> objectEstimatesColumns;
	private List<ColumnSettings> estimateCalculationsColumns;
	private List<ColumnSettings> estimateCostColumns;
	
	private List<ColumnHeaderGroup> localEstimatesColumnHeaders;
	private List<ColumnHeaderGroup> objectEstimatesColumnHeaders;
	private List<ColumnHeaderGroup> estimateCalculationsColumnHeaders;
	private List<ColumnHeaderGroup> estimateCostColumnHeaders;
	
	private boolean showTree;

	private boolean showLocalEstimatesTree;
	private boolean showObjectEstimatesTree;
	private boolean showEstimateCalculationsTree;
	private boolean showEstimateCostTreeGroup;

	
	public boolean isShowLocalEstimatesTree() {
		return showLocalEstimatesTree;
	}

	public void setShowLocalEstimatesTree(boolean showLocalEstimatesTree) {
		this.showLocalEstimatesTree = showLocalEstimatesTree;
	}

	public boolean isShowObjectEstimatesTree() {
		return showObjectEstimatesTree;
	}

	public void setShowObjectEstimatesTree(boolean showObjectEstimatesTree) {
		this.showObjectEstimatesTree = showObjectEstimatesTree;
	}

	public boolean isShowEstimateCalculationsTree() {
		return showEstimateCalculationsTree;
	}

	public void setShowEstimateCalculationsTree(boolean showEstimateCalculationsTree) {
		this.showEstimateCalculationsTree = showEstimateCalculationsTree;
	}

	public boolean isShowEstimateCostTreeGroup() {
		return showEstimateCostTreeGroup;
	}

	public void setShowEstimateCostTreeGroup(boolean showEstimateCostTreeGroup) {
		this.showEstimateCostTreeGroup = showEstimateCostTreeGroup;
	}

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
	public List<ColumnHeaderGroup> getDefaultEstimateCostHeaders() {

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
	public NodeWrapper getDefaultEstimateCostTreeGroup() {
		NodeWrapper root = new NodeWrapper("HProject", "name");
		root.addChild(new NodeWrapper("CProject", "name")).addChild(new NodeWrapper("Stage", "name"))
				.addChild(new NodeWrapper("PlanObject", "name"));
		return root;
	}
	
	

	@JsonIgnore
	public List<ColumnSettings> getDefaultLocalEstimatesColumns() {
		List<ColumnSettings> result = new ArrayList<>();
		String entityName = LocalEstimate.class.getSimpleName();

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
		String entityName = ObjectEstimate.class.getSimpleName();

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
		String entityName = EstimateCalculation.class.getSimpleName();

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
	public List<ColumnSettings> getDefaultEstimateCostColumns() {
		List<ColumnSettings> result = new ArrayList<>();
		String entityName = EstimateCost.class.getSimpleName();

		result.add(new ColumnSettings(entityName, IEstimateCost.FIELD_ID, null, false, 0));
		result.add(new ColumnSettings(entityName, IEstimateCost.FIELD_MAT_CUSTOMER_SUPPLY, null, true, 1));
		result.add(new ColumnSettings(entityName, IEstimateCost.FIELD_MAT_CONTRACTOR_SUPPLY, null, true, 2));
		result.add(new ColumnSettings(entityName, IEstimateCost.FIELD_MAT_PERCENT_MANUAL_SUPPLY, null, true, 3));
		
		result.add(new ColumnSettings(entityName, IEstimateCost.FIELD_OZP, null, true, 4));
		result.add(new ColumnSettings(entityName, IEstimateCost.FIELD_EMM, null, true, 5));
		result.add(new ColumnSettings(entityName, IEstimateCost.FIELD_ZPM, null, true, 6));
		
		result.add(new ColumnSettings(entityName, IEstimateCost.FIELD_NR, null, true, 7));
		result.add(new ColumnSettings(entityName, IEstimateCost.FIELD_SP, null, true, 8));
		result.add(new ColumnSettings(entityName, IEstimateCost.FIELD_SMR_TOTAL, null, true, 9));
		
		result.add(new ColumnSettings(entityName, IEstimateCost.FIELD_DEVICES_TOTAL, null, true, 10));
		result.add(new ColumnSettings(entityName, IEstimateCost.FIELD_DEVICES_PERCENT_MANUAL, null, true, 11));
		result.add(new ColumnSettings(entityName, IEstimateCost.FIELD_DEVICES_CUSTOMER_SUPPLY, null, true, 12));
		result.add(new ColumnSettings(entityName, IEstimateCost.FIELD_DEVICES_CONTRACTOR_SUPPLY, null, true, 13));

		result.add(new ColumnSettings(entityName, IEstimateCost.FIELD_OTHER, null, true, 14));
		result.add(new ColumnSettings(entityName, IEstimateCost.FIELD_TOTAL, null, true, 15));
		result.add(new ColumnSettings(entityName, IEstimateCost.FIELD_SERVICES, null, true, 16));
		
		result.add(new ColumnSettings(entityName, IEstimateCost.FIELD_CREATE_DATE, null, true, 17));
		result.add(new ColumnSettings(entityName, IEstimateCost.FIELD_CHANGE_DATE, null, false, 18));
		result.add(new ColumnSettings(entityName, IEstimateCost.FIELD_VERSION, null, false, 19));

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
	
	public List<ColumnHeaderGroup> getEstimateCostColumnHeaders() {
		if (Objects.isNull(estimateCostColumnHeaders)) {
			estimateCostColumnHeaders = getDefaultEstimateCostHeaders();
		}

		return estimateCostColumnHeaders;
	}
	public void setEstimateCostColumnHeaders(List<ColumnHeaderGroup> headers) {
		estimateCostColumnHeaders = headers;
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
	
	public NodeWrapper getEstimateCostTreeGroup() {
		if (Objects.isNull(estimateCostTreeGroup)) {
			estimateCostTreeGroup = getDefaultEstimateCostTreeGroup();
		}
		return estimateCostTreeGroup;
	}
	public void setEstimateCostTreeGroup(NodeWrapper node) {
		estimateCostTreeGroup = node;
	}
	
	
	
	public List<ColumnSettings> getLocalEstimatesColumns() {
		if (Objects.isNull(localEstimatesColumns)) {
			localEstimatesColumns = getDefaultLocalEstimatesColumns();
		}

		return localEstimatesColumns;
	}
	public void setLocalEstimatesColumns(List<ColumnSettings> columns) {
		localEstimatesColumns = columns;
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
	
	public List<ColumnSettings> getEstimateCostColumns() {
		if (Objects.isNull(estimateCostColumns)) {
			estimateCostColumns = getDefaultEstimateCostColumns();
		}

		return estimateCostColumns;
	}
	public void setEstimateCostColumns(List<ColumnSettings> columns) {
		estimateCostColumns = columns;
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
