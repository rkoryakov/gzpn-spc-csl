package ru.gzpn.spc.csl.ui.approval;

import ru.gzpn.spc.csl.model.dataproviders.AbstractRegistryDataProvider;
import ru.gzpn.spc.csl.model.dataproviders.ProjectTreeDataProvider;
import ru.gzpn.spc.csl.model.jsontypes.ColumnSettings;
import ru.gzpn.spc.csl.model.jsontypes.ISettingsJson;
import ru.gzpn.spc.csl.model.presenters.interfaces.ILocalEstimatePresenter;
import ru.gzpn.spc.csl.services.bl.interfaces.IDataService;
import ru.gzpn.spc.csl.services.bl.interfaces.ILocalEstimateService;
import ru.gzpn.spc.csl.services.bl.interfaces.IProjectService;
import ru.gzpn.spc.csl.services.bl.interfaces.IUserSettigsService;
import ru.gzpn.spc.csl.ui.common.AbstractTreeGridComponent;
import ru.gzpn.spc.csl.ui.common.AbstractTreeGridSettingsWindow;

@SuppressWarnings("serial")
public class LocalEstimatesApprovalGridComponent extends AbstractTreeGridComponent<ILocalEstimatePresenter> {
	public static final String I18N_LOCALESTIMATESAPPROVAL_XLS_CAP = "LocalEstimatesApprovalGridComponent.xls.cap";
	
	private AbstractRegistryDataProvider<ILocalEstimatePresenter, Void> gridDataProvider;
	
	
	public LocalEstimatesApprovalGridComponent(IProjectService treeDataService,
			IDataService<? super ILocalEstimatePresenter, ILocalEstimatePresenter> gridDataService,
			IUserSettigsService userSettingsService) {
		super(treeDataService, gridDataService, userSettingsService);
	}

	@Override
	public boolean isTreeVisible() {
		return false;
	}

	@Override
	public AbstractRegistryDataProvider<ILocalEstimatePresenter, ?> getGridDataProvider() {
		if (gridDataProvider == null) {
			gridDataProvider = new LocalEstimatesApprovalDataProvider((ILocalEstimateService)gridDataService);
		}
		
		return  gridDataProvider;
	}

	@Override
	public ProjectTreeDataProvider getTreeDataProvider() {
		return null;
	}

	@Override
	public String getExcelName() {
		return getI18nText(I18N_LOCALESTIMATESAPPROVAL_XLS_CAP, messageSource);
	}

	@Override
	public AbstractTreeGridSettingsWindow getSettingsWindow() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ISettingsJson getSettings() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getSplitPosition() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getGridRowsCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void createGridColumn(ColumnSettings column) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveGridItem(ILocalEstimatePresenter item) {
		// TODO Auto-generated method stub
		
	}

}
