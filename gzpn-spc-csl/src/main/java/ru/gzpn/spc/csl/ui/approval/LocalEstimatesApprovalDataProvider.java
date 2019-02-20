package ru.gzpn.spc.csl.ui.approval;

import java.util.stream.Stream;

import com.vaadin.data.provider.Query;

import ru.gzpn.spc.csl.model.dataproviders.LocalEstimateDataProvider;
import ru.gzpn.spc.csl.model.presenters.interfaces.ILocalEstimatePresenter;
import ru.gzpn.spc.csl.services.bl.interfaces.ILocalEstimateService;

@SuppressWarnings("serial")
public class LocalEstimatesApprovalDataProvider extends LocalEstimateDataProvider {

	public LocalEstimatesApprovalDataProvider(ILocalEstimateService localEstimateService) {
		super(localEstimateService);
	}

	@Override
	protected Stream<ILocalEstimatePresenter> fetchFromBackEnd(Query<ILocalEstimatePresenter, Void> query) {
		// TODO Auto-generated method stub
		return super.fetchFromBackEnd(query);
	}

	@Override
	protected int sizeInBackEnd(Query<ILocalEstimatePresenter, Void> query) {
		// TODO Auto-generated method stub
		return super.sizeInBackEnd(query);
	}

}
