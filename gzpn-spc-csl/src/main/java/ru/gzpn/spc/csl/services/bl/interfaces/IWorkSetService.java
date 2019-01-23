package ru.gzpn.spc.csl.services.bl.interfaces;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import com.vaadin.data.provider.QuerySortOrder;

import ru.gzpn.spc.csl.model.interfaces.IWorkSet;
import ru.gzpn.spc.csl.model.utils.NodeWrapper;
import ru.gzpn.spc.csl.services.bl.WorkSetService.WorkSetFilter;

public interface IWorkSetService {
	public Order createSortOrder(String fieldName, Direction direction);
	public WorkSetFilter createWorkSetFilter();
	public Stream<IWorkSet> getAllItems(List<Order> sortOrders, int offset, int limit);
	/**
	 * Get items by selected NodeWrapper. It might be a Stage, CProject or PlanObject
	 */
	public Stream<IWorkSet> getItemsByNode(NodeWrapper node, int offset, int limit);
	public Comparator<IWorkSet> getSortComparator(List<QuerySortOrder> list);
}
