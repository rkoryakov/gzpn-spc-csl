package ru.gzpn.spc.csl.model.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ru.gzpn.spc.csl.model.WorkSet;
import ru.gzpn.spc.csl.model.interfaces.IWorkSet;

@Repository
public interface WorkSetRepository extends BaseRepository<WorkSet> {
	@Query(value = "SELECT ws FROM WorkSet ws WHERE ws.planObject.id = ?1", 
	countQuery = "SELECT COUNT(ws) FROM WorkSet ws WHERE ws.planObject.id = ?1")
	public List<IWorkSet> findByPlanObjectId(Long id/*, Pageable pageable*/);
	
	@Query(value = "SELECT po.workset FROM PlanObject po WHERE po.cproject.id = ?1",
	countQuery = "SELECT COUNT(po) FROM PlanObject po WHERE po.cproject.id = ?1")
	public List<IWorkSet> findWorkSetByCProjectId(Long id/*, Pageable pageable*/);
	
	@Query(value = "SELECT po.workset FROM PlanObject po WHERE po.cproject.stage.id = ?1",
	countQuery = "SELECT COUNT(po.workset) FROM PlanObject po WHERE po.cproject.stage.id = ?1")
	public List<IWorkSet> findWorkSetByStageId(Long id/*, Pageable pageable*/);
}


