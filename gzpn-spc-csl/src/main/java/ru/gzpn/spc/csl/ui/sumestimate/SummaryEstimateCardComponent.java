package ru.gzpn.spc.csl.ui.sumestimate;

import com.vaadin.data.Binder;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import ru.gzpn.spc.csl.model.presenters.interfaces.IEstimateCalculationPresenter;
import ru.gzpn.spc.csl.services.bl.interfaces.ISummaryEstimateCardService;
import ru.gzpn.spc.csl.ui.common.I18n;

@SuppressWarnings("serial")
public class SummaryEstimateCardComponent extends AbstarctSummaryEstimateCardComponent implements I18n {
	
	public static final String I18N_ESTIMATECALCULATIONFILEDS_CAP = "SummaryEstimateCardComponent.estimatecalculationfileds.cap";
	
	public static final String I18N_ESTIMATECALCULATIONFILED_EST_CODE = "SummaryEstimateCardComponent.calcFields.estimateCode.cap";
	public static final String I18N_ESTIMATECALCULATIONFILED_NAME = "SummaryEstimateCardComponent.calcFields.name.cap";
	public static final String I18N_ESTIMATECALCULATIONFILE_PRJ_CODE = "SummaryEstimateCardComponent.calcFields.projectCode.cap";
	public static final String I18N_ESTIMATECALCULATIONFILED_DATE = "SummaryEstimateCardComponent.calcFields.createDate.cap";
	
	private CssLayout calculationFieldsLayout;
	private Binder<IEstimateCalculationPresenter> calculationFieldsBinder;
	private TextField estimateCodeField;
	private TextField estimateName;
	private TextField projectCodeField;
	private DateField estimateCreateDateField;
	
	public SummaryEstimateCardComponent(ISummaryEstimateCardService service) {
		super(service);
	}

	@Override
	public VerticalLayout createBodyLayout() {
		VerticalLayout body = new VerticalLayout();
		body.setSizeFull();
		body.addComponent(createEstimateCalculationFileds());
		
		return body;
	}

	public Component createEstimateCalculationFileds() {
		Panel panel = new Panel(getI18nText(I18N_ESTIMATECALCULATIONFILEDS_CAP, messageSource));
		panel.setSizeFull();
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		layout.setMargin(true);
		calculationFieldsLayout = new CssLayout();
		layout.addComponent(calculationFieldsLayout);
		panel.setContent(layout);
		
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
		
		return panel;
	}
	
	
	private TextField createEstimateCodeField() {
		estimateCodeField = new TextField();
		calculationFieldsBinder.forField(estimateCodeField)
				.bind(IEstimateCalculationPresenter::getCode, IEstimateCalculationPresenter::setCode);
		
		return estimateCodeField;
	}

	private TextField createEstimateNameField() {
		estimateName = new TextField();
		calculationFieldsBinder.forField(estimateName)
				.bind(IEstimateCalculationPresenter::getName, IEstimateCalculationPresenter::setName);
		
		return estimateName;
	}

	private TextField createProjectCodeField() {
		projectCodeField = new TextField();
		calculationFieldsBinder.forField(projectCodeField)
				.bind(item -> item.getProject().getCode(), null);
		return projectCodeField;
	}

	private DateField createEstimateDateField() {
		estimateCreateDateField = new DateField();
		calculationFieldsBinder.forField(estimateCreateDateField)
				.bind(item -> item.getCreateDate().toLocalDate(), null);
		return estimateCreateDateField;
	}

	public HorizontalLayout wrapCalculationField(String label, AbstractField field) {
		HorizontalLayout layout = new HorizontalLayout();
		layout.addComponent(new Label(label));
		layout.addComponent(field);
		
		calculationFieldsLayout.addComponent(layout);
		return layout;
	}
}
