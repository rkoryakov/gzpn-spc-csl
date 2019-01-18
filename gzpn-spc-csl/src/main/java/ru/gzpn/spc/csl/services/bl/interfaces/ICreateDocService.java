package ru.gzpn.spc.csl.services.bl.interfaces;

import org.springframework.context.MessageSource;

import ru.gzpn.spc.csl.services.bl.DocumentService;

public interface ICreateDocService {
	public IProjectService getProjectService();
	public IUserSettigsService getUserSettingsService();
	public IWorkSetService getWorkService();
	public MessageSource getMessageSource();
	public DocumentService getDocumentService();
}
