package ru.gzpn.spc.csl.ui.estimatereg;

import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import org.springframework.context.i18n.LocaleContextHolder;

import com.vaadin.data.provider.AbstractBackEndDataProvider;
import com.vaadin.data.provider.Query;

import ru.gzpn.spc.csl.model.jsontypes.ColumnSettings;
import ru.gzpn.spc.csl.services.bl.EstimateCalculationService.EstimateCalculationFilter;
import ru.gzpn.spc.csl.services.bl.interfaces.IEstimateCalculationService;

@SuppressWarnings("serial")
public class EstimateCalculationDataProvider extends AbstractBackEndDataProvider<IEstimateCalculationPresenter, Void> {

	private IEstimateCalculationService estimateCalculationService;
	private EstimateCalculationFilter filter;
	private List<ColumnSettings> shownColumns;
	private Locale locale;
	
	public EstimateCalculationDataProvider(IEstimateCalculationService estimateCalculationService) {
		this.estimateCalculationService = estimateCalculationService;
		this.filter = new EstimateCalculationFilter();
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
	
	public EstimateCalculationFilter getFilter() {
		return filter;
	}

	public void setFilter(EstimateCalculationFilter filter) {
		this.filter = filter;
	}

	public List<ColumnSettings> getShownColumns() {
		return shownColumns;
	}

	public void setShownColumns(List<ColumnSettings> shownColumns) {
		this.shownColumns = shownColumns;
	}
}
