package ru.gzpn.spc.csl.services.bl.interfaces;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.domain.Sort.Order;

import com.vaadin.data.provider.QuerySortOrder;

import ru.gzpn.spc.csl.model.interfaces.IWorkSet;
import ru.gzpn.spc.csl.model.presenters.interfaces.IWorkSetPresenter;
import ru.gzpn.spc.csl.model.utils.NodeWrapper;

public interface IWorkSetService extends IDataService<IWorkSet, IWorkSetPresenter> {
	public Stream<IWorkSet> getAllItems(List<Order> sortOrders, int offset, int limit);
	/**
	 * Get items by selected NodeWrapper. It might be a Stage, CProject or PlanObject
	 */
	public Stream<IWorkSet> getItemsByNode(NodeWrapper node, int offset, int limit);
	public Comparator<IWorkSetPresenter> getSortComparator(List<QuerySortOrder> list);
	void save(IWorkSet bean, NodeWrapper parentNode);
}
