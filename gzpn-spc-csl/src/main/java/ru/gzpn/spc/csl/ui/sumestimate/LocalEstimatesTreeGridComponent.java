package ru.gzpn.spc.csl.ui.sumestimate;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.vaadin.data.ValueProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.TextField;

import ru.gzpn.spc.csl.model.LocalEstimate;
import ru.gzpn.spc.csl.model.dataproviders.AbstractRegistryDataProvider;
import ru.gzpn.spc.csl.model.dataproviders.LocalEstimateDataProvider;
import ru.gzpn.spc.csl.model.dataproviders.ProjectTreeDataProvider;
import ru.gzpn.spc.csl.model.enums.Entities;
import ru.gzpn.spc.csl.model.interfaces.ILocalEstimate;
import ru.gzpn.spc.csl.model.interfaces.IStage;
import ru.gzpn.spc.csl.model.jsontypes.ColumnHeaderGroup;
import ru.gzpn.spc.csl.model.jsontypes.ColumnSettings;
import ru.gzpn.spc.csl.model.jsontypes.ISettingsJson;
import ru.gzpn.spc.csl.model.jsontypes.SummaryEstimateCardSettingsJson;
import ru.gzpn.spc.csl.model.presenters.interfaces.IEstimateCost;
import ru.gzpn.spc.csl.model.utils.NodeWrapper;
import ru.gzpn.spc.csl.services.bl.interfaces.IDataService;
import ru.gzpn.spc.csl.services.bl.interfaces.ILocalEstimateService;
import ru.gzpn.spc.csl.services.bl.interfaces.IProjectService;
import ru.gzpn.spc.csl.services.bl.interfaces.IUserSettigsService;
import ru.gzpn.spc.csl.ui.common.AbstractTreeGridComponent;
import ru.gzpn.spc.csl.ui.common.AbstractTreeGridSettingsWindow;
import ru.gzpn.spc.csl.ui.common.JoinedLayout;

@SuppressWarnings("serial")
public class LocalEstimatesTreeGridComponent extends AbstractTreeGridComponent<IEstimateCost> {

	private static final String I18N_ADDITEMBUTTON_DESC = "LocalEstimatesTreeGridComponent.addItemButton.desc";
	private static final String I18N_REMOVEITEMBUTTON_DESC = "LocalEstimatesTreeGridComponent.removeItemButton.desc";
	private boolean treeVisibility = false;
	private AbstractRegistryDataProvider<IEstimateCost, Void> gridDataProvider;
	private ProjectTreeDataProvider treeDataProvider;
	private Button addItemButton;
	private Button removeItemButton;
	
	public LocalEstimatesTreeGridComponent(IProjectService treeDataService,
			IDataService<ILocalEstimate, IEstimateCost> gridDataService, IUserSettigsService userSettingsService) {
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
	public AbstractRegistryDataProvider<IEstimateCost, ?> getGridDataProvider() {
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
		SummaryEstimateCardSettingsJson settings = new SummaryEstimateCardSettingsJson();
		settings.setSplitPosition(30);
		settings.setShowTree(false);
		ISettingsJson settingsJson = userSettingsService.getSummaryEstimateCardSettings(user, settings);
		
		return new ISettingsJson() {
			@Override
			public boolean isShownTree() {
				return settingsJson.isShownTree();
			}

			@Override
			public NodeWrapper getTreeSettings() {
				return ((SummaryEstimateCardSettingsJson)settingsJson).getLocalEstimatesTreeGroup();
			}

			@Override
			public List<ColumnSettings> getColumns() {
				return ((SummaryEstimateCardSettingsJson)settingsJson).getLocalEstimatesColumns();
			}

			@Override
			public List<ColumnHeaderGroup> getHeaders() {
				return ((SummaryEstimateCardSettingsJson)settingsJson).getLocalEstimatesColumnHeaders();
			}
		};
	}

	@Override
	public float getSplitPosition() {
		return ((SummaryEstimateCardSettingsJson)getSettings()).getSplitPosition();
	}

	@Override
	public int getGridRowsCount() {
		return 5;
	}

	@Override
	public void createGridColumn(ColumnSettings column) {
		switch (column.getEntityFieldName()) {		
		case ILocalEstimate.FIELD_NAME:
			createGridColumn(column, IEstimateCost::getName, ILocalEstimate.FIELD_NAME)
				.setEditorComponent(new TextField(), IEstimateCost::setName).setEditable(true);
			break;
		case ILocalEstimate.FIELD_CODE:
			createGridColumn(column, IEstimateCost::getCode, ILocalEstimate.FIELD_CODE)
				.setEditorComponent(new TextField(), IEstimateCost::setCode).setEditable(true);
			break;
		case ILocalEstimate.FIELD_STAGE:
			createGridColumn(column, IEstimateCost::getStage, ILocalEstimate.FIELD_STAGE)
				.setEditorComponent(new ComboBox<IStage>("", treeDataService.getStagesRepository()
																.findAll().stream().map(item -> (IStage)item)
																		.collect(Collectors.toList())), 
																			IEstimateCost::setStage).setEditable(true);
			break;
		case ILocalEstimate.FIELD_STATUS:
			createGridColumn(column, IEstimateCost::getStatus, ILocalEstimate.FIELD_STATUS);
			break;
		case ILocalEstimate.FIELD_CHANGEDBY:
			createGridColumn(column, IEstimateCost::getChangeDatePresenter, ILocalEstimate.FIELD_CHANGE_DATE);
			break;
		case ILocalEstimate.FIELD_COMMENT:
			createGridColumn(column, IEstimateCost::getComment, ILocalEstimate.FIELD_COMMENT)
				.setEditorComponent(new TextField(), IEstimateCost::setComment).setEditable(true);
			break;
		case ILocalEstimate.FIELD_DOCUMENT:
			createGridColumn(column, IEstimateCost::getDocumentCaption, ILocalEstimate.FIELD_DOCUMENT);
			break;
		case ILocalEstimate.FIELD_DRAWING:
			createGridColumn(column, IEstimateCost::getDrawing, ILocalEstimate.FIELD_DRAWING);
			break;
		case ILocalEstimate.FIELD_ESTIMATEHEAD:
			createGridColumn(column, IEstimateCost::getEstimateHeadCaption, ILocalEstimate.FIELD_ESTIMATEHEAD);
			break;
		case ILocalEstimate.FIELD_ESTIMATECALCULATION:
			createGridColumn(column, IEstimateCost::getEstimateCalculationCaption, ILocalEstimate.FIELD_ESTIMATECALCULATION);
			break;
		case ILocalEstimate.FIELD_OBJECTESTIMATE:
			createGridColumn(column, IEstimateCost::getObjectEstimateCaption, ILocalEstimate.FIELD_OBJECTESTIMATE);
			break;
		case ILocalEstimate.FIELD_ID:
			createGridColumn(column, IEstimateCost::getId, ILocalEstimate.FIELD_ID);
			break;
		case ILocalEstimate.FIELD_VERSION:
			createGridColumn(column, IEstimateCost::getVersion, ILocalEstimate.FIELD_VERSION);
			break;
		case ILocalEstimate.FIELD_CREATE_DATE:
			createGridColumn(column, IEstimateCost::getCreateDatePresenter, ILocalEstimate.BASE_FIELD_CREATE_DATE);
			break;
		case ILocalEstimate.FIELD_CHANGE_DATE:
			createGridColumn(column, IEstimateCost::getChangeDatePresenter, ILocalEstimate.BASE_FIELD_CHANGE_DATE);
			break;
			default:
		}
		
	}

	public <T> Column<IEstimateCost, T> createGridColumn(ColumnSettings columnSettings, 
													ValueProvider<IEstimateCost, T> provider, 
													String field) {
		
		Column<IEstimateCost, T> column = this.grid.addColumn(provider);
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
	
	@SuppressWarnings("unchecked")
	@Override
	public Component createGridButtons() {
		JoinedLayout<Button, Button> buttons = new JoinedLayout<>();
		addItemButton = new Button(VaadinIcons.PLUS);
		addItemButton.addClickListener(clickEvent -> {
			ILocalEstimate le = new LocalEstimate();
			
			((ILocalEstimateService)gridDataService)
				.cretaeLocalEstimateByCalculationId(le, getGridDataProvider()
						.getParentNode().getId());
			getGridDataProvider().refreshAll();
		});
		
		removeItemButton = new Button(VaadinIcons.MINUS);
		removeItemButton.addClickListener(clickEvent -> {
			grid.getSelectedItems().forEach(item -> {
				gridDataService.remove(item);
			});
			
			grid.deselectAll();
			grid.getDataProvider().refreshAll();
		});
		
		addItemButton.setDescription(getI18nText(I18N_ADDITEMBUTTON_DESC, messageSource));
		removeItemButton.setDescription(getI18nText(I18N_REMOVEITEMBUTTON_DESC, messageSource));
		addItemButton.setEnabled(true);
		removeItemButton.setEnabled(true);
		
		buttons.addComponent(addItemButton);
		buttons.addComponent(removeItemButton);
		
		return buttons;
	}
	
	@Override
	public void saveGridItem(IEstimateCost item) {
		gridDataService.save(item);
		
	}

}