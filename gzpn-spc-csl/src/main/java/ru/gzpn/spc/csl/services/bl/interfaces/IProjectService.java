package ru.gzpn.spc.csl.services.bl.interfaces;

import java.util.stream.Stream;

import ru.gzpn.spc.csl.model.interfaces.ICProject;
import ru.gzpn.spc.csl.model.interfaces.IHProject;
import ru.gzpn.spc.csl.model.repositories.CProjectRepository;
import ru.gzpn.spc.csl.model.repositories.HProjectRepository;
import ru.gzpn.spc.csl.model.repositories.PlanObjectRepository;
import ru.gzpn.spc.csl.model.repositories.StageRepository;
import ru.gzpn.spc.csl.model.utils.NodeWrapper;

public interface IProjectService {
	HProjectRepository getBaseRepository();
	HProjectRepository getHPRepository();
	CProjectRepository getCPRepository();
	StageRepository getStagesRepository();
	
	Stream<NodeWrapper> getItemsGroupedByValue(NodeWrapper node);
	Stream<NodeWrapper> getItemsGroupedByField(String entity, String groupByField);
	Stream<NodeWrapper> getItemsGroupedByField(NodeWrapper node);
	Stream<NodeWrapper> getItemsGroupedByFieldValue(String entity, String fieldName, Object fieldValue, String groupFieldName);
	long getCount(String entity, String groupByField, String filterBy, String filterValue);
	long getCount(String entity, String groupByField);
	void saveHProjectAcls(IHProject project);
	void saveCProjectAcls(ICProject project);
	PlanObjectRepository getPlanObjectRepository();
}
