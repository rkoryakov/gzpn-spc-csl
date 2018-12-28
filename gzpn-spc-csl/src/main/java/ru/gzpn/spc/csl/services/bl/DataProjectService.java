package ru.gzpn.spc.csl.services.bl;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.gzpn.spc.csl.model.repositories.CProjectRepository;
import ru.gzpn.spc.csl.model.repositories.HProjectRepository;
import ru.gzpn.spc.csl.model.repositories.LocalEstimateRepository;
import ru.gzpn.spc.csl.model.repositories.PhaseRepository;
import ru.gzpn.spc.csl.model.repositories.PlanObjectRepository;
import ru.gzpn.spc.csl.model.repositories.StageRepository;
import ru.gzpn.spc.csl.model.repositories.WorkRepository;
import ru.gzpn.spc.csl.model.repositories.WorkSetRepository;
import ru.gzpn.spc.csl.model.utils.Entities;
import ru.gzpn.spc.csl.model.utils.ProjectEntityGraph;
import ru.gzpn.spc.csl.ui.createdoc.NodeWrapper;

@Service
@Transactional
public class DataProjectService {
	public static final Logger logger = LoggerFactory.getLogger(DataProjectService.class);
	@Autowired
	private HProjectRepository hpRepository;
	@Autowired
	private CProjectRepository cpRepository;
	@Autowired
	private PhaseRepository phaseRepository;
	@Autowired
	private StageRepository stageRepository;
	@Autowired
	private PlanObjectRepository planObjectRepository;
	@Autowired
	private WorkRepository workRepository;
	@Autowired
	private LocalEstimateRepository localEstimateRepository;
	@Autowired
	private WorkSetRepository workSetRepository;
	
	
	public WorkSetRepository getWorkSetRepository() {
		return workSetRepository;
	}

	public HProjectRepository getHPRepository() {
		return hpRepository;
	}
	
	public CProjectRepository getCPRepository() {
		return cpRepository;
	}
	
	public PhaseRepository getPhaseRepository() {
		return phaseRepository;
	}
	
	public StageRepository getStageRepository() {
		return stageRepository;
	}
	
	public PlanObjectRepository getPlanObjectRepository() {
		return planObjectRepository;
	}
	
	
	public WorkRepository getWorkRepository() {
		return workRepository;
	}

	public LocalEstimateRepository getLocalEstimateRepository() {
		return localEstimateRepository;
	}

	/**
	 * Items count of the given entity grouped by the given field
	 * 
	 * @param entity string name of the entity
	 * @param groupByField string name of the GROUP BY field 
	 * @return
	 */
	public long getCount(String entity, String groupByField) {
		return getBaseRepository().countOfGroupedItems(entity, groupByField);
	}
	
	/**
	 * Items count of the given entity grouped by the given field filtered by the 
	 * 
	 * @param entity string name of the entity
	 * @param groupByField string name of the GROUP BY field 
	 * @return
	 */
	public long getCount(String entity, String groupByField, String filterBy, String filterValue) {
		return getBaseRepository().countOfGroupedItems(entity, groupByField, filterBy, filterValue);
	}
	
	/**
	 * Get grouped items by field in the target entity
	 * @param node
	 * @return Stream<NodeWrapper>
	 */
	public Stream<NodeWrapper> getItemsGroupedByField(NodeWrapper node) {
		return getItemsGroupedByField(node.getEntityName(), node.getGroupField())
				.peek(e -> {
					e.setParent(node);
					e.setChild(node.getChild());
					/* set hashCode for UI Vaadin */
					e.generateHashCode();
				});
	}
	
	/**
	 * Get grouped items by field value in the target entity
	 * @param node
	 * @return
	 */
	public Stream<NodeWrapper> getItemsGroupedByValue(NodeWrapper node) {
		Stream<NodeWrapper> result = Stream.empty();

		if (node.isGrouping() && node.hasGroupFieldValue() && node.hasChild()) {
			if (hasIntermadiateNode(node)) {
				result = getBaseRepository().getItemsGroupedByFieldValue(node.getEntityName(), node.getChild().getEntityName(), 
							node.getGroupField(), node.getGroupFiledValue(), node.getChild().getGroupField(), 
								node.getParent().getId(), node.getParent().getEntityName());
			} else {
				result = getBaseRepository().getItemsGroupedByFieldValue(node.getEntityName(), node.getChild().getEntityName(), 
							node.getGroupField(), node.getGroupFiledValue(), 
								node.getChild().getGroupField());
			}
			/* set parent and child */
			result = result.peek(e -> {
				e.setParent(node);
				if (node.getChild() != null) {
					e.setChild(node.getChild().getChild());
				}
				/* set hashCode for UI Vaadin */
				e.generateHashCode();
			});
		}
		
		return result;
	}
	
	/**
	 * Checks if the given node's path contains node.getParent node
	 *  
	 * @param node
	 * @return 
	 */
	protected boolean hasIntermadiateNode(NodeWrapper node) {
		boolean result = false;
		
		if (node.hasParent()) {
			String parentEntityName = node.getParent().getEntityName();
			Entities parentEntity = Entities.valueOf(parentEntityName.toUpperCase());
			List<Entities> path = ProjectEntityGraph.getPathBetweenNodes(node.getEntityName(), node.getChild().getEntityName());
			result = path.contains(parentEntity);
		}
		
		return result;
	}
	
	/**
	 * Get grouped items by filed in the same entity
	 * @param entity
	 * @param groupByField
	 * @return Stream<NodeWrapper>
	 */
	public Stream<NodeWrapper> getItemsGroupedByField(String entity, String groupByField) {
		return getBaseRepository().getItemsGroupedByField(entity, groupByField);
	}
	
	/**
	 * Get grouped items by field value in the same entity
	 * @param entity
	 * @param fieldName
	 * @param fieldValue
	 * @param groupFieldName
	 * @return Stream<NodeWrapper>
	 */
	public Stream<NodeWrapper> getItemsGroupedByFieldValue(String entity, String fieldName, Object fieldValue, String groupFieldName) {
		return getBaseRepository().getItemsGroupedByFieldValue(entity, fieldName, fieldValue, groupFieldName);
	}
	
	public HProjectRepository getBaseRepository() {
		// we can use any implementation of the BaseRepository - we use HProjectRepository
		return getHPRepository();
	}
	
	public <T> T executeByField(Supplier<T> suplier, String field) {
		T result = suplier.get();
		return result;
	}
}
