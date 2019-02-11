package ru.gzpn.spc.csl.model.dataproviders;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.data.provider.AbstractBackEndDataProvider;
import com.vaadin.data.provider.Query;

import ru.gzpn.spc.csl.model.jsontypes.ColumnSettings;
import ru.gzpn.spc.csl.model.presenters.WorkSetPresenter;
import ru.gzpn.spc.csl.model.presenters.interfaces.IWorkSetPresenter;
import ru.gzpn.spc.csl.model.utils.NodeWrapper;
import ru.gzpn.spc.csl.services.bl.WorkSetService.WorkSetFilter;
import ru.gzpn.spc.csl.services.bl.interfaces.IWorkSetService;

public class WorksetDataProvider extends AbstractBackEndDataProvider<IWorkSetPresenter, Void> {
	private static final long serialVersionUID = 1L;
	public static final Logger logger = LogManager.getLogger(WorksetDataProvider.class);
	
	private IWorkSetService service;
	private WorkSetFilter filter;
	/* The selected project item by which we are going to fetch WorkSets */
	private NodeWrapper parentNode;
	private List<ColumnSettings> shownColumns;
	
	public WorksetDataProvider(IWorkSetService service) {
		this.service = service;
		this.filter = new WorkSetFilter();
		this.parentNode = null;
	}

	@Override
	protected Stream<IWorkSetPresenter> fetchFromBackEnd(Query<IWorkSetPresenter, Void> query) {
		Stream<IWorkSetPresenter> result = Stream.empty();
		if (!Objects.isNull(parentNode)) {
			result = service
						.getItemsByNode(parentNode, query.getOffset(), query.getLimit())
							.map(item -> (IWorkSetPresenter) new WorkSetPresenter(item))
								.filter(getFilter().filter(shownColumns))
									.sorted(service.getSortComparator(query.getSortOrders()));
		}
		return result;
	}

	@Override
	protected int sizeInBackEnd(Query<IWorkSetPresenter, Void> query) {
		long result = 0;
		if (!Objects.isNull(parentNode)) {
			result = service
						.getItemsByNode(parentNode, query.getOffset(), query.getLimit())
							.filter(getFilter().filter(shownColumns)).count();
		}

		return (int)result;
	}
	
	public WorkSetFilter getFilter() {
		return this.filter;
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
