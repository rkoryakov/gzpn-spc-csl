package ru.gzpn.spc.csl.ui.sumestimate;

import ru.gzpn.spc.csl.model.dataproviders.AbstractRegistryDataProvider;
import ru.gzpn.spc.csl.model.dataproviders.LocalEstimateDataProvider;
import ru.gzpn.spc.csl.model.dataproviders.ProjectTreeDataProvider;
import ru.gzpn.spc.csl.model.jsontypes.ColumnSettings;
import ru.gzpn.spc.csl.model.jsontypes.ISettingsJson;
import ru.gzpn.spc.csl.model.jsontypes.SummaryEstimateCardSettingsJson;
import ru.gzpn.spc.csl.model.presenters.interfaces.ILocalEstimatePresenter;
import ru.gzpn.spc.csl.services.bl.interfaces.IDataService;
import ru.gzpn.spc.csl.services.bl.interfaces.ILocalEstimateService;
import ru.gzpn.spc.csl.services.bl.interfaces.IProjectService;
import ru.gzpn.spc.csl.services.bl.interfaces.IUserSettigsService;
import ru.gzpn.spc.csl.ui.common.AbstractTreeGridComponent;
import ru.gzpn.spc.csl.ui.common.AbstractTreeGridSettingsWindow;

@SuppressWarnings("serial")
public class LocalEstimatesTreeGridComponent extends AbstractTreeGridComponent<ILocalEstimatePresenter> {

	private boolean treeVisibility = false;
	private AbstractRegistryDataProvider<ILocalEstimatePresenter, Void> gridDataProvider;
	private ProjectTreeDataProvider treeDataProvider;
	
	public LocalEstimatesTreeGridComponent(IProjectService treeDataService,
			IDataService<ILocalEstimatePresenter, ?> gridDataService, IUserSettigsService userSettingsService) {
		super(treeDataService, gridDataService, userSettingsService);
	}

	public void setTreeVisibility(boolean visible) {
		treeVisibility = visible;
	}
	
	@Override
	public boolean isTreeVisible() {
		return treeVisibility;
	}

	@Override
	public AbstractRegistryDataProvider<ILocalEstimatePresenter, ?> getGridDataProvider() {
		if (gridDataProvider == null) {
			gridDataProvider = new LocalEstimateDataProvider((ILocalEstimateService)gridDataService);
		}
		return  gridDataProvider;
	}

	@Override
	public ProjectTreeDataProvider getTreeDataProvider() {
		if (treeDataProvider == null) {
			SummaryEstimateCardSettingsJson settings = (SummaryEstimateCardSettingsJson)
					userSettingsService.getSummaryEstimateCardSettings(user, new SummaryEstimateCardSettingsJson());
			treeDataProvider = new ProjectTreeDataProvider(treeDataService, settings.getDefaultLocalEstimatesTreeGroup());
		}
		return treeDataProvider;
	}

	@Override
	public String getExcelName() {
		return "Local_Estimates.xls";
	}

	@Override
	public AbstractTreeGridSettingsWindow getSettingsWindow() {
		return new LocalEstimatesSettingsWindow(userSettingsService, gridDataService.getMessageSource());
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
