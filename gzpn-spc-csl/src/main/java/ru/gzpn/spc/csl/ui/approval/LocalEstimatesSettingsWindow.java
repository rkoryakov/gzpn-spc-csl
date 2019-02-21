package ru.gzpn.spc.csl.ui.approval;

import java.util.List;

import org.springframework.context.MessageSource;

import ru.gzpn.spc.csl.model.jsontypes.ColumnHeaderGroup;
import ru.gzpn.spc.csl.model.jsontypes.ColumnSettings;
import ru.gzpn.spc.csl.model.jsontypes.ISettingsJson;
import ru.gzpn.spc.csl.model.jsontypes.LocalEstimatesApprovalJson;
import ru.gzpn.spc.csl.model.utils.NodeWrapper;
import ru.gzpn.spc.csl.services.bl.interfaces.IUserSettigsService;
import ru.gzpn.spc.csl.ui.common.AbstractTreeGridSettingsWindow;

@SuppressWarnings("serial")
public class LocalEstimatesSettingsWindow extends AbstractTreeGridSettingsWindow {
	private LocalEstimatesApprovalJson changedLocalEstimatesApprovalJson = null;
	
	public LocalEstimatesSettingsWindow(IUserSettigsService settingsService, MessageSource messageSource) {
		super(settingsService, messageSource);
	}

	@Override
	public NodeWrapper getTreeSettings() {
		return null;
	}

	@Override
	public void setTreeSettings(NodeWrapper node) {
	}

	@Override
	public List<ColumnSettings> getColumnSettings() {
		return  ((LocalEstimatesApprovalJson)getUserSettings()).getColumns();
	}

	@Override
	public void setColumnSettings(List<ColumnSettings> columns) {
		 ((LocalEstimatesApprovalJson)getUserSettings()).getColumns().clear();
		 ((LocalEstimatesApprovalJson)getUserSettings()).getColumns().addAll(columns);
	}

	@Override
	public List<ColumnHeaderGroup> getHeaderSettings() {
		return  ((LocalEstimatesApprovalJson)getUserSettings()).getHeaders();
	}

	@Override
	public void setHeaderSettings(List<ColumnHeaderGroup> headers) {
		 ((LocalEstimatesApprovalJson)getUserSettings()).getHeaders().clear();
		 ((LocalEstimatesApprovalJson)getUserSettings()).getHeaders().addAll(headers);
	}

	@Override
	public ISettingsJson getUserSettings() {
		if (changedLocalEstimatesApprovalJson == null) {
			changedLocalEstimatesApprovalJson = (LocalEstimatesApprovalJson)
					settingsService.getLocalEstimatesApprovalSettings(currentUser, new LocalEstimatesApprovalJson());
		}
		return changedLocalEstimatesApprovalJson;
	}

}
