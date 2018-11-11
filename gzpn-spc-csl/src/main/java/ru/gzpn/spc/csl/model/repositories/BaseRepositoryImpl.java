package ru.gzpn.spc.csl.model.repositories;

import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

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

			if (groupFieldName != null) {
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
	
	public Stream<NodeWrapper> getItemsGroupedByEntityValue(String sourceEntity, String targetEntity, String fieldName, Object fieldValue, String groupFieldName) {
		StringBuilder jpql = new StringBuilder();
		Stream<NodeWrapper> result = null;
		
		try (Formatter formatter = new Formatter(jpql, Locale.ROOT)) {

			List<Entities> list = ProjectEntityGraph.getPathBetweenNodes(sourceEntity, targetEntity);
			Optional<LinkedFields> linkedFileds = ProjectEntityGraph.getLinkedFields(sourceEntity, targetEntity);
			
			if (linkedFileds.isPresent()) {
				for (Entities e : list) {
					
				}
			}
			
			
			
//			if (groupFieldName != null) {
//				formatter.format("SELECT NEW ru.gzpn.spc.csl.ui.createdoc.NodeWrapper('%1$s', '%4$s', e.%4$s) "
//								+ "FROM %1$s e1 WHERE e1.%2$s = :fieldValue GROUP BY e.%4$s",
//								entity, fieldName, fieldValue, groupFieldName);
//			} else {
//				formatter.format("SELECT NEW ru.gzpn.spc.csl.ui.createdoc.NodeWrapper('%1$s', e) "
//								+ "FROM %1$s e WHERE e.%2$s = :fieldValue",
//								entity, fieldName);
//			}
//			
//			query = entityManager.createQuery(jpql.toString(), NodeWrapper.class);
//			
//			if (fieldValue != null) {
//				query.setParameter("fieldValue", fieldValue);
//			}
//			
//			result = query.getResultList().stream();
		}
		
		return result;
	}
}
