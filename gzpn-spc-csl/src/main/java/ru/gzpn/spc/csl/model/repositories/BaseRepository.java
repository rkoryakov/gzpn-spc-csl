package ru.gzpn.spc.csl.model.repositories;

import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import ru.gzpn.spc.csl.model.BaseEntity;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity> extends JpaRepository<T, Long> {
	public long getCountByGroupField(String field);
	public long getCountByGroupField(String entity, String field);
	public Stream<BaseEntity> getItemsByGroupField(String entity, String field);
}
