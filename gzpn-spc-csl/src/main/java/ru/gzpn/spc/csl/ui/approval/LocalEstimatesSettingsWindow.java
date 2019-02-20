package ru.gzpn.spc.csl.ui.approval;

import java.util.List;

import org.springframework.context.MessageSource;

import ru.gzpn.spc.csl.model.jsontypes.ColumnHeaderGroup;
import ru.gzpn.spc.csl.model.jsontypes.ColumnSettings;
import ru.gzpn.spc.csl.model.jsontypes.ISettingsJson;
import ru.gzpn.spc.csl.model.jsontypes.SummaryEstimateCardSettingsJson;
import ru.gzpn.spc.csl.model.utils.NodeWrapper;
import ru.gzpn.spc.csl.services.bl.interfaces.IUserSettigsService;
import ru.gzpn.spc.csl.ui.common.AbstractTreeGridSettingsWindow;

@SuppressWarnings("serial")
public class LocalEstimatesSettingsWindow extends AbstractTreeGridSettingsWindow {
	private SummaryEstimateCardSettingsJson changedSummaryEstimateCardSettingsJson = null;
	
	public LocalEstimatesSettingsWindow(IUserSettigsService settingsService, MessageSource messageSource) {
		super(settingsService, messageSource);
	}

	@Override
	public NodeWrapper getTreeSettings() {
		return ((SummaryEstimateCardSettingsJson)getUserSettings()).getLocalEstimatesTreeGroup();
	}

	@Override
	public void setTreeSettings(NodeWrapper node) {
		 ((SummaryEstimateCardSettingsJson)getUserSettings()).setLocalEstimatesTreeGroup(node);
	}

	@Override
	public List<ColumnSettings> getColumnSettings() {
		return  ((SummaryEstimateCardSettingsJson)getUserSettings()).getLocalEstimatesColumns();
	}

	@Override
	public void setColumnSettings(List<ColumnSettings> columns) {
		 ((SummaryEstimateCardSettingsJson)getUserSettings()).setLocalEstimatesColumns(columns);
	}

	@Override
	public List<ColumnHeaderGroup> getHeaderSettings() {
		return  ((SummaryEstimateCardSettingsJson)getUserSettings()).getLocalEstimatesColumnHeaders();
	}

	@Override
	public void setHeaderSettings(List<ColumnHeaderGroup> headers) {
		 ((SummaryEstimateCardSettingsJson)getUserSettings()).setLocalEstimatesColumnHeaders(headers);
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
