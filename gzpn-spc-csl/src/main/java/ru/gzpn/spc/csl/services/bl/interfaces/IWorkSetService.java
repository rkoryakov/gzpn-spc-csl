package ru.gzpn.spc.csl.services.bl.interfaces;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import ru.gzpn.spc.csl.model.interfaces.IWorkSet;
import ru.gzpn.spc.csl.services.bl.WorkSetService.WorkSetFilter;

public interface IWorkSetService {
	public Order createSortOrder(String fieldName, Direction direction);
	public WorkSetFilter createWorkSetFilter();
	public Stream<IWorkSet> getItems(List<Order> sortOrders, int offset, int limit);
	public Stream<IWorkSet> getItems(Long planObjId, List<Order> sortOrders, int offset, int limit);
}
