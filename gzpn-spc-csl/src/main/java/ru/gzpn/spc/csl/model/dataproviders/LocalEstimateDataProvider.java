package ru.gzpn.spc.csl.model.dataproviders;

import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import org.springframework.context.i18n.LocaleContextHolder;

import com.vaadin.data.provider.Query;

import ru.gzpn.spc.csl.model.jsontypes.ColumnSettings;
import ru.gzpn.spc.csl.model.presenters.LocalEstimatePresenter;
import ru.gzpn.spc.csl.model.presenters.interfaces.ILocalEstimatePresenter;
import ru.gzpn.spc.csl.services.bl.LocalEstimateService.LocalEstimateFilter;
import ru.gzpn.spc.csl.services.bl.interfaces.ILocalEstimateService;
import ru.gzpn.spc.csl.ui.common.IRegisterFilter;

@SuppressWarnings("serial")
public class LocalEstimateDataProvider extends AbstractRegistryDataProvider<ILocalEstimatePresenter, Void> {

	private ILocalEstimateService localEstimateService;
	private List<ColumnSettings> shownColumns;
	private Locale locale;
	private IRegisterFilter filter;


	public LocalEstimateDataProvider(ILocalEstimateService localEstimateService) {
		this.localEstimateService = localEstimateService;
		this.locale = LocaleContextHolder.getLocale();
	}

	@Override
	protected Stream<ILocalEstimatePresenter> fetchFromBackEnd(Query<ILocalEstimatePresenter, Void> query) {
		return localEstimateService.getLocalEstimates().stream().map(
				item -> (ILocalEstimatePresenter) new LocalEstimatePresenter(item)
					).filter(getFilter().getFilterPredicate(shownColumns))
						.sorted(localEstimateService.getSortComparator(query.getSortOrders()));
	}

	@Override
	protected int sizeInBackEnd(Query<ILocalEstimatePresenter, Void> query) {
		return (int)localEstimateService.getLocalEstimates().stream().map(
				item -> (ILocalEstimatePresenter) new LocalEstimatePresenter(item)
					).filter(getFilter().getFilterPredicate(shownColumns)).count();
	}

	@Override
	public List<ColumnSettings> getShownColumns() {
		return shownColumns;
	}

	@Override
	public void setShownColumns(List<ColumnSettings> shownColumns) {
		this.shownColumns = shownColumns;
	}
	
	@Override
	public IRegisterFilter getFilter() {
		if (filter == null) {
			filter = new LocalEstimateFilter();
		}
		return filter;
	}
}
