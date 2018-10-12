package ru.gzpn.spc.csl.model.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IHProjectRepository extends JpaRepository<IHProject, Long> {
	@Query(value = "SELECT COUNT(*) FROM spc_csl_schema.havy_projects hp", nativeQuery = true)
	public long getCount();
}
