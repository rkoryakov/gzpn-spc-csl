package ru.gzpn.spc.csl.services.bl;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.gzpn.spc.csl.model.UserSettings;
import ru.gzpn.spc.csl.model.repositories.UserSettingsRepository;
import ru.gzpn.spc.csl.ui.createdoc.NodeWrapper;

@Service
@Transactional
public class DataUserSettigsService {
	public static final Logger logger = LoggerFactory.getLogger(DataUserSettigsService.class);
	@Autowired
	private UserSettingsRepository repository;
	@Autowired
	private JpaContext jpaContext;
	
	public NodeWrapper getDefaultNodesHierarchy() {
		return new NodeWrapper("HProject", "name")
				.addChild(new NodeWrapper("CProject", "name"))
				.addChild(new NodeWrapper("Phase", "name"))
				.addChild(new NodeWrapper("Phase"));
	}
	
	public EntityManager geEntityManager() {
		return jpaContext.getEntityManagerByManagedType(UserSettings.class);
	}
}
