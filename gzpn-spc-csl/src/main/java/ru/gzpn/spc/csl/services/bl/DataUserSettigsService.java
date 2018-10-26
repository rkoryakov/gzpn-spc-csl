package ru.gzpn.spc.csl.services.bl;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.gzpn.spc.csl.model.UserSettings;
import ru.gzpn.spc.csl.model.interfaces.IUserSettingsRepository;
import ru.gzpn.spc.csl.ui.createdoc.NodeWrapper;

@Service
@Transactional
public class DataUserSettigsService {
	public static final Logger logger = LoggerFactory.getLogger(DataUserSettigsService.class);
	@Autowired
	private IUserSettingsRepository repository;
	@Autowired
	private JpaContext jpaContext;
	
	public Deque<NodeWrapper> getDefaultNodesPath() {
		
		return new ArrayDeque<>(Arrays.asList(new NodeWrapper("HProject", "projectId"), 
				new NodeWrapper("HProject", "name"),
				new NodeWrapper("CProject", "name"),
				new NodeWrapper("Phase", null))); 
	}
	
	public EntityManager geEntityManager() {
		return jpaContext.getEntityManagerByManagedType(UserSettings.class);
	}
}
