package ru.gzpn.spc.csl.services.bl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.gzpn.spc.csl.model.UserSettings;
import ru.gzpn.spc.csl.model.jsontypes.CreateDocSettingsJson;
import ru.gzpn.spc.csl.model.repositories.UserSettingsRepository;
import ru.gzpn.spc.csl.ui.createdoc.NodeWrapper;

@Service
@Transactional
public class DataUserSettigsService {
	public static final Logger logger = LoggerFactory.getLogger(DataUserSettigsService.class);
	@Autowired
	private UserSettingsRepository repository;
	
	public NodeWrapper getDefaultNodesHierarchyCreateDoc() {
		NodeWrapper root =  new NodeWrapper("HProject", "name");
		root.addChild(new NodeWrapper("CProject", "name"))
				.addChild(new NodeWrapper("Phase", "name"))
					.addChild(new NodeWrapper("Phase"));
		return root;
	}
	
	public CreateDocSettingsJson getCreateDocSettings(String userId) {
		List<UserSettings> list = repository.findByUserId(userId);
		CreateDocSettingsJson result = null;
		
		if (!list.isEmpty()) {
			result = list.get(0).getDocSettingsJson();
		}
		return result;
	}
}
