package ru.gzpn.spc.csl.ui.approval;

import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;

import ru.gzpn.spc.csl.services.bl.interfaces.IUIService;
import ru.gzpn.spc.csl.ui.common.AbstractRegisterDataProvider;

public class LocalEstimatesApprovalComponent extends AbstractLocalEstimatesApprovalComponent {

	public LocalEstimatesApprovalComponent(IUIService service) {
		super(service);
		// TODO Auto-generated constructor stub
	}

	@Override
	public <T, F> AbstractRegisterDataProvider<T, F> getDataProvider() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> Grid<T> getLocalEstimatesGrid() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VerticalLayout createBodyLayout() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void refreshUiElements() {
		// TODO Auto-generated method stub

	}

}
