package ru.gzpn.spc.csl.ui.approval;

import java.util.List;

import org.springframework.context.MessageSource;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

import ru.gzpn.spc.csl.model.jsontypes.ColumnHeaderGroup;
import ru.gzpn.spc.csl.model.jsontypes.ColumnSettings;
import ru.gzpn.spc.csl.model.jsontypes.ISettingsJson;
import ru.gzpn.spc.csl.model.jsontypes.LocalEstimatesApprovalJson;
import ru.gzpn.spc.csl.model.utils.NodeWrapper;
import ru.gzpn.spc.csl.services.bl.interfaces.IUserSettingsService;
import ru.gzpn.spc.csl.ui.common.AbstractTreeGridSettingsWindow;

@SuppressWarnings("serial")
public class LocalEstimatesSettingsWindow extends AbstractTreeGridSettingsWindow {
	private LocalEstimatesApprovalJson changedLocalEstimatesApprovalJson = null;
	
	public LocalEstimatesSettingsWindow(IUserSettingsService settingsService, MessageSource messageSource) {
		super(settingsService, messageSource);
	}
	
	@Override
	public ComponentContainer createBodyLayout() {
		VerticalLayout bodyLayout = new VerticalLayout();
		tabSheet = new TabSheet();
		
		VerticalLayout columnsSettings = new VerticalLayout();
		columnsSettings.addComponent(createRightColumnsGrid());
		
		tabSheet.addTab(columnsSettings, getI18nText(I18N_COLUMNSTAB_CAPTION, messageSource));
		tabSheet.addTab(createColumnHeadersGrid(), getI18nText(I18N_HEADERSTAB_CAPTION, messageSource));
		
		bodyLayout.addComponent(tabSheet);
		return bodyLayout;
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
