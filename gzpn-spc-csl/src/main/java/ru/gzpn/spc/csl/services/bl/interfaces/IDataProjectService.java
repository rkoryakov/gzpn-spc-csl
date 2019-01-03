package ru.gzpn.spc.csl.services.bl.interfaces;

import java.util.stream.Stream;

import ru.gzpn.spc.csl.model.repositories.HProjectRepository;
import ru.gzpn.spc.csl.ui.createdoc.NodeWrapper;

public interface IDataProjectService {
	public HProjectRepository getBaseRepository();
	public HProjectRepository getHPRepository();
	public Stream<NodeWrapper> getItemsGroupedByValue(NodeWrapper node);
	public Stream<NodeWrapper> getItemsGroupedByField(String entity, String groupByField);
	public Stream<NodeWrapper> getItemsGroupedByField(NodeWrapper node);
	public Stream<NodeWrapper> getItemsGroupedByFieldValue(String entity, String fieldName, Object fieldValue, String groupFieldName);
	public long getCount(String entity, String groupByField, String filterBy, String filterValue);
	public long getCount(String entity, String groupByField);
}
