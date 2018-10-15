package ru.gzpn.spc.csl.model.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ru.gzpn.spc.csl.model.HProject;

@Repository
public interface IHProjectRepository extends JpaRepository<HProject, Long> {

	@Query(value = "SELECT COUNT(*) FROM spc_csl_schema.havy_projects hp", nativeQuery = true)
	public long getCount();

//	@Query(value = "SELECT * FROM spc_csl_schema.stages s", nativeQuery = true)
//	public List<Object[]> getCountOfStages();
//	@Query(value = "SELECT s FROM Stage s", nativeQuery = false)
//	public List<Stage> getCountOfStages();
}
