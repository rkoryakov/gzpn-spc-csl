package ru.gzpn.spc.csl.model.repositories;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import ru.gzpn.spc.csl.model.BaseEntity;
@NoRepositoryBean
public class BaseRepositoryImpl<T extends BaseEntity> extends SimpleJpaRepository<T, Long> implements BaseRepository<T> {

	private final EntityManager entityManager;
	
	public BaseRepositoryImpl(Class<T> domainClass, EntityManager em) {
		super(domainClass, em);
		this.entityManager = em;
	}

	public BaseRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager em) {
		super(entityInformation, em);
		this.entityManager = em;
	}

	@Override
	public long getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getCountByGroupField(String field) {
		// TODO Auto-generated method stub
		return 0;
	}

}
