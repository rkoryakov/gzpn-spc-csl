package ru.gzpn.spc.csl.ui.estimatereg;

import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.vaadin.data.ValueProvider;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.HeaderCell;
import com.vaadin.ui.components.grid.HeaderRow;

import ru.gzpn.spc.csl.model.enums.Entities;
import ru.gzpn.spc.csl.model.interfaces.IEstimateCalculation;
import ru.gzpn.spc.csl.model.jsontypes.ColumnHeaderGroup;
import ru.gzpn.spc.csl.model.jsontypes.ColumnSettings;
import ru.gzpn.spc.csl.model.jsontypes.EstimatesRegSettingsJson;
import ru.gzpn.spc.csl.services.bl.interfaces.IEstimateRegisterService;
import ru.gzpn.spc.csl.ui.common.AbstractRegisterDataProvider;
import ru.gzpn.spc.csl.ui.common.RegisterComponent;

public class EstimateRegisterComponent extends RegisterComponent {
	
	private Grid<IEstimateCalculationPresenter> estimateGrid;
	private EstimateCalculationDataProvider estimateCalculationDataProvider;
	private static final int ESTIMATE_GRID_ROWS = 11;
	
	public EstimateRegisterComponent(IEstimateRegisterService service) {
		super(service);
		
		setSizeFull();
		this.createItemButton.setEnabled(false);
		this.createItemButton.setVisible(false);
	}

	@Override
	public VerticalLayout createBodyLayout() {
		VerticalLayout bodyLayout = new VerticalLayout();
		bodyLayout.setSizeFull();
		bodyLayout.setMargin(false);
		bodyLayout.setSpacing(false);
		bodyLayout.addComponent(createEstimateGrid());
		return bodyLayout;
	}

	@Override
	public AbstractRegisterDataProvider getDataProvider() {
		return new EstimateCalculationDataProvider(((IEstimateRegisterService)service).getEstimateCalculationService());
	}

	@Override
	public Grid getRegisterGrid() {
		return estimateGrid;
	}
	
	private Component createEstimateGrid() {
		estimateGrid = new Grid<>();
		refreshEstimateGrid();
		return estimateGrid;
	}
	
	public void refreshEstimateGrid() {
		estimateCalculationDataProvider = new EstimateCalculationDataProvider(((IEstimateRegisterService) service).getEstimateCalculationService());
		
		EstimatesRegSettingsJson userSettings = (EstimatesRegSettingsJson)service.getUserSettingsService().getEstimatesRegSettings(this.user, new EstimatesRegSettingsJson());
		List<ColumnSettings> columnSettings = userSettings.getRightResultColumns();
		
		columnSettings.sort((cs1, cs2) -> 
			Integer.compare(cs1.getOrderIndex(), cs2.getOrderIndex())
		);
		estimateGrid.removeAllColumns();
		columnSettings.forEach(this::addEstimateGridColumn);
		estimateGrid.setSizeFull();
		estimateGrid.setHeightByRows(ESTIMATE_GRID_ROWS);
		estimateGrid.setColumnReorderingAllowed(true);
		estimateCalculationDataProvider.setShownColumns(columnSettings);
		estimateGrid.setDataProvider(estimateCalculationDataProvider);
		
		// test column headers
		userSettings.getRightResultColumns();
		createEstimateHeaderColumns(userSettings);
	}
	
	public void createEstimateHeaderColumns(EstimatesRegSettingsJson userSettings) {
		if (userSettings.hasRightColumnHeaders()) {
			refreshColumnHeaderGroups(userSettings.getRightColumnHeaders());
		}
		estimateGrid.setHeightByRows(ESTIMATE_GRID_ROWS - estimateGrid.getHeaderRowCount() + 1);
	}
	
	public void refreshColumnHeaderGroups(List<ColumnHeaderGroup> groups) {
		final String headStyle = "v-grid-header-align-left";
		Deque<List<ColumnHeaderGroup>> childGroups = new LinkedList<>();
		childGroups.push(groups);
		removePrepandedHeaderRows();
		
		HeaderRow headerRow = estimateGrid.prependHeaderRow();
		headerRow.setStyleName(headStyle);

		while (!childGroups.isEmpty()) {
			List<ColumnHeaderGroup> list = childGroups.pop();
			Iterator<ColumnHeaderGroup> it = list.iterator();
			
			while (it.hasNext()) {
				ColumnHeaderGroup g = it.next();

				if (g.hasChildrenGroups()) {
					HeaderRow subRow = estimateGrid.prependHeaderRow();
					subRow.setStyleName(headStyle);
					childGroups.push(g.getChildren());
					HeaderCell groupCell = subRow.join(getEstimateColumnIds(g.getChildren()));
					groupCell.setText(g.getCaption());
					break;
				
				} else if (g.hasColumns()) {
					
						HeaderCell groupCell = headerRow.join(g.getColumns().stream().map(column -> 
								column.getEntityFieldName())
										.toArray(String[]::new));
						
						groupCell.setText(g.getCaption());
						childGroups.pollFirst();
				}
			}
		}
	}
	
	public void removePrepandedHeaderRows() {
		int count = estimateGrid.getHeaderRowCount();
		if (count > 1) {
			for (int i = 0; i < count - 1; i ++) {
				estimateGrid.removeHeaderRow(0);
			}
		}
	}
	
	/**
	 * Get column Ids that are contained in the given head groups:
	 *  _____________________________________
	 * |	 Header1	 |  	Header2	     |
	 * |colum1 | column2 | column3 | column4 |
	 * 
	 */
	public String[] getEstimateColumnIds(List<ColumnHeaderGroup> groups) {
		Set<String> columnIds = new HashSet<>();

		for (int i = 0; i < groups.size(); i ++) {
			Iterator<ColumnHeaderGroup> it = groups.listIterator(i);
			while (it.hasNext()) {
				ColumnHeaderGroup g = it.next();
				if (g.hasChildrenGroups()) {
					it = g.getChildren().iterator();
				} else if (g.hasColumns()) {
					columnIds.addAll(g.getColumns().stream().map(c -> c.getEntityFieldName()).collect(Collectors.toSet()));
				}
			}
		}
		return columnIds.toArray(new String[0]);
	}

	public void addEstimateGridColumn(ColumnSettings settings) {
		switch (settings.getEntityFieldName()) {
		case IEstimateCalculation.FIELD_CODE:
			addEstimateGridColumn(settings, IEstimateCalculationPresenter::getCode, IEstimateCalculation.FIELD_CODE);
			break;
		case IEstimateCalculation.FILED_CPROJECT:
			addEstimateGridColumn(settings, IEstimateCalculationPresenter::getCProjectCaption, IEstimateCalculation.FILED_CPROJECT);
			break;
		case IEstimateCalculation.FIELD_NAME:
			addEstimateGridColumn(settings, IEstimateCalculationPresenter::getName, IEstimateCalculation.FIELD_NAME);
			break;
		case IEstimateCalculation.FILED_HANDLER:
			addEstimateGridColumn(settings, IEstimateCalculationPresenter::getHandler, IEstimateCalculation.FILED_HANDLER);
			break;
		case IEstimateCalculation.FIELD_CREATE_DATE:
			addEstimateGridColumn(settings, IEstimateCalculationPresenter::getChangeDate, IEstimateCalculation.FIELD_CREATE_DATE);
			break;
			default:
		}
	}
	
	public <T> void addEstimateGridColumn(ColumnSettings settings, ValueProvider<IEstimateCalculationPresenter, T> provider, String field) {
		
		Column<IEstimateCalculationPresenter, T> column = estimateGrid.addColumn(provider);
		column.setSortProperty(settings.getEntityFieldName());
		column.setSortable(true);
		column.setCaption(Entities.getEntityFieldText(field, messageSource, getLocale()));
		Double width = settings.getWidth();
		
		if (Objects.nonNull(width) && Double.isFinite(width) && width > 1) {
			column.setWidth(width);
		} else {
			column.setWidthUndefined();
		}
		
		if (!settings.isShown()) {	
			column.setHidden(true);
		}
		
		column.setId(settings.getEntityFieldName());
	}
	
	@Override
	public void refreshUiElements() {
		refreshEstimateGrid();
	}


}
