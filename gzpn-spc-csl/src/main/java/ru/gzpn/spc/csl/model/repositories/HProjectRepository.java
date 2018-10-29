package ru.gzpn.spc.csl.model.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ru.gzpn.spc.csl.model.HProject;
import ru.gzpn.spc.csl.model.Stage;

@Repository
public interface HProjectRepository extends BaseRepository<HProject> {

	@Query(value = "SELECT COUNT(*) FROM spc_csl_schema.havy_projects hp", nativeQuery = true)
	public long getHProjectCount();

//	@Query(value = "SELECT * FROM spc_csl_schema.stages s", nativeQuery = true)
//	public List<Object[]> getCountOfStages();
	
	@Query(value = "SELECT s FROM Stage s", nativeQuery = false)
	public List<Stage> getStages();
	
	public long groupByPrjIdCount();
	public long groupByNameCount();
}
