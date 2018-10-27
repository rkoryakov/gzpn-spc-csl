package ru.gzpn.spc.csl.model.repositories;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

public class CommonRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements CommonRepository<T, ID> {

	public CommonRepositoryImpl(Class<T> domainClass, EntityManager em) {
		super(domainClass, em);
	}

	public CommonRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
	}

	@Override
	public long getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getGroupCount(String field) {
		// TODO Auto-generated method stub
		return 0;
	}

}
