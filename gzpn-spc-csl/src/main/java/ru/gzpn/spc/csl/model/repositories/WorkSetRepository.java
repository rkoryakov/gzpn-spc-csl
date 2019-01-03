package ru.gzpn.spc.csl.model.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;

import ru.gzpn.spc.csl.model.WorkSet;
import ru.gzpn.spc.csl.model.interfaces.IPlanObject;
import ru.gzpn.spc.csl.model.interfaces.IWorkSet;

public interface WorkSetRepository extends BaseRepository<WorkSet> {
	
	public List<IWorkSet> findByPlanObject(IPlanObject planObj, Pageable pageable);
}


