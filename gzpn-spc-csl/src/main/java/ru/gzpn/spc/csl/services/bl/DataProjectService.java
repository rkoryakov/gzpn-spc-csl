package ru.gzpn.spc.csl.services.bl;

import java.util.function.Supplier;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.gzpn.spc.csl.model.repositories.CProjectRepository;
import ru.gzpn.spc.csl.model.repositories.HProjectRepository;
import ru.gzpn.spc.csl.model.repositories.PhaseRepository;
import ru.gzpn.spc.csl.ui.createdoc.NodeWrapper;

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
		return getBaseRepository().getCountByGroupField(entity, groupByField);
	}
	
	public Stream<NodeWrapper> getItemsGroupedBy(NodeWrapper node) {
		return getItemsGroupedByField(node.getEntityName(), node.getGroupFiled())
				.peek(e -> e.setParent(node));
	}
	
	public Stream<NodeWrapper> getItemsGroupedByField(String entity, String groupByField) {
		return getBaseRepository().getItemsGroupedByField(entity, groupByField);
	}
	
	public HProjectRepository getBaseRepository() {
		// To get the count of the given entity we need the base repository implementation
		// thus we can use any implementation of the BaseRepository - we use HProjectRepository
		return getHPRepository();
	}
	
	public <T> T executeByField(Supplier<T> suplier, String field) {
		T result = suplier.get();
		return result;
	}
}
