package ru.gzpn.spc.csl.model.repositories;

import java.util.Formatter;
import java.util.Locale;
import java.util.stream.Stream;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import ru.gzpn.spc.csl.model.BaseEntity;
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
	public long getCountByGroupField(String entity, String groupField) {
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
	
	
}
