package ru.gzpn.spc.csl.services.bl.interfaces;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import ru.gzpn.spc.csl.model.interfaces.IWorkSet;
import ru.gzpn.spc.csl.services.bl.WorkSetService.WorkSetFilter;
import ru.gzpn.spc.csl.ui.createdoc.NodeWrapper;

public interface IWorkSetService {
	public Order createSortOrder(String fieldName, Direction direction);
	public WorkSetFilter createWorkSetFilter();
	public Stream<IWorkSet> getAllItems(List<Order> sortOrders, int offset, int limit);
	public long getCountItemsByNode(NodeWrapper node);
	/**
	 * Get items by selected NodeWrapper. It might be a Stage, CProject or PlanObject
	 */
	public Stream<IWorkSet> getItemsByNode(NodeWrapper node, List<Order> sortOrders, int offset, int limit);
}
