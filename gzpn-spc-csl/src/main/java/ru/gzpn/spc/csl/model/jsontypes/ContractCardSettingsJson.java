package ru.gzpn.spc.csl.model.jsontypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ru.gzpn.spc.csl.model.EstimateCost;
import ru.gzpn.spc.csl.model.LocalEstimate;
import ru.gzpn.spc.csl.model.Milestone;
import ru.gzpn.spc.csl.model.interfaces.IEstimateCost;
import ru.gzpn.spc.csl.model.interfaces.ILocalEstimate;
import ru.gzpn.spc.csl.model.interfaces.IMilestone;
import ru.gzpn.spc.csl.model.utils.NodeWrapper;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class ContractCardSettingsJson implements ISettingsJson, Serializable {
	
	public NodeWrapper milestonesTreeGroup;
	public List<ColumnSettings> milestonesColumns;
	public List<ColumnHeaderGroup> milestonesColumnHeaders;
	
	public NodeWrapper localEstimatesTreeGroup;
	public List<ColumnSettings> localEstimatesColumns;
	public List<ColumnHeaderGroup> localEstimatesColumnHeaders;
	
	public NodeWrapper coefficientsTreeGroup;
	public List<ColumnSettings> coefficientsColumns;
	public List<ColumnHeaderGroup> coefficientsColumnHeaders;
	
	public boolean showTree;
	@JsonIgnore
	public List<ColumnSettings> getMilestonrColumns() {
		if (milestonesColumns == null) {
			milestonesColumns = new ArrayList<>();
			String milestones = Milestone.class.getSimpleName();
			milestonesColumns.add(new ColumnSettings(milestones, IMilestone.FIELD_PPNUM, null, true, 0));
			milestonesColumns.add(new ColumnSettings(milestones, IMilestone.FIELD_MILNUM, null, true, 1));
			milestonesColumns.add(new ColumnSettings(milestones, IMilestone.FIELD_NAME, null, true, 2));
			milestonesColumns.add(new ColumnSettings(milestones, IMilestone.FIELD_STARTDATE, null, true, 3));
			milestonesColumns.add(new ColumnSettings(milestones, IMilestone.FIELD_ENDDATE, null, true, 4));
			milestonesColumns.add(new ColumnSettings(milestones, IMilestone.FIELD_SUM, null, true, 5));
			milestonesColumns.add(new ColumnSettings(milestones, IMilestone.FIELD_TAXTYPE, null, true, 6));
			milestonesColumns.add(new ColumnSettings(milestones, IMilestone.FIELD_PROJECT, null, true, 7));
		}
		return milestonesColumns;
	}
	
	@JsonIgnore
	public List<ColumnSettings> getLocalEstimatesColumns() {
		if (localEstimatesColumns == null) {
			localEstimatesColumns = new ArrayList<>();
			String estimates = LocalEstimate.class.getSimpleName();
			String milestones = Milestone.class.getSimpleName();
			String costs = EstimateCost.class.getSimpleName();
			
			localEstimatesColumns.add(new ColumnSettings(milestones, IMilestone.FIELD_NAME, null, true, 0));
			localEstimatesColumns.add(new ColumnSettings(estimates, IEstimateCost.FIELD_MAT_PERCENT_MANUAL_SUPPLY, null, true, 1));
			localEstimatesColumns.add(new ColumnSettings(estimates, ILocalEstimate.FIELD_CODE, null, true, 2));
			localEstimatesColumns.add(new ColumnSettings(estimates, ILocalEstimate.FIELD_NAME, null, true, 3));
			localEstimatesColumns.add(new ColumnSettings(milestones, IMilestone.FIELD_SUM, null, true, 4));
			
			localEstimatesColumns.add(new ColumnSettings(costs, IEstimateCost.FIELD_TOTAL, null, true, 5));
			localEstimatesColumns.add(new ColumnSettings(costs, IEstimateCost.FIELD_MAT_CUSTOMER_SUPPLY, null, true, 6));
			localEstimatesColumns.add(new ColumnSettings(costs, IEstimateCost.FIELD_MAT_CONTRACTOR_SUPPLY, null, true, 7));
			localEstimatesColumns.add(new ColumnSettings(costs, IEstimateCost.FIELD_OZP, null, true, 8));
			localEstimatesColumns.add(new ColumnSettings(costs, IEstimateCost.FIELD_EMM, null, true, 9));
			localEstimatesColumns.add(new ColumnSettings(costs, IEstimateCost.FIELD_ZPM, null, true, 10));
			localEstimatesColumns.add(new ColumnSettings(costs, IEstimateCost.FIELD_NR, null, true, 11));
			localEstimatesColumns.add(new ColumnSettings(costs, IEstimateCost.FIELD_SP, null, true, 12));
			localEstimatesColumns.add(new ColumnSettings(costs, IEstimateCost.FIELD_DEVICES_TOTAL, null, true, 13));
			localEstimatesColumns.add(new ColumnSettings(costs, IEstimateCost.FIELD_DEVICES_CUSTOMER_SUPPLY, null, true, 14));
			localEstimatesColumns.add(new ColumnSettings(costs, IEstimateCost.FIELD_DEVICES_CONTRACTOR_SUPPLY, null, true, 15));
			localEstimatesColumns.add(new ColumnSettings(costs, IEstimateCost.FIELD_OTHER, null, true, 16));
			localEstimatesColumns.add(new ColumnSettings(costs, IEstimateCost.FIELD_PRICELEVEL, null, true, 17));
			
			localEstimatesColumns.add(new ColumnSettings(estimates, ILocalEstimate.FIELD_DRAWING, null, true, 18));
			localEstimatesColumns.add(new ColumnSettings(estimates, ILocalEstimate.FIELD_STAGE, null, true, 19));
		}
		return localEstimatesColumns;
	}

	@JsonIgnore
	public NodeWrapper getMilestonesTreeGroup() {
		if (milestonesTreeGroup == null) {
			milestonesTreeGroup = new NodeWrapper("HProject", "name");
			milestonesTreeGroup.addChild(new NodeWrapper("CProject", "name"))
				.addChild(new NodeWrapper("Contract", "name"));
		}
		return milestonesTreeGroup;
	}
	
	@JsonIgnore
	public NodeWrapper getLocalEstimatesTreeGroup() {
		if (localEstimatesTreeGroup == null) {
			localEstimatesTreeGroup = new NodeWrapper("HProject", "name");
			localEstimatesTreeGroup.addChild(new NodeWrapper("CProject", "name"))
					.addChild(new NodeWrapper("EstimateCost", "name"));
		}
		return localEstimatesTreeGroup;
	}
	
	@Override
	@JsonIgnore
	public boolean isShownTree() {
		return false;
	}

	@Override
	@JsonIgnore
	public NodeWrapper getTreeSettings() {
		return null;
	}

	@Override
	@JsonIgnore
	public List<ColumnSettings> getColumns() {
		return null;
	}

	@Override
	@JsonIgnore
	public List<ColumnHeaderGroup> getHeaders() {
		return null;
	}
}
