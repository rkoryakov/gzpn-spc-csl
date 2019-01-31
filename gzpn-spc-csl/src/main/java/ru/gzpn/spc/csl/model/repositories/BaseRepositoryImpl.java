package ru.gzpn.spc.csl.model.repositories;

import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.util.Pair;

import ru.gzpn.spc.csl.model.BaseEntity;
import ru.gzpn.spc.csl.model.enums.Entities;
import ru.gzpn.spc.csl.model.utils.NodeWrapper;
import ru.gzpn.spc.csl.model.utils.ProjectEntityGraph;
import ru.gzpn.spc.csl.model.utils.ProjectEntityGraph.Rib.LinkedFields;
@NoRepositoryBean
public class BaseRepositoryImpl<T extends BaseEntity> extends SimpleJpaRepository<T, Long> implements BaseRepository<T> {
	public static final Logger logger = LoggerFactory.getLogger(BaseRepositoryImpl.class);
	
	private final EntityManager entityManager;
	private final JpaEntityInformation<T, ?> entityInformation;
	private final Class<T> domainClass;
	
	public BaseRepositoryImpl(Class<T> domainClass, EntityManager em) {
		super(domainClass, em);
		this.entityManager = em;
		this.domainClass = domainClass;
		this.entityInformation = JpaEntityInformationSupport.getEntityInformation(domainClass, em);
	}

	public BaseRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager em) {
		super(entityInformation, em);
		this.entityInformation = entityInformation;
		this.entityManager = em;
		this.domainClass = entityInformation.getJavaType();
	}

	@Override
	public long getCountByGroupField(String groupField) {
		StringBuilder jpql = new StringBuilder();
		long result = 0;
		
		try (Formatter formatter = new Formatter(jpql, Locale.ROOT)) {
			formatter.format("SELECT COUNT(DISTINCT e.%2$s) FROM %1$s e", entityInformation.getEntityName(), groupField);
			result = entityManager.createQuery(jpql.toString(), Long.class).getSingleResult();
		}
		
		return result;
	}
	
	@Override
	public long countOfGroupedItems(String entity, String groupField) {
		StringBuilder jpql = new StringBuilder();
		long result = 0;
		
		try (Formatter formatter = new Formatter(jpql, Locale.ROOT)) {
			formatter.format("SELECT COUNT(DISTINCT e.%2$s) FROM %1$s e", entity, groupField);
			result = entityManager.createQuery(jpql.toString(), Long.class).getSingleResult();
		}
		
		return result;
	}

	@Override
	public long countOfGroupedItems(String entity, String groupField, String filterBy, String filterValue) {
		StringBuilder jpql = new StringBuilder();
		long result = 0;
		
		try (Formatter formatter = new Formatter(jpql, Locale.ROOT)) {
			formatter.format("SELECT COUNT(DISTINCT e.%2$s) FROM %1$s e WHERE CAST( e.%3$s as string ) LIKE :filterValue", entity, groupField, filterBy);
			result = entityManager.createQuery(jpql.toString(), Long.class).setParameter("filterValue", filterValue).getSingleResult();
		}
		
		return result;
	}
	
	@Override
	public Stream<NodeWrapper> getItemsGroupedByField(String entity, String groupField) {
		StringBuilder jpql = new StringBuilder();
		Stream<NodeWrapper> result = null;
		
		try (Formatter formatter = new Formatter(jpql, Locale.ROOT)) {
			formatter.format("SELECT NEW ru.gzpn.spc.csl.model.utils.NodeWrapper('%1$s', '%2$s', e.%2$s, e.id) "
							+ "FROM %1$s e GROUP BY e.%2$s, e.id", entity, groupField);
			result = entityManager.createQuery(jpql.toString(), NodeWrapper.class).getResultList().stream();
		}
		
		return result;
	}
	
	@Override
	public Stream<NodeWrapper> getItemsGroupedByFieldValue(String entity, String fieldName, Object fieldValue, String groupFieldName) {
		StringBuilder jpql = new StringBuilder();
		Stream<NodeWrapper> result = null;
		TypedQuery<NodeWrapper> query = null;
		
		try (Formatter formatter = new Formatter(jpql, Locale.ROOT)) {

			if (StringUtils.isNotEmpty(groupFieldName)) {
				formatter.format("SELECT NEW ru.gzpn.spc.csl.model.utils.NodeWrapper('%1$s', '%4$s', e.%4$s, e.id) "
								+ "FROM %1$s e WHERE e.%2$s = :fieldValue GROUP BY e.%4$s, e.id",
								entity, fieldName, fieldValue, groupFieldName);
			} else {
				formatter.format("SELECT NEW ru.gzpn.spc.csl.model.utils.NodeWrapper('%1$s', e, e.id) "
								+ "FROM %1$s e WHERE e.%2$s = :fieldValue",
								entity, fieldName);
			}
			
			query = entityManager.createQuery(jpql.toString(), NodeWrapper.class);
			
			if (fieldValue != null) {
				query.setParameter("fieldValue", fieldValue);
			}
			List<NodeWrapper> resultList = query.getResultList();
			result = resultList.stream();
		}

		return result;
	}
	
	@Override
	public <T> Stream<T> getItemsGroupedByFieldValue(String entity, String fieldName, Object fieldValue, Class<T> entityClass) {
		StringBuilder jpql = new StringBuilder();
		Stream<T> result = null;
		TypedQuery<T> query = null;
		
		try (Formatter formatter = new Formatter(jpql, Locale.ROOT)) {

			formatter.format("SELECT e FROM %1$s e WHERE e.%2$s = :fieldValue", entity, fieldName);
		}
			
		query = entityManager.createQuery(jpql.toString(), entityClass);
			
		if (fieldValue != null) {
			query.setParameter("fieldValue", fieldValue);
		}
		List<T> resultList = query.getResultList();
		result = resultList.stream();
		
		return result;
	}
	
	@Override
	public Stream<NodeWrapper> getItemsGroupedByFieldValue(NodeWrapper node) {
		Stream<NodeWrapper> result = null;
		StringBuilder jpql = new StringBuilder();
		
		try (Formatter formatter = new Formatter(jpql, Locale.ROOT)) {
			createJpqlQueryGroupedByFieldValue(formatter, node);
			logger.debug("JPQL string '{}'", jpql);
			logger.debug("sourceFieldValue '{}'", node.getGroupFiledValue());
		}
		TypedQuery<NodeWrapper> query = entityManager.createQuery(jpql.toString(), NodeWrapper.class);
		List<NodeWrapper> resultList = query.setParameter("sourceFieldValue", node.getGroupFiledValue()).getResultList();
		logger.debug("resultList '{}'", resultList.size());
		result = resultList.stream();
		
		return result;
	}
	
	private void createJpqlQueryGroupedByFieldValue(Formatter formatter, NodeWrapper node) {
		String sourceEntity = node.getEntityName(); 
		String targetEntity = node.getChild().getEntityName();
		String sourceFieldName = node.getGroupField();
		String targetGroupFieldName = node.getChild().getGroupField();
		
		List<Entities> path = ProjectEntityGraph.getPathBetweenNodes(sourceEntity, targetEntity);
		boolean isPathMoreOne = path.size() > 1;
		boolean isGroup = StringUtils.isNotEmpty(targetGroupFieldName);
		Pair<Optional<Integer>, Optional<String>> parentDirectRelation = getParentDirectRelationNode(node, path);
		
		Optional<String> parentEntity = parentDirectRelation.getSecond();
		Optional<Integer> entityIndex = parentDirectRelation.getFirst();
		
		if (isGroup) {
			formatter.format("SELECT NEW ru.gzpn.spc.csl.model.utils.NodeWrapper('%2$s', '%3$s', T.%3$s, T.id)"
					+ " FROM %1$s S, %2$s T", sourceEntity, targetEntity, targetGroupFieldName);
		} else {
			formatter.format("SELECT DISTINCT NEW ru.gzpn.spc.csl.model.utils.NodeWrapper('%2$s', T, T.id)"
					+ " FROM %1$s S, %2$s T", sourceEntity, targetEntity);
		}
		
		for (int i = 1; i < path.size() - 1; i ++) {
			formatter.format(", %1$s E_%2$d ", path.get(i).getName(), i);
		}

		if (parentEntity.isPresent()) {
			formatter.format(", %1$s P_1", parentEntity.get());
		}
		
		Optional<LinkedFields> sourceNext = ProjectEntityGraph.getLinkedFields(sourceEntity,
					isPathMoreOne ? path.get(1).getName() : path.get(0).getName());
		String leftSourceField = sourceNext.get().getLeftEntityField();
		String rightSourceField = sourceNext.get().getRightEntityField();
		
		if (path.size() == 1 || path.size() == 2) {
			formatter.format(" WHERE S.%1$s = :sourceFieldValue AND S.%2$s = T.%3$s", sourceFieldName,
					leftSourceField, rightSourceField);
		} else if (path.size() > 2) {
			formatter.format(" WHERE S.%1$s = :sourceFieldValue AND S.%2$s = E_1.%3$s", sourceFieldName,
					leftSourceField, rightSourceField);
		}
		
		for (int i = 1; i < path.size() - 1; i ++) {
			Entities left = path.get(i);
			Entities right = path.get(i + 1);
			String leftShortcut = "E_" + i;
			String rightShortcut = (i == path.size() - 2) ? "T" : "E_" + (i + 1);

			Optional<LinkedFields> linked = ProjectEntityGraph.getLinkedFields(left.getName(), right.getName());
			formatter.format(" AND %1$s.%2$s = %3$s.%4$s ", leftShortcut, linked.get().getLeftEntityField(),
					rightShortcut, linked.get().getRightEntityField());
		}

		if (parentEntity.isPresent()) {
			String parentName = parentEntity.get();
			NodeWrapper parentNode = NodeWrapper.findParentByEntityName(node, parentName);
			
			if (entityIndex.isPresent()) {
				String entityByIndexName = path.get(entityIndex.get()).getName();
				Optional<LinkedFields> linked = ProjectEntityGraph.getLinkedFields(entityByIndexName, parentName);
			
				if (entityIndex.get() < path.size() - 1
									&& linked.isPresent()) {
					formatter.format(" AND E_%1$d.%2$s = P_1.%3$s AND P_1.id = %4$d", entityIndex.get(), 
																	linked.get().getLeftEntityField(),
																	linked.get().getRightEntityField(),
																	parentNode.getId());
				}
				else if (entityIndex.get() == path.size() - 1) {
					formatter.format(" AND T.%1$s = P_1.%2$s AND P_1.id = %3$d", 
																	linked.get().getLeftEntityField(), 
																	linked.get().getRightEntityField(),
																	parentNode.getId());
				}
			}
		}
		
		if (isGroup) {
			formatter.format(" GROUP BY T.%1$s, T.id", targetGroupFieldName);
		}
		
	}
	

	/**
	 * Get the parent entity name that has a direct relation to the node. 
	 * For example, we have the following hierarchy settings: 
	 * 		HProject ->						HProject ->
	 * 			CProject ->			OR 			CProject ->	
	 * 				Stage ->						LocalEstimate ->
	 * 					PlanObject							PlanObject
	 * 
	 * 	And if we'll go to PlanObject (thru Stage or LocalEstimate) we also need to join with CProject.
	 *  Because otherwise we would have got all the PlanObject's items by Stage or by LocalEstimate	
	 *  
	 *  @return Pair of entity path index and parent direct relation node name
	 */
	private Pair<Optional<Integer>, Optional<String>> getParentDirectRelationNode(NodeWrapper node, List<Entities> path) {
		Pair<Optional<Integer>, Optional<String>> result = Pair.of(Optional.empty(), Optional.empty());
		// TODO: return List of pairs to 
		String entityName = node.getChild().getEntityName();
		
//		NodeWrapper parentNode = node.getParent();
//		while (parentNode != null && parentNode.hasParent() 
//				&& !hasDirectRelation(entityName, parentNode.getParent().getEntityName())) {
//			parentNode = parentNode.getParent();
//			parentEntityName = parentNode.getParent().getEntityName();
//		}
		/* search for for the direct relation between node.child().getEntityName() and its parents first off all */
		/* if the direct relation hasn't been found then we trying to find it in the path */
		/* note, current node - node.child().getEntityName() is in the path at index = path.size - 1 */
		
		for (int i = path.size() - 1; i > 0; i --) {
			entityName = path.get(i).getName();
			NodeWrapper parentNode = node.getParent();
			while (parentNode != null  
					&& !hasDirectRelation(entityName, parentNode.getEntityName())) {
				parentNode = parentNode.getParent();
			}
				
			if (parentNode != null) {
				/* the direct relation has been found */
				Optional<String> parentDirectRelationNode = Optional.ofNullable(parentNode.getEntityName());
				Optional<Integer> parentEntityIndex = Optional.ofNullable(i);
				result = Pair.of(parentEntityIndex, parentDirectRelationNode);
				break;
			}
		}
		
//		if (hasDirectRelation(entityName, parentEntityName)) {
//			formatter.format(", %1$s P_1", parentEntityName);
//			formatter.format(" %1$s", formatter.substring(formatter.indexOf("WHERE"), formatter.length()));
//			LinkedFields linkedFields = ProjectEntityGraph.getLinkedFields(entityName, parentEntityName).get();
//			formatter.format(" AND T.%1$s = P_1.%2$s", linkedFields.getLeftEntityField(), linkedFields.getRightEntityField());
//		}
		
		return result;
	}

	private void addParentFromClause(Formatter formatter, Optional<LinkedFields> linkedFields, String parentEntityName) {
		if (linkedFields.isPresent()) {
			formatter.format(", %1$s P_1", parentEntityName);
		}
	}
	
	private void addParentWhereClause(Formatter formatter, Optional<LinkedFields> linkedFields) {
		if (linkedFields.isPresent()) {
			formatter.format(" AND T.%1$s = P_1.%2$s", 
					linkedFields.get().getLeftEntityField(), linkedFields.get().getRightEntityField());
		}
	}
	
	private boolean hasDirectRelation(String entityName, String parentEntityName) {
		return ProjectEntityGraph.getLinkedFields(entityName, parentEntityName).isPresent();
	}

//	public Stream<NodeWrapper> getItemsGroupedByFieldValue(String sourceEntity, String targetEntity, 
//										String sourceFieldName, Object sourceFieldValue, String targetGroupFieldName, Long parentId, String parentEntity) {
//		StringBuilder jpql = new StringBuilder();
//		Stream<NodeWrapper> result = null;
//		
//		try (Formatter formatter = new Formatter(jpql, Locale.ROOT)) {
//			createJpqlQueryGroupedByFieldValue(formatter, sourceEntity, targetEntity, sourceFieldName, targetGroupFieldName, parentId, parentEntity);
//			logger.debug("JPQL string '{}'", jpql);
//			logger.debug("sourceFieldValue '{}'", sourceFieldValue);
//			
//			TypedQuery<NodeWrapper> query = entityManager.createQuery(jpql.toString(), NodeWrapper.class);
//			List<NodeWrapper> resultList = query.setParameter("sourceFieldValue", sourceFieldValue).getResultList();
//			logger.debug("resultList '{}'", resultList.size());
//			//resultList.forEach(e -> {e.generateHashCode(); logger.debug("repository node {}", e);});
//			result = resultList.stream();
//		}
//		
//		return result;
//	}
	
//	private void createJpqlQueryGroupedByFieldValue(Formatter jpqlFormatter, String sourceEntity, String targetEntity,
//									String sourceFieldName, String targetGroupFieldName, Long parentId, String parentEntity) {
//		List<Entities> path = ProjectEntityGraph.getPathBetweenNodes(sourceEntity, targetEntity);
//		boolean isGroup = StringUtils.isNotEmpty(targetGroupFieldName);
//
//		if (isGroup) {
//			jpqlFormatter.format("SELECT NEW ru.gzpn.spc.csl.model.utils.NodeWrapper('%2$s', '%3$s', T.%3$s, T.id)"
//					+ " FROM %1$s S, %2$s T", sourceEntity, targetEntity, targetGroupFieldName);
//		} else {
//			jpqlFormatter.format("SELECT DISTINCT NEW ru.gzpn.spc.csl.model.utils.NodeWrapper('%2$s', T, T.id)"
//					+ " FROM %1$s S, %2$s T", sourceEntity, targetEntity);
//		}
//
//		for (int i = 1; i < path.size() - 1; i++) {
//			jpqlFormatter.format(", %1$s E_%2$d ", path.get(i).getName(), i);
//		}
//
//		Optional<LinkedFields> sourceNext = ProjectEntityGraph.getLinkedFields(sourceEntity,
//				path.size() > 1 ? path.get(1).getName() : path.get(0).getName());
//		String leftSourceField = sourceNext.get().getLeftEntityField();
//		String rightSourceField = sourceNext.get().getRightEntityField();
//
//		if (path.size() == 1 || path.size() == 2) {
//			jpqlFormatter.format(" WHERE S.%1$s = :sourceFieldValue AND S.%2$s = T.%3$s", sourceFieldName,
//					leftSourceField, rightSourceField);
//		} else if (path.size() > 2) {
//			jpqlFormatter.format(" WHERE S.%1$s = :sourceFieldValue AND S.%2$s = E_1.%3$s", sourceFieldName,
//					leftSourceField, rightSourceField);
//		}
//		
//		addParentIdSqlExpression(jpqlFormatter, path, parentEntity, parentId);
//		
//		for (int i = 1; i < path.size() - 1; i++) {
//			Entities left = path.get(i);
//			Entities right = path.get(i + 1);
//			String leftShortcut = "E_" + i;
//			String rightShortcut = (i == path.size() - 2) ? "T" : "E_" + (i + 1);
//
//			Optional<LinkedFields> linked = ProjectEntityGraph.getLinkedFields(left.getName(), right.getName());
//			jpqlFormatter.format(" AND %1$s.%2$s = %3$s.%4$s ", leftShortcut, linked.get().getLeftEntityField(),
//					rightShortcut, linked.get().getRightEntityField());
//		}
//
//		if (isGroup) {
//			jpqlFormatter.format(" GROUP BY T.%1$s, T.id", targetGroupFieldName);
//		}
//	}
	
	/**
	 * Add parent entity relation clause to the JPQL expression. 
	 * For example, we have the following hierarchy settings: 
	 * 		HProject ->						HProject ->
	 * 			CProject ->			OR 			CProject ->	
	 * 				Stage ->						LocalEstimate ->
	 * 					PlanObject							PlanObject
	 * 
	 * 	And if we'll go to PlanObject (thru Stage or LocalEstimate) we also need to join with CProject.
	 *  Because otherwise we would have got all the PlanObject's items by Stage or by LocalEstimate		
	 */
//	private void addParentIdSqlExpression(Formatter formatter, List<Entities> path, String parentEntity, Long parentId) {
//		if (!Objects.isNull(parentId) && StringUtils.isNotEmpty(parentEntity)) {
//			int parentEntityIndex = path.indexOf(Entities.valueOf(parentEntity.toUpperCase()));
//			if (parentEntityIndex > 0 && parentEntityIndex < path.size() - 1) {
//				formatter.format(" AND E_%1$d.id = %2$d", parentEntityIndex, parentId);
//			} else if (parentEntityIndex == 0) {
//				formatter.format(" AND S.id = %1$d", parentId);
//			} else if (parentEntityIndex == path.size() - 1) {
//				formatter.format(" AND T.id = %1$d", parentId);
//			} else if (parentEntityIndex == -1) {
//				throw new IllegalArgumentException("Entity " + parentEntity + " doesn't exist in the grouping path " + path 
//								+ ". Maybe there is another shorter path between these two entity nodes.");
//			}
//		}
//	}
	
	public EntityManager getEntityManager() {
		return this.entityManager;
	}
	
}
