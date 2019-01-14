package ru.gzpn.spc.csl.ui.createdoc;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.data.provider.AbstractBackEndDataProvider;
import com.vaadin.data.provider.Query;
import com.vaadin.data.provider.QuerySortOrder;

import ru.gzpn.spc.csl.model.interfaces.IWorkSet;
import ru.gzpn.spc.csl.model.jsontypes.ColumnSettings;
import ru.gzpn.spc.csl.model.utils.NodeWrapper;
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
			result = service
						.getItemsByNode(parentNode, query.getOffset(), query.getLimit())
							.filter(getFilter().filter(shownColumns)).sorted(getSort(query.getSortOrders()));
		}
		return result;
	}

	@Override
	protected int sizeInBackEnd(Query<IWorkSet, Void> query) {
		long result = 0;
		if (!Objects.isNull(parentNode)) {
			result = service
						.getItemsByNode(parentNode, query.getOffset(), query.getLimit())
							.filter(getFilter().filter(shownColumns)).count();
		}
		logger.debug("[fetchFromBackEnd] result = {}", result);
		return (int)result;
	}
	
	public WorkSetFilter getFilter() {
		return this.filter;
	}
	
	public Comparator<IWorkSet> getSort(List<QuerySortOrder> orders) {
		return service.sort(orders);
	}
	/**
	 * Use this setter in the UI to set the selected base item by which 
	 * the WorkSets will be fetched
	 * @param parent
	 */
	public void setParentNode(NodeWrapper parent) {
		this.parentNode = parent;
	}

	public NodeWrapper getParentNode() {
		return this.parentNode;
	}
	
	public List<ColumnSettings> getShownColumns() {
		return shownColumns;
	}

	public void setShownColumns(List<ColumnSettings> shownColumns) {
		this.shownColumns = shownColumns;
	}
	
	
}
