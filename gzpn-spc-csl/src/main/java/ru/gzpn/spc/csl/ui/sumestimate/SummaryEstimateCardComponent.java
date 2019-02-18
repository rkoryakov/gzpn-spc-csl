package ru.gzpn.spc.csl.ui.sumestimate;

import java.util.Optional;

import com.vaadin.data.Binder;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import ru.gzpn.spc.csl.model.LocalEstimate;
import ru.gzpn.spc.csl.model.enums.EstimateType;
import ru.gzpn.spc.csl.model.enums.ItemType;
import ru.gzpn.spc.csl.model.enums.PriceLevel;
import ru.gzpn.spc.csl.model.interfaces.IEstimateCalculation;
import ru.gzpn.spc.csl.model.presenters.EstimateCalculationPresenter;
import ru.gzpn.spc.csl.model.presenters.interfaces.IEstimateCalculationPresenter;
import ru.gzpn.spc.csl.model.utils.NodeWrapper;
import ru.gzpn.spc.csl.services.bl.interfaces.IEstimateCalculationService;
import ru.gzpn.spc.csl.services.bl.interfaces.ISummaryEstimateCardService;
import ru.gzpn.spc.csl.ui.common.I18n;

@SuppressWarnings("serial")
public class SummaryEstimateCardComponent extends AbstarctSummaryEstimateCardComponent implements I18n {
	
	public static final String I18N_ESTIMATECALCULATIONFILEDS_CAP = "SummaryEstimateCardComponent.estimatecalculationfileds.cap";
	
	public static final String I18N_ESTIMATECALCULATIONFILED_EST_CODE = "SummaryEstimateCardComponent.calcFields.estimateCode.cap";
	public static final String I18N_ESTIMATECALCULATIONFILED_NAME = "SummaryEstimateCardComponent.calcFields.name.cap";
	public static final String I18N_ESTIMATECALCULATIONFILE_PRJ_CODE = "SummaryEstimateCardComponent.calcFields.projectCode.cap";
	public static final String I18N_ESTIMATECALCULATIONFILED_DATE = "SummaryEstimateCardComponent.calcFields.createDate.cap";
	private static final String I18N_VIEWATTRIBUTES_CAP = "SummaryEstimateCardComponent.viewAttributes.cap";
	private static final String I18N_PRICELEVEL_CAP = "SummaryEstimateCardComponent.priceLevel.cap";
	private static final String I18N_VIEWCOMBOBOX_PLH = "SummaryEstimateCardComponent.viewCombobox.placeholder";
	private static final String I18N_ITEMSCOMBOBOX_PLH = "SummaryEstimateCardComponent.itemsCombobox.placeholder";
	
	private CssLayout calculationFieldsLayout;
	private Binder<IEstimateCalculationPresenter> calculationFieldsBinder;
	private TextField estimateCodeField;
	private TextField estimateName;
	private TextField projectCodeField;
	private DateField estimateCreateDateField;

	private CssLayout viewAttributesLayout;

	private ComboBox<EstimateType> viewComboBox;
	private ComboBox<ItemType> itemsComboBox;
	private ComboBox<PriceLevel> priceLevelComboBox;
	
	private VerticalLayout estimatesLayout;
	private VerticalLayout calculationsLayout;
	
	private LocalEstimatesTreeGridComponent localEstimatesTreeGrid;
	
	public SummaryEstimateCardComponent(ISummaryEstimateCardService service, Long estimateCalculationId, String taskId) {
		super(service, estimateCalculationId, taskId);
	}

	@Override
	public VerticalLayout createBodyLayout() {
		VerticalLayout body = new VerticalLayout();
		body.setSizeFull();
		body.addComponent(createEstimateCalculationFileds());
		body.addComponent(createViewAttributes());
		body.addComponent(createEstimatesGrid());
		return body;
	}

	public Component createEstimateCalculationFileds() {
		Panel panel = new Panel(getI18nText(I18N_ESTIMATECALCULATIONFILEDS_CAP, messageSource));
		panel.setSizeFull();
		calculationFieldsLayout = createResponsiveLayout();
		
		panel.setContent(calculationFieldsLayout);
		
		calculationFieldsBinder = new Binder<>();
		
		calculationFieldsLayout.addComponent(
				wrapCalculationField(getI18nText(I18N_ESTIMATECALCULATIONFILED_EST_CODE, messageSource), 
						createEstimateCodeField()));
		calculationFieldsLayout.addComponent(
				wrapCalculationField(getI18nText(I18N_ESTIMATECALCULATIONFILED_NAME, messageSource), 
						createEstimateNameField()));
		calculationFieldsLayout.addComponent(
				wrapCalculationField(getI18nText(I18N_ESTIMATECALCULATIONFILE_PRJ_CODE, messageSource), 
						createProjectCodeField()));
		calculationFieldsLayout.addComponent(
				wrapCalculationField(getI18nText(I18N_ESTIMATECALCULATIONFILED_DATE, messageSource), 
						createEstimateDateField()));
		
		ISummaryEstimateCardService service = (ISummaryEstimateCardService)this.service;
		IEstimateCalculationService calcService = service.getEstimateCalculationService();
		
		Optional<IEstimateCalculation> calculation = calcService.getEstimateCalculation(estimateCalculationId);
		if (calculation.isPresent()) {
			IEstimateCalculationPresenter calculationPresenter = new EstimateCalculationPresenter(calculation.get());
			calculationFieldsBinder.readBean(calculationPresenter);
		}
		
		return panel;
	}

	public TextField createEstimateCodeField() {
		estimateCodeField = new TextField();
		calculationFieldsBinder.forField(estimateCodeField)
				.bind(IEstimateCalculationPresenter::getCode, IEstimateCalculationPresenter::setCode);
		
		return estimateCodeField;
	}

	public TextField createEstimateNameField() {
		estimateName = new TextField();
		calculationFieldsBinder.forField(estimateName)
				.bind(IEstimateCalculationPresenter::getName, IEstimateCalculationPresenter::setName);
		
		return estimateName;
	}

	public TextField createProjectCodeField() {
		projectCodeField = new TextField();
		calculationFieldsBinder.forField(projectCodeField)
				.bind(item -> item.getProject().getCode(), null);
		return projectCodeField;
	}

	public DateField createEstimateDateField() {
		estimateCreateDateField = new DateField();
		calculationFieldsBinder.forField(estimateCreateDateField)
				.bind(item -> item.getCreateDate().toLocalDate(), null);
		return estimateCreateDateField;
	}

	public HorizontalLayout wrapCalculationField(String label, AbstractField field) {
		HorizontalLayout layout = new HorizontalLayout();
		field.setCaption(label);
		layout.setMargin(false);
		layout.addComponent(field);
		
		return layout;
	}
	
	public Component createViewAttributes() {
		Panel panel = new Panel(getI18nText(I18N_VIEWATTRIBUTES_CAP, messageSource));
		panel.setSizeFull();
		viewAttributesLayout = createResponsiveLayout();
		viewAttributesLayout.addComponent(createViewComboBox());
		viewAttributesLayout.addComponent(createItemsComboBox());
		viewAttributesLayout.addComponent(createPriceLevelComboBox());
		panel.setContent(viewAttributesLayout);
		return panel;
	}

	private CssLayout createResponsiveLayout() {
		return new CssLayout() {
			@Override
			protected String getCss(Component c) {
				return "margin: 10px";
			}
		};
	}
	
	private Component createViewComboBox() {
		viewComboBox = new ComboBox<>("", EstimateType.getAll());
		viewComboBox.setPlaceholder(getI18nText(I18N_VIEWCOMBOBOX_PLH, messageSource));
		return viewComboBox;
	}

	private Component createItemsComboBox() {
		itemsComboBox = new ComboBox<>("", ItemType.getAll());
		itemsComboBox.setPlaceholder(getI18nText(I18N_ITEMSCOMBOBOX_PLH, messageSource));
		return itemsComboBox;
	}

	private Component createPriceLevelComboBox() {
		priceLevelComboBox = new ComboBox<>("", PriceLevel.getAll());
		priceLevelComboBox.setPlaceholder(getI18nText(I18N_PRICELEVEL_CAP, messageSource));

		return priceLevelComboBox;
	}
	
	public Component createEstimatesGrid() {
		estimatesLayout = new VerticalLayout();
		estimatesLayout.setMargin(false);
		estimatesLayout.setSpacing(false);
		refreshEstimatesGrid();
		return estimatesLayout;
	}
	
	public void refreshEstimatesGrid() {
		ISummaryEstimateCardService estimateCardService = (ISummaryEstimateCardService)this.service;
		estimatesLayout.removeAllComponents();
		localEstimatesTreeGrid = new LocalEstimatesTreeGridComponent(estimateCardService.getProjectService(), 
																	 estimateCardService.getLocalEstimateService(), 
																	 estimateCardService.getUserSettingsService());
		
		NodeWrapper parentNode = new NodeWrapper(LocalEstimate.class.getSimpleName());
		parentNode.setId(this.estimateCalculationId);
		localEstimatesTreeGrid.getGridDataProvider().setParentNode(parentNode);
		estimatesLayout.addComponent(localEstimatesTreeGrid);
	}
}
