package ru.gzpn.spc.csl.services.bl.interfaces;

import org.springframework.context.MessageSource;

/**
 * Base common service for each UI 
 * see the {@link ICreateDocService}
 */
public interface IUIService {
	public MessageSource getMessageSource();
	public IUserSettingsService getUserSettingsService();
	IProcessService getProcessService();
}
