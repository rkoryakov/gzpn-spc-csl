package ru.gzpn.spc.csl.model.repositories;

import java.util.List;

import org.springframework.stereotype.Repository;

import ru.gzpn.spc.csl.model.LocalEstimate;
import ru.gzpn.spc.csl.model.interfaces.IEstimateCalculation;
import ru.gzpn.spc.csl.model.interfaces.ILocalEstimate;

@Repository
public interface LocalEstimateRepository extends BaseRepository<LocalEstimate> {
	List<ILocalEstimate> findByEstimateCalculation(IEstimateCalculation estimateCalculation);
}
