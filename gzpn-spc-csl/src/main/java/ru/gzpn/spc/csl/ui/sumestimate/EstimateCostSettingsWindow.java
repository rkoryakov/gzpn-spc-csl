package ru.gzpn.spc.csl.ui.sumestimate;

import java.util.List;

import org.springframework.context.MessageSource;

import ru.gzpn.spc.csl.model.jsontypes.ColumnHeaderGroup;
import ru.gzpn.spc.csl.model.jsontypes.ColumnSettings;
import ru.gzpn.spc.csl.model.jsontypes.ISettingsJson;
import ru.gzpn.spc.csl.model.jsontypes.SummaryEstimateCardSettingsJson;
import ru.gzpn.spc.csl.model.utils.NodeWrapper;
import ru.gzpn.spc.csl.services.bl.interfaces.IUserSettingsService;
import ru.gzpn.spc.csl.ui.common.AbstractTreeGridSettingsWindow;

@SuppressWarnings("serial")
public class EstimateCostSettingsWindow extends AbstractTreeGridSettingsWindow {

	private SummaryEstimateCardSettingsJson changedSummaryEstimateCardSettingsJson = null;
	
	public EstimateCostSettingsWindow(IUserSettingsService settingsService, MessageSource messageSource) {
		super(settingsService, messageSource);
	}

	@Override
	public NodeWrapper getTreeSettings() {
		return ((SummaryEstimateCardSettingsJson)getUserSettings()).getEstimateCostTreeGroup();
	}

	@Override
	public void setTreeSettings(NodeWrapper node) {
		 ((SummaryEstimateCardSettingsJson)getUserSettings()).setEstimateCostTreeGroup(node);
	}

	@Override
	public List<ColumnSettings> getColumnSettings() {
		return  ((SummaryEstimateCardSettingsJson)getUserSettings()).getEstimateCostColumns();
	}

	@Override
	public void setColumnSettings(List<ColumnSettings> columns) {
		 ((SummaryEstimateCardSettingsJson)getUserSettings()).setEstimateCostColumns(columns);
	}

	@Override
	public List<ColumnHeaderGroup> getHeaderSettings() {
		return  ((SummaryEstimateCardSettingsJson)getUserSettings()).getEstimateCostColumnHeaders();
	}

	@Override
	public void setHeaderSettings(List<ColumnHeaderGroup> headers) {
		 ((SummaryEstimateCardSettingsJson)getUserSettings()).setEstimateCostColumnHeaders(headers);
	}

	@Override
	public ISettingsJson getUserSettings() {
		if (changedSummaryEstimateCardSettingsJson == null) {
			changedSummaryEstimateCardSettingsJson = (SummaryEstimateCardSettingsJson)
					settingsService.getSummaryEstimateCardSettings(currentUser, new SummaryEstimateCardSettingsJson());
		}
		return changedSummaryEstimateCardSettingsJson;
	}
}
