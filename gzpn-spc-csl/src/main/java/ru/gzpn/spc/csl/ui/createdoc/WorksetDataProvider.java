package ru.gzpn.spc.csl.ui.createdoc;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.data.provider.AbstractBackEndDataProvider;
import com.vaadin.data.provider.Query;
import com.vaadin.data.provider.QuerySortOrder;

import ru.gzpn.spc.csl.model.WorkSet;
import ru.gzpn.spc.csl.services.bl.WorkSetService;
import ru.gzpn.spc.csl.ui.common.NodeFilter;

public class WorksetDataProvider extends AbstractBackEndDataProvider<WorkSet, Void> {
	private static final long serialVersionUID = 1L;
	public static final Logger logger = LogManager.getLogger(WorksetDataProvider.class);
	
	private WorkSetService service;
	private NodeFilter filter;
	/* The selected project item by which we are going to fetch WorkSets */
	private NodeWrapper parentNode;
	
	WorksetDataProvider(WorkSetService service) {
		this.service = service;
		this.filter = new NodeFilter("");
		this.parentNode = null;
	}

	@Override
	protected Stream<WorkSet> fetchFromBackEnd(Query<WorkSet, Void> query) {
		Stream<WorkSet> result = Stream.empty();
		if (!Objects.isNull(parentNode)) {
			List<QuerySortOrder> sortOrders = query.getSortOrders();
		}
		return result;
	}

	@Override
	protected int sizeInBackEnd(Query<WorkSet, Void> query) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public void setFilter(NodeFilter filter) {
		if (!Objects.isNull(filter)) {
			this.filter = filter;
		}
	}
	
	/**
	 * Use this setter in the UI to set the selected base item by which 
	 * the WorkSets will be fetched
	 * @param parent
	 */
	public void setParentCondition(NodeWrapper parent) {
		this.parentNode = parent;
	}
}
