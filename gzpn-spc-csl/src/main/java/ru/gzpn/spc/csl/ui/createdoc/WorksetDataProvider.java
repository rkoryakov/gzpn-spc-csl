package ru.gzpn.spc.csl.ui.createdoc;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import com.vaadin.data.provider.AbstractBackEndDataProvider;
import com.vaadin.data.provider.Query;
import com.vaadin.data.provider.QuerySortOrder;
import com.vaadin.shared.data.sort.SortDirection;

import ru.gzpn.spc.csl.model.interfaces.IWorkSet;
import ru.gzpn.spc.csl.model.jsontypes.ColumnSettings;
import ru.gzpn.spc.csl.services.bl.WorkSetService.WorkSetFilter;
import ru.gzpn.spc.csl.services.bl.interfaces.IWorkSetService;

public class WorksetDataProvider extends AbstractBackEndDataProvider<IWorkSet, Void> {
	private static final long serialVersionUID = 1L;
	public static final Logger logger = LogManager.getLogger(WorksetDataProvider.class);
	
	private IWorkSetService service;
	private WorkSetFilter filter;
	/* The selected project item by which we are going to fetch WorkSets */
	private NodeWrapper parentNode;
	List<ColumnSettings> shownColumns;
	
	WorksetDataProvider(IWorkSetService service) {
		this.service = service;
		this.filter = service.createWorkSetFilter();
		this.parentNode = null;
	}

	@Override
	protected Stream<IWorkSet> fetchFromBackEnd(Query<IWorkSet, Void> query) {
		Stream<IWorkSet> result = Stream.empty();
		if (!Objects.isNull(parentNode)) {
			List<Order> worksetSorts = new ArrayList<>();
			for (QuerySortOrder order : query.getSortOrders()) {
				Direction direction = order.getDirection() == SortDirection.DESCENDING ?
						Direction.DESC : Direction.ASC;
				Order worksetSort = service.createSortOrder(order.getSorted(), direction);
				worksetSorts.add(worksetSort);
			}
			result = service
						.getItems(worksetSorts, query.getOffset(), query.getLimit())
							.filter(getFilter().filter(shownColumns));
		}
		return result;
	}

	@Override
	protected int sizeInBackEnd(Query<IWorkSet, Void> query) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public WorkSetFilter getFilter() {
		return this.filter;
	}
	
	/**
	 * Use this setter in the UI to set the selected base item by which 
	 * the WorkSets will be fetched
	 * @param parent
	 */
	public void setParentCondition(NodeWrapper parent) {
		this.parentNode = parent;
	}

	public List<ColumnSettings> getShownColumns() {
		return shownColumns;
	}

	public void setShownColumns(List<ColumnSettings> shownColumns) {
		this.shownColumns = shownColumns;
	}
	
	
}
