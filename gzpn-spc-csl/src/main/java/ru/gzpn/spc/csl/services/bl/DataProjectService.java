package ru.gzpn.spc.csl.services.bl;

import java.util.List;
import java.util.function.Supplier;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.gzpn.spc.csl.model.CProject;
import ru.gzpn.spc.csl.model.HProject;
import ru.gzpn.spc.csl.model.Phase;
import ru.gzpn.spc.csl.model.repositories.ICProjectRepository;
import ru.gzpn.spc.csl.model.repositories.IHProjectRepository;
import ru.gzpn.spc.csl.model.repositories.IPhaseRepository;

@Service
@Transactional
public class DataProjectService {
	public static final Logger logger = LoggerFactory.getLogger(DataProjectService.class);
	@Autowired
	private IHProjectRepository hpRepository;
	@Autowired
	private ICProjectRepository cpRepository;
	@Autowired
	private IPhaseRepository phaseRepository;
	
	@Autowired
	JpaContext jpaContext;
	
	
	public List<HProject> getHProjects() {
		return hpRepository.findAll();
	}
	
	public long getHProjectsCount() {
		return hpRepository.count();
	}
	
	public long getHProjectGroupCount(String field) {
		long count = -1;
		switch (field) {
		case HProject.FILED_NAME:
			count = hpRepository.groupByNameCount();
			break;
		case HProject.FILED_PROJECT_ID:
			count = hpRepository.groupByPrjIdCount();
			break;
		}
		return count;
	}
	
	public List<CProject> getCProjects() {
		return cpRepository.findAll();
	}
	
	public long getCProjectsCount() {
		return cpRepository.count();
	}
	
	public long getCProjectsGroupCount(String field) {
		long count = -1;
		switch (field) {
		case CProject.FILED_NAME:
			count = hpRepository.groupByNameCount();
			break;
		case CProject.FILED_PROJECT_ID:
			count = hpRepository.groupByPrjIdCount();
			break;
		}
		return count;
	}
	
	public List<Phase> getPhases() {
		return phaseRepository.findAll();
	}
	
	public long getPhasesCount() {
		return phaseRepository.count();
	}
	
	public EntityManager geEntityManager() {
		return jpaContext.getEntityManagerByManagedType(HProject.class);
	}
	
	public <T> T executeByField(Supplier<T> suplier, String field) {
		T result = null;
		
		return result;
	}
}
