package ru.gzpn.spc.csl.model.dataproviders;

import java.util.List;
import java.util.stream.Stream;

import com.vaadin.data.provider.Query;

import ru.gzpn.spc.csl.model.jsontypes.ColumnSettings;
import ru.gzpn.spc.csl.model.presenters.EstimateCalculationPresenter;
import ru.gzpn.spc.csl.model.presenters.interfaces.IEstimateCalculationPresenter;
import ru.gzpn.spc.csl.model.utils.NodeWrapper;
import ru.gzpn.spc.csl.services.bl.EstimateCalculationService.EstimateCalculationFilter;
import ru.gzpn.spc.csl.services.bl.interfaces.IEstimateCalculationService;
import ru.gzpn.spc.csl.ui.common.IGridFilter;

@SuppressWarnings("serial")
public class EstimateCalculationDataProvider extends AbstractRegistryDataProvider<IEstimateCalculationPresenter, Void> {

	private IEstimateCalculationService estimateCalculationService;
	private List<ColumnSettings> shownColumns;
	private IGridFilter<IEstimateCalculationPresenter> filter;
	
	public EstimateCalculationDataProvider(IEstimateCalculationService estimateCalculationService) {
		this.estimateCalculationService = estimateCalculationService;
	}
	
	@Override
	protected Stream<IEstimateCalculationPresenter> fetchFromBackEnd(Query<IEstimateCalculationPresenter, Void> query) {
		return estimateCalculationService.getEstimateCalculations().stream().map(
					item -> (IEstimateCalculationPresenter) new EstimateCalculationPresenter(item)
						).filter(getFilter().getFilterPredicate(shownColumns))
							.sorted(estimateCalculationService.getSortComparator(query.getSortOrders()));
	}

	@Override
	protected int sizeInBackEnd(Query<IEstimateCalculationPresenter, Void> query) {
		return (int)estimateCalculationService.getEstimateCalculations().stream().map(
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
	public IGridFilter<IEstimateCalculationPresenter> getFilter() {
		if (filter == null) {
			filter = new EstimateCalculationFilter();
		}
		return filter;
	}

	@Override
	public NodeWrapper getParentNode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParentNode(NodeWrapper node) {
		// TODO Auto-generated method stub
		
	}
}
