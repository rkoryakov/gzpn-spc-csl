package ru.gzpn.spc.csl.model.dataproviders;

import java.util.List;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;

import com.vaadin.data.provider.Query;

import ru.gzpn.spc.csl.model.jsontypes.ColumnSettings;
import ru.gzpn.spc.csl.model.presenters.LocalEstimatePresenter;
import ru.gzpn.spc.csl.model.presenters.interfaces.IEstimateCost;
import ru.gzpn.spc.csl.model.utils.NodeWrapper;
import ru.gzpn.spc.csl.services.bl.LocalEstimateService.LocalEstimateFilter;
import ru.gzpn.spc.csl.services.bl.interfaces.ILocalEstimateService;
import ru.gzpn.spc.csl.ui.common.IGridFilter;

@SuppressWarnings("serial")
public class LocalEstimateDataProvider extends AbstractRegistryDataProvider<IEstimateCost, Void> {

	public Logger logger = LogManager.getLogger(LocalEstimateDataProvider.class);
	protected ILocalEstimateService localEstimateService;
	protected List<ColumnSettings> shownColumns;
	protected IGridFilter<IEstimateCost> filter;
	
	//First Request with page and limit = 1 
    //long totalElements = pageCommand.findPages(1, 1).getTotalElements();

	public LocalEstimateDataProvider(ILocalEstimateService localEstimateService) {
		this.localEstimateService = localEstimateService;
	}

	@Override
	protected Stream<IEstimateCost> fetchFromBackEnd(Query<IEstimateCost, Void> query) {
		Stream<IEstimateCost> result = Stream.empty();
		
		if (parentNode != null) {
			result = fetchByParentNode(query);
		} else {
			result = localEstimateService.getLocalEstimates().stream()
						.map(item -> (IEstimateCost) new LocalEstimatePresenter(item))
							.filter(getFilter().getFilterPredicate(shownColumns))
								.sorted(localEstimateService.getSortComparator(query.getSortOrders()));
		}
		
		return result;
	}

	@Override
	protected int sizeInBackEnd(Query<IEstimateCost, Void> query) {
		Stream<IEstimateCost> result = Stream.empty();
		
		if (parentNode != null) {
			result = fetchByParentNode(query);
		} else {
			result = localEstimateService.getLocalEstimates().stream()
						.map(item -> (IEstimateCost) new LocalEstimatePresenter(item))
							.filter(getFilter().getFilterPredicate(shownColumns));
		}
		
		return (int)result.count();
	}

	
	protected Stream<IEstimateCost> fetchByParentNode(Query<IEstimateCost, Void> query) {
		Stream<IEstimateCost> result = Stream.empty();
		
		switch (parentNode.getEntityEnum()) {
		case ESTIMATECALCULATION:
			result = fetchByEstimateCalculationId(query, parentNode);
			break;
		default:
			break;
		}
		
		return result;
	}
	
	protected Stream<IEstimateCost> fetchByEstimateCalculationId(Query<IEstimateCost, Void> query, NodeWrapper parentNode) {
		Stream<IEstimateCost> result = Stream.empty();
		if (parentNode.getId() != null) {
			result = localEstimateService.getLocalEstimatesByCalculationId(parentNode.getId())
							.stream().map(item -> (IEstimateCost) new LocalEstimatePresenter(item))
								.filter(getFilter().getFilterPredicate(shownColumns))
									.sorted(localEstimateService.getSortComparator(query.getSortOrders()));
		}
		
		return result;
	}


	public interface PageCommand<T> {
		Page<T> findPages(int offset, int limit);
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
	public IGridFilter<IEstimateCost> getFilter() {
		if (filter == null) {
			filter = new LocalEstimateFilter();
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
}
