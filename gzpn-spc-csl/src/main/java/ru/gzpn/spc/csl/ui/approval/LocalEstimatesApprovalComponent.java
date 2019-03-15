package ru.gzpn.spc.csl.ui.approval;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

import ru.gzpn.spc.csl.model.interfaces.ILocalEstimate;
import ru.gzpn.spc.csl.services.bl.LocalEstimatesApprovalService;
import ru.gzpn.spc.csl.services.bl.interfaces.ILocalEstimatesApprovalService;
import ru.gzpn.spc.csl.services.bl.interfaces.IProcessService;
import ru.gzpn.spc.csl.services.bl.interfaces.IUIService;

@SuppressWarnings("serial")
public class LocalEstimatesApprovalComponent extends AbstractLocalEstimatesApprovalComponent {

	private LocalEstimatesApprovalGridComponent approvalGrid;
	private VerticalLayout estimatesLayout;
	
	public LocalEstimatesApprovalComponent(IUIService service, String taskId) {
		super(service, taskId);
	}

	@Override
	public VerticalLayout createBodyLayout() {
		VerticalLayout body = new VerticalLayout();
		body.setSizeFull();

		body.addComponent(createApprovalGrid());
		return body;
	}

	public Component createApprovalGrid() {
		estimatesLayout = new VerticalLayout();
		estimatesLayout.setMargin(false);
		estimatesLayout.setSpacing(false);
		refreshApprovalGrid();
		return estimatesLayout;
	}
	
	public void refreshApprovalGrid() {
		ILocalEstimatesApprovalService approvalService = (ILocalEstimatesApprovalService)this.service;
		estimatesLayout.removeAllComponents();
		approvalGrid = new LocalEstimatesApprovalGridComponent(null, 
																approvalService.getLocalEstimateService(), 
																approvalService.getUserSettingsService());
		((LocalEstimatesApprovalDataProvider) approvalGrid.getGridDataProvider()).getIds().clear();
		((LocalEstimatesApprovalDataProvider) approvalGrid.getGridDataProvider()).getIds().addAll(getLocalEstimatesIds());
		estimatesLayout.addComponent(approvalGrid);
	}

	private Collection<? extends Long> getLocalEstimatesIds() {
		Long ssrId = (Long)service.getProcessService().getProcessVariableByTaskId(taskId, IProcessService.SSR_ID);
		List<ILocalEstimate> estimates = ((LocalEstimatesApprovalService)service).getLocalEstimateService()
											.getLocalEstimatesByCalculationId(ssrId);
		return estimates.stream().map(item -> item.getId()).collect(Collectors.toList());
	}
}
