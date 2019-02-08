package ru.gzpn.spc.csl.services.bl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import ru.gzpn.spc.csl.services.bl.interfaces.IProcessManagerService;
import ru.gzpn.spc.csl.services.bl.interfaces.IProcessService;
import ru.gzpn.spc.csl.services.bl.interfaces.IUserSettigsService;

@Service
public class ProcessManagerService implements IProcessManagerService {

	@Autowired
	private MessageSource messageSource;
	@Autowired
	private IUserSettigsService userSettingsService;
	@Autowired
	private IProcessService processService;

	@Override
	public MessageSource getMessageSource() {
		return this.messageSource;
	}

	@Override
	public IUserSettigsService getUserSettingsService() {
		return this.userSettingsService;
	}

	@Override
	public IProcessService getProcessService() {
		return this.processService;
	}
}
