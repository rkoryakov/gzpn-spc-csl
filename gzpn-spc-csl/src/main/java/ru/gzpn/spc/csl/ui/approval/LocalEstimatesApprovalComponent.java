package ru.gzpn.spc.csl.ui.approval;

import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

import ru.gzpn.spc.csl.services.bl.interfaces.ILocalEstimatesApprovalService;
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
		
		estimatesLayout.addComponent(approvalGrid);
	}
}
