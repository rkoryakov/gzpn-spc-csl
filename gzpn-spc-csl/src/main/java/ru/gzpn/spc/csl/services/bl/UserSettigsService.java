package ru.gzpn.spc.csl.services.bl;

import java.util.Objects;

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
import ru.gzpn.spc.csl.services.bl.interfaces.IUserSettigsService;

@Service
@Transactional
public class UserSettigsService implements IUserSettigsService {
	public static final Logger logger = LoggerFactory.getLogger(UserSettigsService.class);
	@Autowired
	private UserSettingsRepository repository;
	@Autowired
	ServerProperties serverProperties;
	
	@Override
	public CreateDocSettingsJson getUserSettings() {
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
	public CreateDocSettingsJson getUserSettings(String userId) {
		UserSettings userSettings = repository.findByUserId(userId);
		CreateDocSettingsJson result = null;
		
		if (userSettings != null) {
			result = userSettings.getCreateDocSettingsJson();
			// JSON field is empty
			if (Objects.isNull(result)) {
				result = new CreateDocSettingsJson();
			}
		// no such user
		} else {
			result = new CreateDocSettingsJson();
		}
		
		return result;
	}
	
	@Override
	public void saveCreateDocSettings(String userId, CreateDocSettingsJson createDoc) {
		UserSettings userSettings = repository.findByUserId(userId);
		
		if (userSettings != null) {
			userSettings.setCreateDocSettingsJson(createDoc);
		} else {
			userSettings = new UserSettings();
			userSettings.setUserId(userId);
			userSettings.setCreateDocSettingsJson(createDoc);
		}
	
		repository.save(userSettings);
	}
}
