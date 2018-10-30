package ru.gzpn.spc.csl.services.bl;

import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.gzpn.spc.csl.model.repositories.CProjectRepository;
import ru.gzpn.spc.csl.model.repositories.HProjectRepository;
import ru.gzpn.spc.csl.model.repositories.PhaseRepository;

@Service
@Transactional
public class DataProjectService {
	public static final Logger logger = LoggerFactory.getLogger(DataProjectService.class);
	@Autowired
	private HProjectRepository hpRepository;
	@Autowired
	private CProjectRepository cpRepository;
	@Autowired
	private PhaseRepository phaseRepository;

	public HProjectRepository getHPRepository() {
		return hpRepository;
	}
	
	public CProjectRepository getCPRepository() {
		return cpRepository;
	}
	
	public PhaseRepository getPhaseRepository() {
		return phaseRepository;
	}
	
	/**
	 * Items count of the given entity grouped by given field
	 * 
	 * @param entity string name of the entity
	 * @param groupByField string name of the GROUPBY field 
	 * @return
	 */
	public long getCount(String entity, String groupByField) {
		// To get the count of the given entity we need the base repository implementation
		// thus we can use any implementation of the BaseRepository - we use HProjectRepository
		return hpRepository.getCountByGroupField(entity, groupByField);
	}
	
	public <T> T executeByField(Supplier<T> suplier, String field) {
		T result = suplier.get();
		return result;
	}
}
