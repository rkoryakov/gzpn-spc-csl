package ru.gzpn.spc.csl.ui.views;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewBeforeLeaveEvent;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import ru.gzpn.spc.csl.services.bl.interfaces.IContractCardService;
import ru.gzpn.spc.csl.ui.common.ConfirmDialogWindow;
import ru.gzpn.spc.csl.ui.contract.ContractCardComponent;

@SuppressWarnings("serial")
@SpringView(name = ContractView.NAME)
@UIScope
public class ContractView extends VerticalLayout implements View {
	public static final String NAME = "contractView";
	public static final String REQUEST_PARAM_TASKID = "taskId";
	public static final Logger logger = LogManager.getLogger(ContractView.class);

	@Autowired
	private IContractCardService service;
	private String taskId;

	public ContractView() {
		setMargin(false);
		setSpacing(false);
	}

	@PostConstruct
	void init() {
	}


	@Override
	public void enter(ViewChangeEvent event) {
		if (event.getParameters() != null) {
			String contractId = event.getParameterMap().get("id");
			String taskId = event.getParameterMap().get(REQUEST_PARAM_TASKID);
			logger.debug(event.getParameters());
			logger.debug(event.getParameterMap());
			
			Long id = null;

			if (contractId != null && StringUtils.isNumeric(contractId)) {
				id = Long.parseLong(contractId);
			}
			ContractCardComponent layout = new ContractCardComponent(service, id, taskId);
			addComponent(layout);			
		}
	}


	public void createFooter() {
		HorizontalLayout footer = createFooterLayout();
		this.addComponent(footer);
	}

	public HorizontalLayout createFooterLayout() {
		HorizontalLayout footerLayout = new HorizontalLayout();
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		footerLayout.setSizeFull();
		footerLayout.setMargin(true);
		footerLayout.setSpacing(true);
		footerLayout.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);
		horizontalLayout.addComponent(createContractButton());
		horizontalLayout.addComponent(createCalculateButton());

		footerLayout.addComponent(horizontalLayout);
		
		return footerLayout;
	}

	public Component createContractButton() {
		Button contractButton = new Button("Сформировать карточку договора");
		contractButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		contractButton.setEnabled(true);
		contractButton.addClickListener(listener -> {
			getUI().addWindow((new ConfirmDialogWindow("Подтвердите операцию",
					"Карточка договора сформирована корректно?", "Да", "Отмена",
					confirmClickEvent -> {
						(new Thread() {
							@Override
							public void run() {
//								Map<String, Object> processVariables = new HashMap<>();
//								processVariables.put(IProcessService.INITIATOR, currentUser);
//								processVariables.put(IProcessService.DOCUMENTS, documentsGrid.getSelectedItems());
//								processVariables.put(IProcessService.COMMENTS, descriptionField.getValue());
								
								service.getProcessService().completeTask(taskId);
							}
						}).start();
						Notification notification = new Notification("Создана карточка договора",
								"Процесс регистрации смет переходит к сдедующему шагу",
								Type.TRAY_NOTIFICATION);
						notification.setDelayMsec(8000);
						notification.show(getUI().getPage());
					})));
		});
		return contractButton;
	}

	public Component createCalculateButton() {
		Button calculateButton = new Button("Расчет НМЦ");
		calculateButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
		calculateButton.addClickListener(listener -> {
			getUI().addWindow((new ConfirmDialogWindow("Подтвердите операцию",
					"Расчет НМЦ выполнен корректно?", "Да", "Отмена",
					confirmClickEvent -> {
						(new Thread() {
							@Override
							public void run() {
//								Map<String, Object> processVariables = new HashMap<>();
//								processVariables.put(IProcessService.INITIATOR, currentUser);
//								processVariables.put(IProcessService.DOCUMENTS, documentsGrid.getSelectedItems());
//								processVariables.put(IProcessService.COMMENTS, descriptionField.getValue());
								
								service.getProcessService().completeTask(taskId);
							}
						}).start();
						Notification notification = new Notification("Выполнен расчет НМЦ",
								"Процесс регистрации смет завершен",
								Type.TRAY_NOTIFICATION);
						notification.setDelayMsec(8000);
						notification.show(getUI().getPage());
					})));
		});
		return calculateButton;
	}

	
	@Override
	public void beforeLeave(ViewBeforeLeaveEvent event) {
		View.super.beforeLeave(event);
	}

	@Override
	public Component getViewComponent() {
		return View.super.getViewComponent();
	}
}
