package ru.gzpn.spc.csl.services.bl.interfaces;

import ru.gzpn.spc.csl.services.bl.DocumentService;

/**
 * Create Document UI services
 */
public interface ICreateDocService extends IUIService {
	public IProjectService getProjectService();
	public IWorkSetService getWorkService();
	public DocumentService getDocumentService();
}
