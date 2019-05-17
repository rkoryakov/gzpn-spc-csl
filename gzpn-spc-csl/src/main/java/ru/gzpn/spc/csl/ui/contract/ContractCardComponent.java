package ru.gzpn.spc.csl.ui.contract;

import java.util.Optional;
import java.util.stream.Collectors;

import com.vaadin.data.Binder;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import ru.gzpn.spc.csl.model.EstimateCalculation;
import ru.gzpn.spc.csl.model.enums.ContractType;
import ru.gzpn.spc.csl.model.interfaces.ICProject;
import ru.gzpn.spc.csl.model.interfaces.IContract;
import ru.gzpn.spc.csl.model.interfaces.ILocalEstimate;
import ru.gzpn.spc.csl.model.utils.NodeWrapper;
import ru.gzpn.spc.csl.services.bl.interfaces.IContractCardService;
import ru.gzpn.spc.csl.ui.common.ConfirmDialogWindow;
import ru.gzpn.spc.csl.ui.common.I18n;

@SuppressWarnings("serial")
public class ContractCardComponent extends AbstarctContractCardComponent implements I18n {
	
	public static final String I18N_CONTRACTCARDFILEDS_CAP = "ContractCardComponent.fields.cap";
	
	public static final String I18N_SAVED_NOTIFICATION_CAP = "ContractCardComponent.notifications.save.cap";
	public static final String I18N_SAVED_NOTIFICATION_DES = "ContractCardComponent.notifications.save.des";
	public static final String I18N_CONTRACT_CODE_FIELD = "ContractCardComponent.fields.contractCode";
	public static final String I18N_EXECUTOR_NAME_FIELD = "ContractCardComponent.fields.executorName";
	public static final String I18N_CUSTOMER_NAME_FIELD = "ContractCardComponent.fields.customerName";
	public static final String I18N_CONTRACTTYPE_FIELD = "ContractCardComponent.fields.contractType";
	public static final String I18N_PROJECT_FIELD = "ContractCardComponent.fields.project";
	public static final String I18N_SIGNINGDATE_FIELD = "ContractCardComponent.fields.signingDate";
	public static final String I18N_STARTDATE_FIELD = "ContractCardComponent.fields.startDate";
	public static final String I18N_ENDDATE_FIELD = "ContractCardComponent.fields.endDate";
	public static final String I18N_MILESTONES_TAB = "ContractCardComponent.tabs.milestones";
	public static final String I18N_ESTIMATES_TAB = "ContractCardComponent.tabs.estimates";
	public static final String I18N_COEFFICIENTS_TAB = "ContractCardComponent.tabs.coefficients";
	
	private CssLayout contractFieldsLayout;
	private Binder<IContract> contractFieldsBinder;
	private Binder<ILocalEstimate> localEstimateFieldsBinder;
	
	private TextField contractCodeField;
	private TextField executorNameField;
	private TextField customerNameField;
	private ComboBox<ContractType> contractTypeField;
	private ComboBox<ICProject> projectField;
	private DateField signingDateField;
	private DateField startDateFiled;
	private DateField endDateField;
	
	private CssLayout viewAttributesLayout;
	
	private TabSheet tabs;
	private VerticalLayout milestonesLayout;
	private VerticalLayout estimatesLayout;
	private VerticalLayout coefficientsLayout;
	
	private MilestonesTreeGridComponent milestonesTreeGrid;
	private LocalEstimatesTreeGridComponent localEstimatesTreeGrid;
	private CoefficientsTreeGridComponent coefficientsTreeGrid;

	private IContractCardService contractCardService;

	
	public ContractCardComponent(IContractCardService service, Long contractCardId, String taskId) {
		super(service, contractCardId, taskId);
		createEventListeners();
	}

	private void createEventListeners() {
		addOnSaveListener(event -> {
			getUI().addWindow((new ConfirmDialogWindow(getI18nText(ConfirmDialogWindow.I18N_CONFIRM_MESSAGE, messageSource),
					getI18nText(ConfirmDialogWindow.I18N_SAVE_CONTRACT_CARD, messageSource), 
					getI18nText(ConfirmDialogWindow.I18N_YES, messageSource), 
					getI18nText(ConfirmDialogWindow.I18N_CANCEL, messageSource),
					confirmClickEvent -> {
						(new Thread(() -> processService.completeTask(taskId))).start();
						Notification notification = new Notification(getI18nText(I18N_SAVED_NOTIFICATION_CAP, messageSource),
								getI18nText(I18N_SAVED_NOTIFICATION_DES, messageSource),
								Type.TRAY_NOTIFICATION);
						notification.setDelayMsec(8000);
						notification.show(getUI().getPage());
					})));
		});
	}

	@Override
	public VerticalLayout createBodyLayout() {
		VerticalLayout body = new VerticalLayout();
		body.setSizeFull();
		body.addComponent(createContractCardFileds());
		body.addComponent(createTabs());
		return body;
	}

	public Component createContractCardFileds() {
		Panel panel = new Panel(getI18nText(I18N_CONTRACTCARDFILEDS_CAP, messageSource));
		panel.setSizeFull();
		contractFieldsLayout = createResponsiveLayout();
		
		panel.setContent(contractFieldsLayout);
		
		contractFieldsBinder = new Binder<>();
		
		contractFieldsLayout.addComponent(
				wrapField(getI18nText(I18N_CONTRACT_CODE_FIELD, messageSource), 
						createContractCodeField()));
		contractFieldsLayout.addComponent(
				wrapField(getI18nText(I18N_EXECUTOR_NAME_FIELD, messageSource), 
						createExecutorNameField()));
		contractFieldsLayout.addComponent(
				wrapField(getI18nText(I18N_CUSTOMER_NAME_FIELD, messageSource), 
						createCustomerNameField()));
		contractFieldsLayout.addComponent(
				wrapField(getI18nText(I18N_CONTRACTTYPE_FIELD, messageSource), 
						createContractTypeField()));
		contractFieldsLayout.addComponent(
				wrapField(getI18nText(I18N_PROJECT_FIELD, messageSource), 
						createProjectField()));
		contractFieldsLayout.addComponent(
				wrapField(getI18nText(I18N_SIGNINGDATE_FIELD, messageSource), 
						createSigningDateField()));
		contractFieldsLayout.addComponent(
				wrapField(getI18nText(I18N_STARTDATE_FIELD, messageSource), 
						createStartDateField()));
		contractFieldsLayout.addComponent(
				wrapField(getI18nText(I18N_ENDDATE_FIELD, messageSource), 
						createEndDateField()));
		
		IContractCardService service = (IContractCardService)this.service;
		
		Optional<IContract> contract = service.getContract(contractCardId);
		if (contract.isPresent()) {
			contractFieldsBinder.readBean(contract.get());
		}
		return panel;
	}

	public TextField createContractCodeField() {
		contractCodeField = new TextField();
		contractFieldsBinder.forField(contractCodeField)
				.bind(IContract::getCode, IContract::setCode);
		return contractCodeField;
	}

	public TextField createExecutorNameField() {
		executorNameField = new TextField();
		contractFieldsBinder.forField(executorNameField)
				.bind(IContract::getExecutorName, IContract::setExecutorName);
		return executorNameField;
	}

	public TextField createCustomerNameField() {
		customerNameField = new TextField();
		contractFieldsBinder.forField(customerNameField)
				.bind(IContract::getCustomerName, IContract::setCustomerName);
		return customerNameField;
	}

	public ComboBox<ContractType> createContractTypeField() {
		contractTypeField = new ComboBox<>("", ContractType.getAll());
		contractFieldsBinder.forField(contractTypeField)
				.bind(IContract::getContractType, IContract::setContractType);
		return contractTypeField;
	}

	public ComboBox<ICProject> createProjectField() {
		projectField = new ComboBox<>("", ((IContractCardService)service).getProjectService()
				.getCPRepository().findAll().stream()
					.map(item -> (ICProject)item)
						.collect(Collectors.toList()));
		contractFieldsBinder.forField(projectField)
				.bind(IContract::getProject, IContract::setProject);
		return projectField;
	}
	
	public AbstractComponent createSigningDateField() {
		signingDateField = new DateField();
		contractFieldsBinder.forField(signingDateField)
				.bind(IContract::getSigningDate, IContract::setSigningDate);
		return signingDateField;
	}
	
	public AbstractComponent createStartDateField() {
		startDateFiled= new DateField();
		contractFieldsBinder.forField(startDateFiled)
				.bind(IContract::getStartDate, null);
		return startDateFiled;
	}
	
	private AbstractComponent createEndDateField() {
		endDateField = new DateField();
		contractFieldsBinder.forField(endDateField)
				.bind(IContract::getEndDate, null);
		return endDateField;
	}
	
	public HorizontalLayout wrapField(String label, AbstractComponent field) {
		HorizontalLayout layout = new HorizontalLayout();
		field.setCaption(label);
		layout.setMargin(false);
		layout.addComponent(field);
		return layout;
	}

	private CssLayout createResponsiveLayout() {
		return new CssLayout() {
			@Override
			protected String getCss(Component c) {
				return "margin: 10px";
			}
		};
	}
	
	public Component createTabs() {
		tabs = new TabSheet();
		tabs.addTab(createMilestones(), getI18nText(I18N_MILESTONES_TAB, messageSource));
		tabs.addTab(createEstimates(), getI18nText(I18N_ESTIMATES_TAB, messageSource));
		tabs.addTab(createCoefficients(), getI18nText(I18N_COEFFICIENTS_TAB, messageSource));
		return tabs;
	}

	private Component createMilestones() {
		return createMilestonesGrid();
	}

	private Component createEstimates() {
		return createEstimatesGrid();
	}

	private Component createCoefficients() {
		return createCoefficientsGrid();
	}

	public Component createMilestonesGrid() {
		return null;
	}
	
	public Component refreshMilestonesGrid() {
		return null;
	}
	
	public Component createEstimatesGrid() {
		estimatesLayout = new VerticalLayout();
		estimatesLayout.setMargin(false);
		estimatesLayout.setSpacing(false);
		refreshLocalEstimatesGrid();
		return estimatesLayout;
	}
	
	public void refreshLocalEstimatesGrid() {
		estimatesLayout.removeAllComponents();
		contractCardService = (IContractCardService)this.service;
		localEstimatesTreeGrid = new LocalEstimatesTreeGridComponent(contractCardService.getProjectService(), 
																	 contractCardService.getLocalEstimateService(), 
																	 contractCardService.getUserSettingsService());
		localEstimatesTreeGrid.addOnGridItemSelect(itemSelect -> {
			//NodeWrapper parentNode = new NodeWrapper(LocalEstimate.class.getSimpleName());
		
		});
		NodeWrapper parentNode = new NodeWrapper(EstimateCalculation.class.getSimpleName());
		//contractFieldsBinder.getBean().getProject().get
		parentNode.setId(this.contractCardId);
		localEstimatesTreeGrid.getGridDataProvider().setParentNode(parentNode);
		estimatesLayout.addComponent(localEstimatesTreeGrid);
	}
	
	public Component createCoefficientsGrid() {
		return null;
	}
	
	public Component refreshCoefficientsGrid() {
		return null;
	}
}
