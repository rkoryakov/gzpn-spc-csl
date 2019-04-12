package ru.gzpn.spc.csl.services.bl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import ru.gzpn.spc.csl.services.bl.interfaces.IProcessManagerService;
import ru.gzpn.spc.csl.services.bl.interfaces.IProcessService;
import ru.gzpn.spc.csl.services.bl.interfaces.IProjectService;
import ru.gzpn.spc.csl.services.bl.interfaces.IUserSettingsService;

@Service
public class ProcessManagerService implements IProcessManagerService {

	@Autowired
	private MessageSource messageSource;
	@Autowired
	private IUserSettingsService userSettingsService;
	@Autowired
	private IProcessService processService;
	@Autowired
	private IProjectService projectService;
	
	@Override
	public MessageSource getMessageSource() {
		return this.messageSource;
	}

	@Override
	public IUserSettingsService getUserSettingsService() {
		return this.userSettingsService;
	}

	@Override
	public IProcessService getProcessService() {
		return this.processService;
	}
	
	@Override
	public IProjectService getProjectService() {
		return projectService;
	}
}
