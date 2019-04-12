package ru.gzpn.spc.csl.ui.sumestimate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import com.vaadin.data.Binder;
import com.vaadin.data.ValueProvider;
import com.vaadin.data.converter.StringToBigDecimalConverter;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.TextField;

import ru.gzpn.spc.csl.model.EstimateCost;
import ru.gzpn.spc.csl.model.dataproviders.AbstractRegistryDataProvider;
import ru.gzpn.spc.csl.model.dataproviders.EstimateCostDataProvider;
import ru.gzpn.spc.csl.model.dataproviders.ProjectTreeDataProvider;
import ru.gzpn.spc.csl.model.enums.Entities;
import ru.gzpn.spc.csl.model.interfaces.IEstimateCost;
import ru.gzpn.spc.csl.model.jsontypes.ColumnHeaderGroup;
import ru.gzpn.spc.csl.model.jsontypes.ColumnSettings;
import ru.gzpn.spc.csl.model.jsontypes.ISettingsJson;
import ru.gzpn.spc.csl.model.jsontypes.SummaryEstimateCardSettingsJson;
import ru.gzpn.spc.csl.model.utils.NodeWrapper;
import ru.gzpn.spc.csl.services.bl.interfaces.IDataService;
import ru.gzpn.spc.csl.services.bl.interfaces.IEstimateCostService;
import ru.gzpn.spc.csl.services.bl.interfaces.IProjectService;
import ru.gzpn.spc.csl.services.bl.interfaces.IUserSettingsService;
import ru.gzpn.spc.csl.ui.common.AbstractTreeGridComponent;
import ru.gzpn.spc.csl.ui.common.AbstractTreeGridSettingsWindow;
import ru.gzpn.spc.csl.ui.common.JoinedLayout;

public class CostsTreeGridComponent extends AbstractTreeGridComponent<IEstimateCost> {
	private static final String I18N_ADDITEMBUTTON_DESC = "CostsTreeGridComponent.addItemButton.desc";
	private static final String I18N_REMOVEITEMBUTTON_DESC = "CostsTreeGridComponent.removeItemButton.desc";
	public  static final String I18N_BIGDECIMALCONVERTER_DESC = "CostsTreeGridComponent.bigdecimalconverter.desc";
	
	private boolean treeVisibility = false;
	private AbstractRegistryDataProvider<IEstimateCost, Void> gridDataProvider;
	private ProjectTreeDataProvider treeDataProvider;
	private Button addItemButton;
	private Button removeItemButton;
	
	public CostsTreeGridComponent(IProjectService treeDataService,
			IDataService<IEstimateCost, IEstimateCost> gridDataService, IUserSettingsService userSettingsService) {
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
			gridDataProvider = new EstimateCostDataProvider((IEstimateCostService)gridDataService);
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
		return new EstimateCostSettingsWindow(userSettingsService, gridDataService.getMessageSource());
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
				return ((SummaryEstimateCardSettingsJson)settingsJson).isShowEstimateCostTreeGroup();
			}

			@Override
			public NodeWrapper getTreeSettings() {
				return ((SummaryEstimateCardSettingsJson)settingsJson).getEstimateCostTreeGroup();
			}

			@Override
			public List<ColumnSettings> getColumns() {
				return ((SummaryEstimateCardSettingsJson)settingsJson).getEstimateCostColumns();
			}

			@Override
			public List<ColumnHeaderGroup> getHeaders() {
				return ((SummaryEstimateCardSettingsJson)settingsJson).getEstimateCostColumnHeaders();
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

//	public boolean isValidValue(String decimal) {
//		StringToBigDecimalConverter converter = new StringToBigDecimalConverter(new BigDecimal("0"), "Must be an amount");
//		return !converter.convertToModel(decimal,  new ValueContext(this.getCurrentLocale())).isError();
//	}
	
	@Override
	public void createGridColumn(ColumnSettings column) {
		Binder<IEstimateCost> binder = new Binder<>();
		
		switch (column.getEntityFieldName()) {
		case IEstimateCost.FIELD_MAT_CUSTOMER_SUPPLY:
			createGridColumn(column, IEstimateCost::getMatCustomerSupply, IEstimateCost.FIELD_MAT_CUSTOMER_SUPPLY)
				.setEditorBinding(binder.forField(new TextField())
						.withConverter(new StringToBigDecimalConverter(new BigDecimal("0"), getI18nText(I18N_BIGDECIMALCONVERTER_DESC, messageSource)))
						.bind(IEstimateCost::getMatCustomerSupply, IEstimateCost::setMatCustomerSupply)).setEditable(true);
			break;
			
		case IEstimateCost.FIELD_MAT_CONTRACTOR_SUPPLY:
			createGridColumn(column, IEstimateCost::getMatContractorSupply, IEstimateCost.FIELD_MAT_CONTRACTOR_SUPPLY)
				.setEditorBinding(binder.forField(new TextField())
						.withConverter(new StringToBigDecimalConverter(new BigDecimal("0"), getI18nText(I18N_BIGDECIMALCONVERTER_DESC, messageSource)))
						.bind(IEstimateCost::getMatContractorSupply, IEstimateCost::setMatContractorSupply)).setEditable(true);
			break;
		case IEstimateCost.FIELD_MAT_PERCENT_MANUAL_SUPPLY:
			createGridColumn(column, IEstimateCost::getMatPercentManual, IEstimateCost.FIELD_MAT_PERCENT_MANUAL_SUPPLY)
				.setEditorBinding(binder.forField(new TextField())
						.withConverter(new StringToBigDecimalConverter(new BigDecimal("0"), getI18nText(I18N_BIGDECIMALCONVERTER_DESC, messageSource)))
						.bind(IEstimateCost::getMatPercentManual, IEstimateCost::setMatPercentManual)).setEditable(true);
			break;
		case IEstimateCost.FIELD_OZP:
			createGridColumn(column, IEstimateCost::getOzp, IEstimateCost.FIELD_OZP)
			.setEditorBinding(binder.forField(new TextField())
					.withConverter(new StringToBigDecimalConverter(new BigDecimal("0"), getI18nText(I18N_BIGDECIMALCONVERTER_DESC, messageSource)))
					.bind(IEstimateCost::getOzp, IEstimateCost::setOzp)).setEditable(true);
			break;
		case IEstimateCost.FIELD_EMM:
			createGridColumn(column, IEstimateCost::getEmm, IEstimateCost.FIELD_EMM)
			.setEditorBinding(binder.forField(new TextField())
					.withConverter(new StringToBigDecimalConverter(new BigDecimal("0"), getI18nText(I18N_BIGDECIMALCONVERTER_DESC, messageSource)))
					.bind(IEstimateCost::getEmm, IEstimateCost::setEmm)).setEditable(true);
			break;
		case IEstimateCost.FIELD_ZPM:
			createGridColumn(column, IEstimateCost::getZpm, IEstimateCost.FIELD_ZPM)
			.setEditorBinding(binder.forField(new TextField())
					.withConverter(new StringToBigDecimalConverter(new BigDecimal("0"), getI18nText(I18N_BIGDECIMALCONVERTER_DESC, messageSource)))
					.bind(IEstimateCost::getZpm, IEstimateCost::setZpm)).setEditable(true);
			break;
		case IEstimateCost.FIELD_NR:
			createGridColumn(column, IEstimateCost::getNr, IEstimateCost.FIELD_NR)
			.setEditorBinding(binder.forField(new TextField())
					.withConverter(new StringToBigDecimalConverter(new BigDecimal("0"), getI18nText(I18N_BIGDECIMALCONVERTER_DESC, messageSource)))
					.bind(IEstimateCost::getNr, IEstimateCost::setNr)).setEditable(true);
			break;
		case IEstimateCost.FIELD_SP:
			createGridColumn(column, IEstimateCost::getSp, IEstimateCost.FIELD_SP)
			.setEditorBinding(binder.forField(new TextField())
					.withConverter(new StringToBigDecimalConverter(new BigDecimal("0"), getI18nText(I18N_BIGDECIMALCONVERTER_DESC, messageSource)))
					.bind(IEstimateCost::getSp, IEstimateCost::setSp)).setEditable(true);
			break;
		case IEstimateCost.FIELD_SMR_TOTAL:
			createGridColumn(column, IEstimateCost::getSmrTotal, IEstimateCost.FIELD_SMR_TOTAL)
			.setEditorBinding(binder.forField(new TextField())
					.withConverter(new StringToBigDecimalConverter(new BigDecimal("0"), getI18nText(I18N_BIGDECIMALCONVERTER_DESC, messageSource)))
					.bind(IEstimateCost::getSmrTotal, IEstimateCost::setSmrTotal)).setEditable(true);
			break;
		case IEstimateCost.FIELD_DEVICES_TOTAL:
			createGridColumn(column, IEstimateCost::getDevicesTotal, IEstimateCost.FIELD_DEVICES_TOTAL)
			.setEditorBinding(binder.forField(new TextField())
					.withConverter(new StringToBigDecimalConverter(new BigDecimal("0"), getI18nText(I18N_BIGDECIMALCONVERTER_DESC, messageSource)))
					.bind(IEstimateCost::getDevicesTotal, IEstimateCost::setDevicesTotal)).setEditable(true);
			break;
		case IEstimateCost.FIELD_DEVICES_PERCENT_MANUAL:
			createGridColumn(column, IEstimateCost::getDevicesPercentManual, IEstimateCost.FIELD_DEVICES_PERCENT_MANUAL)
			.setEditorBinding(binder.forField(new TextField())
					.withConverter(new StringToBigDecimalConverter(new BigDecimal("0"), getI18nText(I18N_BIGDECIMALCONVERTER_DESC, messageSource)))
					.bind(IEstimateCost::getDevicesPercentManual, IEstimateCost::setDevicesPercentManual)).setEditable(true);
			break;
		case IEstimateCost.FIELD_DEVICES_CUSTOMER_SUPPLY:
			createGridColumn(column, IEstimateCost::getDevicesCustomerSupply, IEstimateCost.FIELD_DEVICES_CUSTOMER_SUPPLY)
			.setEditorBinding(binder.forField(new TextField())
					.withConverter(new StringToBigDecimalConverter(new BigDecimal("0"), getI18nText(I18N_BIGDECIMALCONVERTER_DESC, messageSource)))
					.bind(IEstimateCost::getDevicesCustomerSupply, IEstimateCost::setDevicesCustomerSupply)).setEditable(true);
			break;
		case IEstimateCost.FIELD_DEVICES_CONTRACTOR_SUPPLY:
			createGridColumn(column, IEstimateCost::getDevicesContractorSupply, IEstimateCost.FIELD_DEVICES_CONTRACTOR_SUPPLY)
			.setEditorBinding(binder.forField(new TextField())
					.withConverter(new StringToBigDecimalConverter(new BigDecimal("0"), getI18nText(I18N_BIGDECIMALCONVERTER_DESC, messageSource)))
					.bind(IEstimateCost::getDevicesContractorSupply, IEstimateCost::setDevicesContractorSupply)).setEditable(true);
			break;
		case IEstimateCost.FIELD_OTHER:
			createGridColumn(column, IEstimateCost::getOther, IEstimateCost.FIELD_OTHER)
			.setEditorBinding(binder.forField(new TextField())
					.withConverter(new StringToBigDecimalConverter(new BigDecimal("0"), getI18nText(I18N_BIGDECIMALCONVERTER_DESC, messageSource)))
					.bind(IEstimateCost::getOther, IEstimateCost::setOther)).setEditable(true);
			break;
		case IEstimateCost.FIELD_TOTAL:
			createGridColumn(column, IEstimateCost::getTotal, IEstimateCost.FIELD_TOTAL)
			.setEditorBinding(binder.forField(new TextField())
					.withConverter(new StringToBigDecimalConverter(new BigDecimal("0"), getI18nText(I18N_BIGDECIMALCONVERTER_DESC, messageSource)))
					.bind(IEstimateCost::getTotal, IEstimateCost::setTotal)).setEditable(true);
			break;
		case IEstimateCost.FIELD_SERVICES:
			createGridColumn(column, IEstimateCost::getServices, IEstimateCost.FIELD_SERVICES)
			.setEditorBinding(binder.forField(new TextField())
					.withConverter(new StringToBigDecimalConverter(new BigDecimal("0"), getI18nText(I18N_BIGDECIMALCONVERTER_DESC, messageSource)))
					.bind(IEstimateCost::getServices, IEstimateCost::setServices)).setEditable(true);
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
			IEstimateCost ec = new EstimateCost();
			
			((IEstimateCostService)gridDataService)
				.createEstimateCostByLocal(ec, getGridDataProvider()
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
