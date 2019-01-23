package ru.gzpn.spc.csl.services.bl.interfaces;

import java.util.stream.Stream;

import ru.gzpn.spc.csl.model.interfaces.ICProject;
import ru.gzpn.spc.csl.model.interfaces.IHProject;
import ru.gzpn.spc.csl.model.repositories.CProjectRepository;
import ru.gzpn.spc.csl.model.repositories.HProjectRepository;
import ru.gzpn.spc.csl.model.utils.NodeWrapper;

public interface IProjectService {
	public HProjectRepository getBaseRepository();
	public HProjectRepository getHPRepository();
	public CProjectRepository getCPRepository();
	public Stream<NodeWrapper> getItemsGroupedByValue(NodeWrapper node);
	public Stream<NodeWrapper> getItemsGroupedByField(String entity, String groupByField);
	public Stream<NodeWrapper> getItemsGroupedByField(NodeWrapper node);
	public Stream<NodeWrapper> getItemsGroupedByFieldValue(String entity, String fieldName, Object fieldValue, String groupFieldName);
	public long getCount(String entity, String groupByField, String filterBy, String filterValue);
	public long getCount(String entity, String groupByField);
	void saveHProject(IHProject project);
	void saveCProject(ICProject project);
}
