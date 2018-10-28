package ru.gzpn.spc.csl.model.repositories;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

public class CustomJpaRepositoryFactoryBean<R extends JpaRepository<T, ID>, T, ID extends Serializable> extends JpaRepositoryFactoryBean<R, T, ID> {

	public CustomJpaRepositoryFactoryBean(Class<? extends R> repositoryInterface) {
		super(repositoryInterface);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
	    return new CustomJpaRepositoryFactory(entityManager);
	}
	
	private static class CustomJpaRepositoryFactory<T, ID extends Serializable> extends JpaRepositoryFactory {

		public CustomJpaRepositoryFactory(EntityManager entityManager) {
			super(entityManager);
		}

		@SuppressWarnings({ "unchecked", "rawtypes", "hiding" })
		@Override
		protected <T, ID extends Serializable> SimpleJpaRepository<?, ?> getTargetRepository(RepositoryInformation information, EntityManager entityManager) {
			// the logic to return concrete Repository implementation
			return new BaseRepositoryImpl(information.getDomainType(), entityManager);
		}

		@Override
		protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
			// the logic to return concrete Repository class
			return BaseRepositoryImpl.class;
		}
		
	}
}
