package ru.gzpn.spc.csl.model.repositories;

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
	public long getCountByGroupField(String field) {
		StringBuilder jpql = new StringBuilder("SELECT COUNT(DISTINCT e.");
		jpql.append(field)
			.append(") FROM ")
			.append(entityInformation.getEntityName())
			.append(" e");
		
		return entityManager.createQuery(jpql.toString(), Long.class).getSingleResult();
	}
	
	@Override
	public long getCountByGroupField(String entity, String field) {
		StringBuilder jpql = new StringBuilder("SELECT COUNT(DISTINCT e.");
		jpql.append(field)
			.append(") FROM ")
			.append(entity)
			.append(" e");
		
		return entityManager.createQuery(jpql.toString(), Long.class).getSingleResult();
	}

	@Override
	public Stream<NodeWrapper> getItemsGroupedByField(String entity, String field) {
		StringBuilder jpql = new StringBuilder();
//		Formatter formatter = new Formatter(jpql, Locale.ROOT);
//		formatter.format("SELECT NEW ru.gzpn.spc.csl.ui.createdoc.NodeWrapper('%1s', e.name)", entity,);
//		jpql.append(entity)
//			.append("', ")
//			.append(field)
//			.append(") FROM ")
//			.append(entity)
//			.append(" e GROUP BY e.")
//			.append(field);
		
		return entityManager.createQuery(jpql.toString(), NodeWrapper.class).getResultList().stream();
	}
}
