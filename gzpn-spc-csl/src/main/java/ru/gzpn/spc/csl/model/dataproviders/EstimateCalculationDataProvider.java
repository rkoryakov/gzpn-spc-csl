package ru.gzpn.spc.csl.model.dataproviders;

import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import org.springframework.context.i18n.LocaleContextHolder;

import com.vaadin.data.provider.Query;

import ru.gzpn.spc.csl.model.jsontypes.ColumnSettings;
import ru.gzpn.spc.csl.model.presenters.EstimateCalculationPresenter;
import ru.gzpn.spc.csl.model.presenters.interfaces.IEstimateCalculationPresenter;
import ru.gzpn.spc.csl.services.bl.EstimateCalculationService.EstimateCalculationFilter;
import ru.gzpn.spc.csl.services.bl.interfaces.IEstimateCalculationService;
import ru.gzpn.spc.csl.ui.common.IGridFilter;

@SuppressWarnings("serial")
public class EstimateCalculationDataProvider extends AbstractRegistryDataProvider<IEstimateCalculationPresenter, Void> {

	private IEstimateCalculationService estimateCalculationService;
	private List<ColumnSettings> shownColumns;
	private Locale locale;
	private IGridFilter filter;
	
	public EstimateCalculationDataProvider(IEstimateCalculationService estimateCalculationService) {
		this.estimateCalculationService = estimateCalculationService;
		this.locale = LocaleContextHolder.getLocale();
	}
	
	@Override
	protected Stream<IEstimateCalculationPresenter> fetchFromBackEnd(Query<IEstimateCalculationPresenter, Void> query) {
		return estimateCalculationService.getEstimateCalculation().stream().map(
					item -> (IEstimateCalculationPresenter) new EstimateCalculationPresenter(item)
						).filter(getFilter().getFilterPredicate(shownColumns))
							.sorted(estimateCalculationService.getSortComparator(query.getSortOrders()));
	}

	@Override
	protected int sizeInBackEnd(Query<IEstimateCalculationPresenter, Void> query) {
		return (int)estimateCalculationService.getEstimateCalculation().stream().map(
					item -> (IEstimateCalculationPresenter) new EstimateCalculationPresenter(item)
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
	public IGridFilter getFilter() {
		if (filter == null) {
			filter = new EstimateCalculationFilter();
		}
		return filter;
	}
}