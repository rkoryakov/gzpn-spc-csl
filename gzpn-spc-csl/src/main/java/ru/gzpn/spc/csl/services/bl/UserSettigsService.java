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
import ru.gzpn.spc.csl.model.jsontypes.UserSettingsJson;
import ru.gzpn.spc.csl.model.repositories.UserSettingsRepository;
import ru.gzpn.spc.csl.services.bl.interfaces.IDataUserSettigsService;

@Service
@Transactional
public class UserSettigsService implements IDataUserSettigsService {
	public static final Logger logger = LoggerFactory.getLogger(UserSettigsService.class);
	@Autowired
	private UserSettingsRepository repository;
	@Autowired
	ServerProperties serverProperties;
	
	@Override
	public UserSettingsJson getUserSettings() {
		String user = getCurrentUser();
		return getUserSettings(user);
	}
	
	@Override
	public String getCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String user = null;
		
		Object principal = new Object();
		if (auth != null) {
			principal = auth.getPrincipal();
		}
		
		if (principal instanceof UserDetails) {
			user = ((UserDetails)principal).getUsername();
		} else {
			user = principal.toString();
		}
		
		return user;
	}

	@Override
	public UserSettingsJson getUserSettings(String userId) {
		UserSettings userSettings = repository.findByUserId(userId);
		UserSettingsJson result = null;
		
		if (userSettings != null) {
			result = userSettings.getDocSettingsJson();
		} else {
			result = new UserSettingsJson();
		}
		
		return result;
	}
	
	@Override
	public void save(String userId, UserSettingsJson createDoc) {
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
