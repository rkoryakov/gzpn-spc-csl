package ru.gzpn.spc.csl.model.repositories;

import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import ru.gzpn.spc.csl.model.BaseEntity;
import ru.gzpn.spc.csl.model.utils.Entities;
import ru.gzpn.spc.csl.model.utils.ProjectEntityGraph;
import ru.gzpn.spc.csl.model.utils.ProjectEntityGraph.Rib.LinkedFields;
import ru.gzpn.spc.csl.ui.createdoc.NodeWrapper;
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
	public Stream<NodeWrapper> getItemsGroupedByField(String entity, String groupField) {
		StringBuilder jpql = new StringBuilder();
		Stream<NodeWrapper> result = null;
		
		try (Formatter formatter = new Formatter(jpql, Locale.ROOT)) {
			formatter.format("SELECT NEW ru.gzpn.spc.csl.ui.createdoc.NodeWrapper('%1$s', '%2$s', e.%2$s) "
							+ "FROM %1$s e GROUP BY e.%2$s", entity, groupField);
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

			if (groupFieldName != null && !groupFieldName.isEmpty()) {
				formatter.format("SELECT NEW ru.gzpn.spc.csl.ui.createdoc.NodeWrapper('%1$s', '%4$s', e.%4$s) "
								+ "FROM %1$s e WHERE e.%2$s = :fieldValue GROUP BY e.%4$s",
								entity, fieldName, fieldValue, groupFieldName);
			} else {
				formatter.format("SELECT NEW ru.gzpn.spc.csl.ui.createdoc.NodeWrapper('%1$s', e) "
								+ "FROM %1$s e WHERE e.%2$s = :fieldValue",
								entity, fieldName);
			}
			
			query = entityManager.createQuery(jpql.toString(), NodeWrapper.class);
			
			if (fieldValue != null) {
				query.setParameter("fieldValue", fieldValue);
			}
			
			result = query.getResultList().stream();
		}

		return result;
	}
	
	@Override
	public Stream<NodeWrapper> getItemsGroupedByFieldValue(String sourceEntity, String targetEntity, 
										String sourceFieldName, Object sourceFieldValue, String targetGroupFieldName) {
		StringBuilder jpql = new StringBuilder();
		Stream<NodeWrapper> result = null;
		
		try (Formatter formatter = new Formatter(jpql, Locale.ROOT)) {
			createJpqlQueryGroupedByFieldValue(formatter, sourceEntity, targetEntity, sourceFieldName, targetGroupFieldName);
			logger.debug("JPQL string '{}'", jpql);
			TypedQuery<NodeWrapper> query = entityManager.createQuery(jpql.toString(), NodeWrapper.class);
			result = query.setParameter("sourceFieldValue", sourceFieldValue).getResultList().stream();
		}
		return result;
	}
	
	private void createJpqlQueryGroupedByFieldValue(Formatter jpqlFormatter, String sourceEntity, String targetEntity,
										String sourceFieldName, String targetGroupFieldName) {
		
		List<Entities> path = ProjectEntityGraph.getPathBetweenNodes(sourceEntity, targetEntity);
		boolean isPathMoreOne = path.size() > 1;
		boolean isGroup = targetGroupFieldName != null && !targetGroupFieldName.isEmpty();
		
		if (isGroup) {
			jpqlFormatter.format("SELECT NEW ru.gzpn.spc.csl.ui.createdoc.NodeWrapper('%1$s', '%3$s', T.%3$s)"
					+ " FROM %1$s S, %2$s T", sourceEntity, targetEntity, targetGroupFieldName);
		} else {
			jpqlFormatter.format("SELECT DISTINCT NEW ru.gzpn.spc.csl.ui.createdoc.NodeWrapper('%2$s', T)"
					+ " FROM %1$s S, %2$s T", sourceEntity, targetEntity);
		}
		
		for (int i = 1; i < path.size() - 1; i++) {
			jpqlFormatter.format(", %1$s E_%2$d ", path.get(i).getName(), i);
		}

		Optional<LinkedFields> sourceNext = ProjectEntityGraph.getLinkedFields(sourceEntity,
					isPathMoreOne ? path.get(1).getName() : path.get(0).getName());
		String leftSourceField = sourceNext.get().getLeftEntityField();
		String rightSourceField = sourceNext.get().getRightEntityField();
		jpqlFormatter.format(" WHERE S.%1$s = :sourceFieldValue AND S.%2$s = %3$s.%4$s", sourceFieldName,
					leftSourceField, isPathMoreOne ? "E_1" : "T", rightSourceField);

		for (int i = 1; i < path.size() - 1; i++) {
			Entities left = path.get(i);
			Entities right = path.get(i + 1);
			String leftShortcut = "E_" + i;
			String rightShortcut = (i == path.size() - 2) ? "T" : "E_" + (i + 1);

			Optional<LinkedFields> linked = ProjectEntityGraph.getLinkedFields(left.getName(), right.getName());
			jpqlFormatter.format(" AND %1$s.%2$s = %3$s.%4$s ", leftShortcut, linked.get().getLeftEntityField(),
					rightShortcut, linked.get().getRightEntityField());
		}

		if (isGroup) {
			jpqlFormatter.format(" GROUP BY T.%1$s", targetGroupFieldName);
		}
	}
	
	public EntityManager getEntityManager() {
		return this.entityManager;
	}
}
