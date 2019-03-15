package ru.gzpn.spc.csl.ui.approval;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.vaadin.data.provider.Query;

import ru.gzpn.spc.csl.model.dataproviders.LocalEstimateDataProvider;
import ru.gzpn.spc.csl.model.presenters.LocalEstimatePresenter;
import ru.gzpn.spc.csl.model.presenters.interfaces.ILocalEstimatePresenter;
import ru.gzpn.spc.csl.services.bl.interfaces.ILocalEstimateService;

@SuppressWarnings("serial")
public class LocalEstimatesApprovalDataProvider extends LocalEstimateDataProvider {
	private List<Long> ids = new ArrayList<>();
	
	public LocalEstimatesApprovalDataProvider(ILocalEstimateService localEstimateService) {
		super(localEstimateService);
	}

	@Override
	protected Stream<ILocalEstimatePresenter> fetchFromBackEnd(Query<ILocalEstimatePresenter, Void> query) {
		Stream<ILocalEstimatePresenter> result = Stream.empty();
		result = localEstimateService.getLocalEstimatesByIds(getIds())
					.stream().map(item -> new LocalEstimatePresenter(item));
		return result;
	}

	@Override
	protected int sizeInBackEnd(Query<ILocalEstimatePresenter, Void> query) {
		return (int)fetchFromBackEnd(query).count();
	}

	public List<Long> getIds() {
		return ids;
	}
}
