package ru.gzpn.spc.csl.ui.contractreg;

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
import ru.gzpn.spc.csl.model.interfaces.IContract;
import ru.gzpn.spc.csl.model.jsontypes.ColumnHeaderGroup;
import ru.gzpn.spc.csl.model.jsontypes.ColumnSettings;
import ru.gzpn.spc.csl.model.jsontypes.ContractsRegSettingsJson;
import ru.gzpn.spc.csl.services.bl.interfaces.IContractRegisterService;
import ru.gzpn.spc.csl.ui.common.AbstractRegisterDataProvider;
import ru.gzpn.spc.csl.ui.common.RegisterComponent;

public class ContractRegisterComponent extends RegisterComponent {

	private Grid<IContractPresenter> contractGrid;
	private ContractDataProvider contractDataProvider;
	private static final int CONTRACT_GRID_ROWS = 11;
	
	public ContractRegisterComponent(IContractRegisterService service) {
		super(service);
		//contractDataProvider = new ContractDataProvider(service.getContractService());
		setSizeFull();
	}

	@Override
	public VerticalLayout createBodyLayout() {
		VerticalLayout bodyLayout = new VerticalLayout();
		bodyLayout.setSizeFull();
		bodyLayout.setMargin(false);
		bodyLayout.setSpacing(false);
		bodyLayout.addComponent(createContractGrid());
		return bodyLayout;
	}
	
	@Override
	public AbstractRegisterDataProvider getDataProvider() {
		if (contractDataProvider == null) {
			contractDataProvider = new ContractDataProvider(((IContractRegisterService)service).getContractService());
		}
		return contractDataProvider; 
	}

	@Override
	public Grid getRegisterGrid() {
		return contractGrid;
	}

	private Component createContractGrid() {
		contractGrid = new Grid<>();
		refreshContractGrid();
		return contractGrid;
	}
	
	public void refreshContractGrid() {
		contractDataProvider = new ContractDataProvider(((IContractRegisterService)service).getContractService());
		
		ContractsRegSettingsJson userSettings = (ContractsRegSettingsJson)service.getUserSettingsService().getContracrRegSettings(this.user, new ContractsRegSettingsJson());
		List<ColumnSettings> columnSettings = userSettings.getRightResultColumns();
		
		columnSettings.sort((cs1, cs2) -> 
			Integer.compare(cs1.getOrderIndex(), cs2.getOrderIndex())
		);
		contractGrid.removeAllColumns();
		columnSettings.forEach(this::addContractGridColumn);
		contractGrid.setSizeFull();
		contractGrid.setHeightByRows(CONTRACT_GRID_ROWS);
		contractGrid.setColumnReorderingAllowed(true);
		contractDataProvider.setShownColumns(columnSettings);
		contractGrid.setDataProvider(contractDataProvider);
		
		// test column headers
		userSettings.getRightResultColumns();
		createContractsRegSettingsJson(userSettings);
	}
	
	public void createContractsRegSettingsJson(ContractsRegSettingsJson userSettings) {
		if (userSettings.hasRightColumnHeaders()) {
			refreshColumnHeaderGroups(userSettings.getRightColumnHeaders());
		}
		contractGrid.setHeightByRows(CONTRACT_GRID_ROWS - contractGrid.getHeaderRowCount() + 1);
	}
	
	public void refreshColumnHeaderGroups(List<ColumnHeaderGroup> groups) {
		final String headStyle = "v-grid-header-align-left";
		Deque<List<ColumnHeaderGroup>> childGroups = new LinkedList<>();
		childGroups.push(groups);
		removePrepandedHeaderRows();
		
		HeaderRow headerRow = contractGrid.prependHeaderRow();
		headerRow.setStyleName(headStyle);

		while (!childGroups.isEmpty()) {
			List<ColumnHeaderGroup> list = childGroups.pop();
			Iterator<ColumnHeaderGroup> it = list.iterator();
			
			while (it.hasNext()) {
				ColumnHeaderGroup g = it.next();

				if (g.hasChildrenGroups()) {
					HeaderRow subRow = contractGrid.prependHeaderRow();
					subRow.setStyleName(headStyle);
					childGroups.push(g.getChildren());
					HeaderCell groupCell = subRow.join(getContractColumnIds(g.getChildren()));
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
		int count = contractGrid.getHeaderRowCount();
		if (count > 1) {
			for (int i = 0; i < count - 1; i ++) {
				contractGrid.removeHeaderRow(0);
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
	public String[] getContractColumnIds(List<ColumnHeaderGroup> groups) {
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

	public void addContractGridColumn(ColumnSettings settings) {
		switch (settings.getEntityFieldName()) {
		case IContract.FIELD_CODE:
			addContractGridColumn(settings, IContractPresenter::getCode, IContract.FIELD_CODE);
			break;
		case IContract.FIELD_CUSTOMERNAME:
			addContractGridColumn(settings, IContractPresenter::getCustomerName, IContract.FIELD_CUSTOMERNAME);
			break;
		case IContract.FIELD_EXECUTORNAME:
			addContractGridColumn(settings, IContractPresenter::getExecutorName, IContract.FIELD_EXECUTORNAME);
			break;
		case IContract.FIELD_SIGNINGDATE:
			addContractGridColumn(settings, IContractPresenter::getCreateDatePresenter, IContract.FIELD_CREATE_DATE);
			break;
			default:
		}
	}
	
	public <T> void addContractGridColumn(ColumnSettings settings, ValueProvider<IContractPresenter, T> provider, String field) {
		
		Column<IContractPresenter, T> column = contractGrid.addColumn(provider);
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
		refreshContractGrid();
	}
}
