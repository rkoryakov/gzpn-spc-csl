package ru.gzpn.spc.csl.model.dataproviders;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;

import com.vaadin.data.provider.Query;

import ru.gzpn.spc.csl.model.interfaces.IEstimateCost;
import ru.gzpn.spc.csl.model.jsontypes.ColumnSettings;
import ru.gzpn.spc.csl.model.utils.NodeWrapper;
import ru.gzpn.spc.csl.services.bl.EstimateCostServie.EstimateCostFilter;
import ru.gzpn.spc.csl.services.bl.interfaces.IEstimateCostService;
import ru.gzpn.spc.csl.ui.common.IGridFilter;

@SuppressWarnings("serial")
public class EstimateCostDataProvider extends AbstractRegistryDataProvider<IEstimateCost, Void> {
	public Logger logger = LogManager.getLogger(EstimateCostDataProvider.class);
	protected IEstimateCostService estimateCostService;
	protected List<ColumnSettings> shownColumns;
	protected IGridFilter<IEstimateCost> filter;

	public EstimateCostDataProvider(IEstimateCostService estimateCostService) {
		this.estimateCostService = estimateCostService;
	}
	
	@Override
	public IGridFilter<IEstimateCost> getFilter() {
		if (filter == null) {
			filter = new EstimateCostFilter();
		}
		return filter;
	}

	@Override
	public NodeWrapper getParentNode() {
		return this.parentNode;
	}

	@Override
	public void setParentNode(NodeWrapper node) {
		this.parentNode = node;
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
	protected Stream<IEstimateCost> fetchFromBackEnd(Query<IEstimateCost, Void> query) {
		Stream<IEstimateCost> result = Stream.empty();
		
		if (parentNode != null) {
			result = fetchByParentNode(query);
		} else {

		}
		
		return result;
	}

	@Override
	protected int sizeInBackEnd(Query<IEstimateCost, Void> query) {
		Stream<IEstimateCost> result = Stream.empty();
		
		if (parentNode != null) {
			result = fetchByParentNode(query);
		} else {
			
		}
		
		return (int)result.count();
	}

	
	protected Stream<IEstimateCost> fetchByParentNode(Query<IEstimateCost, Void> query) {
		Stream<IEstimateCost> result = Stream.empty();
		
		switch (parentNode.getEntityEnum()) {
		case LOCALESTIMATE:
			result = fetchByLocalEstimate(query, parentNode);
			break;
		default:
			break;
		}
		
		return result;
	}
	
	protected Stream<IEstimateCost> fetchByLocalEstimate(Query<IEstimateCost, Void> query, NodeWrapper parentNode) {
		Stream<IEstimateCost> result = Stream.empty();
		if (parentNode.getId() != null) {
			result = estimateCostService.getEstimateCostsByLocal(parentNode.getId())
							.stream()
								.filter(getFilter().getFilterPredicate(shownColumns))
									.sorted((Comparator<IEstimateCost>)estimateCostService.getSortComparator(query.getSortOrders()));
		}
		
		return result;
	}


	public interface PageCommand<T> {
		Page<T> findPages(int offset, int limit);
	}
	
}