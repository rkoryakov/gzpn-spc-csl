package ru.gzpn.spc.csl.services.bl.interfaces;

/**
 * Create Document UI services
 */
public interface ICreateDocService extends IUIService {
	public IProjectService getProjectService();
	public IWorkSetService getWorkSetService();
	public IDocumentService getDocumentService();
}
