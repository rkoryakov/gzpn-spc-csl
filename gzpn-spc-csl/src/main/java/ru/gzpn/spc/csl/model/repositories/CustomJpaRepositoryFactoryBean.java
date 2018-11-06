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

import ru.gzpn.spc.csl.model.BaseEntity;

public class CustomJpaRepositoryFactoryBean<R extends JpaRepository<T, ID>, T extends BaseEntity, ID extends Serializable> extends JpaRepositoryFactoryBean<R, T, ID> {

	public CustomJpaRepositoryFactoryBean(Class<? extends R> repositoryInterface) {
		super(repositoryInterface);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
	    return new CustomJpaRepositoryFactory(entityManager);
	}
	
	private static class CustomJpaRepositoryFactory<T extends BaseEntity, ID extends Serializable> extends JpaRepositoryFactory {
		private EntityManager entityManager;
		
		public CustomJpaRepositoryFactory(EntityManager entityManager) {
			super(entityManager);
			this.entityManager = entityManager;
		}

		@SuppressWarnings({ "unchecked", "rawtypes", "hiding"})
		@Override
		protected <R, ID extends Serializable> SimpleJpaRepository<?, ?> getTargetRepository(RepositoryInformation information, EntityManager entityManager) {
			// the logic to return concrete Repository implementation
			return new BaseRepositoryImpl(information.getDomainType(), entityManager);
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		protected Object getTargetRepository(RepositoryInformation information) {
			return new BaseRepositoryImpl(information.getDomainType(), entityManager);
		}

		@Override
		protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
			// the logic to return concrete Repository class
			return BaseRepositoryImpl.class;
		}
		
	}
}
