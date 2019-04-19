package ru.gzpn.spc.csl.ui.contract;

import java.util.List;

import org.springframework.context.MessageSource;

import ru.gzpn.spc.csl.model.jsontypes.ColumnHeaderGroup;
import ru.gzpn.spc.csl.model.jsontypes.ColumnSettings;
import ru.gzpn.spc.csl.model.jsontypes.ContractCardSettingsJson;
import ru.gzpn.spc.csl.model.jsontypes.ISettingsJson;
import ru.gzpn.spc.csl.model.utils.NodeWrapper;
import ru.gzpn.spc.csl.services.bl.interfaces.IUserSettingsService;
import ru.gzpn.spc.csl.ui.common.AbstractTreeGridSettingsWindow;

@SuppressWarnings("serial")
public class LocalEstimatesSettingsWindow extends AbstractTreeGridSettingsWindow {
	private ContractCardSettingsJson changedContractCardSettingsJson = null;
	
	public LocalEstimatesSettingsWindow(IUserSettingsService settingsService, MessageSource messageSource) {
		super(settingsService, messageSource);
	}

	@Override
	public NodeWrapper getTreeSettings() {
		return ((ContractCardSettingsJson)getUserSettings()).getLocalEstimatesTreeGroup();
	}

	@Override
	public void setTreeSettings(NodeWrapper node) {
		 ((ContractCardSettingsJson)getUserSettings()).localEstimatesTreeGroup = node;
	}

	@Override
	public List<ColumnSettings> getColumnSettings() {
		return  ((ContractCardSettingsJson)getUserSettings()).getLocalEstimatesColumns();
	}

	@Override
	public void setColumnSettings(List<ColumnSettings> columns) {
		 ((ContractCardSettingsJson)getUserSettings()).localEstimatesColumns = columns;
	}

	@Override
	public List<ColumnHeaderGroup> getHeaderSettings() {
		return  ((ContractCardSettingsJson)getUserSettings()).getLocalEstimatesColumnHeaders();
	}

	@Override
	public void setHeaderSettings(List<ColumnHeaderGroup> headers) {
		 ((ContractCardSettingsJson)getUserSettings()).localEstimatesColumnHeaders = headers;
	}

	@Override
	public ISettingsJson getUserSettings() {
		if (changedContractCardSettingsJson == null) {
			changedContractCardSettingsJson = (ContractCardSettingsJson)
					settingsService.getContractCardSettings(currentUser, new ContractCardSettingsJson());
		}
		return changedContractCardSettingsJson;
	}

}
