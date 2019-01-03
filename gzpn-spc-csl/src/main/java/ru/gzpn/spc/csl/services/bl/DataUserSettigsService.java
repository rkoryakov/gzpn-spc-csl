package ru.gzpn.spc.csl.services.bl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.gzpn.spc.csl.model.UserSettings;
import ru.gzpn.spc.csl.model.jsontypes.CreateDocSettingsJson;
import ru.gzpn.spc.csl.model.repositories.UserSettingsRepository;
import ru.gzpn.spc.csl.services.bl.interfaces.IDataUserSettigsService;

@Service
@Transactional
public class DataUserSettigsService implements IDataUserSettigsService {
	public static final Logger logger = LoggerFactory.getLogger(DataUserSettigsService.class);
	@Autowired
	private UserSettingsRepository repository;
	@Autowired
	ServerProperties serverProperties;
	
	public CreateDocSettingsJson getCreateDocSettings() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Object principal = new Object();
		if (auth != null) {
			principal = auth.getPrincipal();
		}
		
		String user = null;
		
		if (principal instanceof UserDetails) {
			user = ((UserDetails)principal).getUsername();
		} else {
			user = principal.toString();
		}
		
		return getCreateDocSettings(user);
	}
	
	public CreateDocSettingsJson getCreateDocSettings(String userId) {
		UserSettings userSettings = repository.findByUserId(userId);
		CreateDocSettingsJson result = null;
		
		if (userSettings != null) {
			result = userSettings.getDocSettingsJson();
		} else {
			result = new CreateDocSettingsJson();
		}
		
		return result;
	}
	
	public void saveCreateDocSettingsJson(String userId, CreateDocSettingsJson createDoc) {
		UserSettings userSettings = repository.findByUserId(userId);
		
		if (userSettings != null) {
			userSettings.setDocSettingsJson(createDoc);
		} else {
			userSettings = new UserSettings(); 
			userSettings.setDocSettingsJson(createDoc);
			repository.save(userSettings);
		}
	}
}
