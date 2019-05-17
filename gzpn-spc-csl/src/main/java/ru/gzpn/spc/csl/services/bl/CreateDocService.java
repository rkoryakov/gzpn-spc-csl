package ru.gzpn.spc.csl.services.bl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import ru.gzpn.spc.csl.services.bl.interfaces.ICreateDocService;
import ru.gzpn.spc.csl.services.bl.interfaces.IDocumentService;
import ru.gzpn.spc.csl.services.bl.interfaces.IProcessService;
import ru.gzpn.spc.csl.services.bl.interfaces.IProjectService;
import ru.gzpn.spc.csl.services.bl.interfaces.IUserSettingsService;
import ru.gzpn.spc.csl.services.bl.interfaces.IWorkSetService;

@Service
public class CreateDocService implements ICreateDocService {

	@Autowired
	private IProjectService projectService;
	@Autowired
	private IUserSettingsService userSettingsService;
	@Autowired
	private IWorkSetService workSetService;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private IDocumentService documentService;
	@Autowired
	private IProcessService processService;
	
	@Override
	public IProjectService getProjectService() {
		return projectService;
	}

	@Override
	public IUserSettingsService getUserSettingsService() {
		return userSettingsService;
	}

	@Override
	public IWorkSetService getWorkSetService() {
		return workSetService;
	}

	@Override
	public MessageSource getMessageSource() {
		return messageSource;
	}

	@Override
	public IDocumentService getDocumentService() {
		return documentService;
	}
	
	@Override
	public IProcessService getProcessService() {
		return processService;
	}
}
