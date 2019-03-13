package ru.gzpn.spc.csl.services.bl.interfaces;

public interface IProcessManagerService extends IUIService {
	public IProcessService getProcessService();

	IProjectService getProjectService();
}
