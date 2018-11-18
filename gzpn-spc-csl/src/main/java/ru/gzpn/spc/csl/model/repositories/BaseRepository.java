package ru.gzpn.spc.csl.model.repositories;

import java.util.stream.Stream;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import ru.gzpn.spc.csl.model.BaseEntity;
import ru.gzpn.spc.csl.ui.createdoc.NodeWrapper;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity> extends JpaRepository<T, Long> {
	public EntityManager getEntityManager();
	public long getCountByGroupField(String groupField);
	public long countOfGroupedItems(String entity, String groupField);
	public Stream<NodeWrapper> getItemsGroupedByField(String entity, String groupField);
	public Stream<NodeWrapper> getItemsGroupedByFieldValue(String entity, String fieldName, 
			Object fieldValue, String groupFieldName);
	public Stream<NodeWrapper> getItemsGroupedByEntityValue(String sourceEntity, String targetEntity, 
			String sourceFieldName,  Object sourceFieldValue, String targetGroupFieldName);
	
}
