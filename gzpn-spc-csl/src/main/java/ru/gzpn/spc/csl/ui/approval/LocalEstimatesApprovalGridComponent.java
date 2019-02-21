package ru.gzpn.spc.csl.ui.approval;

import java.util.Objects;
import java.util.stream.Collectors;

import com.vaadin.data.ValueProvider;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

import ru.gzpn.spc.csl.model.dataproviders.AbstractRegistryDataProvider;
import ru.gzpn.spc.csl.model.dataproviders.ProjectTreeDataProvider;
import ru.gzpn.spc.csl.model.enums.Entities;
import ru.gzpn.spc.csl.model.interfaces.ILocalEstimate;
import ru.gzpn.spc.csl.model.interfaces.IStage;
import ru.gzpn.spc.csl.model.jsontypes.ColumnSettings;
import ru.gzpn.spc.csl.model.jsontypes.ISettingsJson;
import ru.gzpn.spc.csl.model.jsontypes.LocalEstimatesApprovalJson;
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
	public Component createGridButtons() {
		return new HorizontalLayout();
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
		return new LocalEstimatesSettingsWindow(userSettingsService, messageSource);
	}

	@Override
	public ISettingsJson getSettings() {
		return userSettingsService.getLocalEstimatesApprovalSettings(user, new LocalEstimatesApprovalJson());
	}

	@Override
	public float getSplitPosition() {
		return 0;
	}

	@Override
	public int getGridRowsCount() {
		return 10;
	}

	@Override
	public void createGridColumn(ColumnSettings column) {
		switch (column.getEntityFieldName()) {		
		case ILocalEstimate.FIELD_NAME:
			createGridColumn(column, ILocalEstimatePresenter::getName, ILocalEstimate.FIELD_NAME)
				.setEditorComponent(new TextField(), ILocalEstimatePresenter::setName).setEditable(true);
			break;
		case ILocalEstimate.FIELD_CODE:
			createGridColumn(column, ILocalEstimatePresenter::getCode, ILocalEstimate.FIELD_CODE)
				.setEditorComponent(new TextField(), ILocalEstimatePresenter::setCode).setEditable(true);
			break;
		case ILocalEstimate.FIELD_STAGE:
			createGridColumn(column, ILocalEstimatePresenter::getStage, ILocalEstimate.FIELD_STAGE)
				.setEditorComponent(new ComboBox<IStage>("", treeDataService.getStagesRepository()
																.findAll().stream().map(item -> (IStage)item)
																		.collect(Collectors.toList())), 
																			ILocalEstimatePresenter::setStage).setEditable(true);
			break;
		case ILocalEstimate.FIELD_STATUS:
			createGridColumn(column, ILocalEstimatePresenter::getStatus, ILocalEstimate.FIELD_STATUS);
			break;
		case ILocalEstimate.FIELD_CHANGEDBY:
			createGridColumn(column, ILocalEstimatePresenter::getChangeDatePresenter, ILocalEstimate.FIELD_CHANGE_DATE);
			break;
		case ILocalEstimate.FIELD_COMMENT:
			createGridColumn(column, ILocalEstimatePresenter::getComment, ILocalEstimate.FIELD_COMMENT)
				.setEditorComponent(new TextField(), ILocalEstimatePresenter::setComment).setEditable(true);
			break;
		case ILocalEstimate.FIELD_DOCUMENT:
			createGridColumn(column, ILocalEstimatePresenter::getDocumentCaption, ILocalEstimate.FIELD_DOCUMENT);
			break;
		case ILocalEstimate.FIELD_DRAWING:
			createGridColumn(column, ILocalEstimatePresenter::getDrawing, ILocalEstimate.FIELD_DRAWING);
			break;
		case ILocalEstimate.FIELD_ESTIMATEHEAD:
			createGridColumn(column, ILocalEstimatePresenter::getEstimateHeadCaption, ILocalEstimate.FIELD_ESTIMATEHEAD);
			break;
		case ILocalEstimate.FIELD_ESTIMATECALCULATION:
			createGridColumn(column, ILocalEstimatePresenter::getEstimateCalculationCaption, ILocalEstimate.FIELD_ESTIMATECALCULATION);
			break;
		case ILocalEstimate.FIELD_OBJECTESTIMATE:
			createGridColumn(column, ILocalEstimatePresenter::getObjectEstimateCaption, ILocalEstimate.FIELD_OBJECTESTIMATE);
			break;
		case ILocalEstimate.FIELD_ID:
			createGridColumn(column, ILocalEstimatePresenter::getId, ILocalEstimate.FIELD_ID);
			break;
		case ILocalEstimate.FIELD_VERSION:
			createGridColumn(column, ILocalEstimatePresenter::getVersion, ILocalEstimate.FIELD_VERSION);
			break;
		case ILocalEstimate.FIELD_CREATE_DATE:
			createGridColumn(column, ILocalEstimatePresenter::getCreateDatePresenter, ILocalEstimate.BASE_FIELD_CREATE_DATE);
			break;
		case ILocalEstimate.FIELD_CHANGE_DATE:
			createGridColumn(column, ILocalEstimatePresenter::getChangeDatePresenter, ILocalEstimate.BASE_FIELD_CHANGE_DATE);
			break;
			default:
		}
	}

	public <T> Column<ILocalEstimatePresenter, T> createGridColumn(ColumnSettings columnSettings,
			ValueProvider<ILocalEstimatePresenter, T> provider, String field) {

		Column<ILocalEstimatePresenter, T> column = this.grid.addColumn(provider);
		column.setSortProperty(columnSettings.getEntityFieldName());
		column.setSortable(true);
		column.setCaption(Entities.getEntityFieldText(field, messageSource));
		Double width = columnSettings.getWidth();

		if (Objects.nonNull(width) && Double.isFinite(width) && width > 1) {
			column.setWidth(width);
		} else {
			column.setWidthUndefined();
		}

		if (!columnSettings.isShown()) {
			column.setHidden(true);
		}

		column.setId(columnSettings.getEntityFieldName());

		return column;
	}

	@Override
	public void saveGridItem(ILocalEstimatePresenter item) {
		// TODO Auto-generated method stub
		
	}

}
