package ru.gzpn.spc.csl.services.bl;

import java.util.List;

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
import ru.gzpn.spc.csl.model.interfaces.ICProjectRepository;
import ru.gzpn.spc.csl.model.interfaces.IHProjectRepository;
import ru.gzpn.spc.csl.model.interfaces.IPhaseRepository;

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
	
	public List<CProject> getCProjects() {
		return cpRepository.findAll();
	}
	
	public long getCProjectsCount() {
		return cpRepository.count();
	}
	
	public List<Phase> getPhases() {
		return phaseRepository.findAll();
	}
	
	public long getPhasesCount() {
		return phaseRepository.count();
	}
	
	public EntityManager geEntityManager(Class<?> c) {
		return jpaContext.getEntityManagerByManagedType(c.getClass());
	}
}
