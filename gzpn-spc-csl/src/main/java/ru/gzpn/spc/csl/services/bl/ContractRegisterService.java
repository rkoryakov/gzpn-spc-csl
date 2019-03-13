package ru.gzpn.spc.csl.services.bl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import ru.gzpn.spc.csl.services.bl.interfaces.IContractRegisterService;
import ru.gzpn.spc.csl.services.bl.interfaces.IContractService;
import ru.gzpn.spc.csl.services.bl.interfaces.IProcessService;
import ru.gzpn.spc.csl.services.bl.interfaces.IUserSettigsService;

@Service
public class ContractRegisterService implements IContractRegisterService {

	@Autowired
	private IContractService contractService;
	@Autowired
	private IUserSettigsService userSettingsService;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private IProcessService processService;
	
	@Override
	public IContractService getContractService() {
		return contractService;
	}

	@Override
	public MessageSource getMessageSource() {
		return messageSource;
	}

	@Override
	public IUserSettigsService getUserSettingsService() {
		return userSettingsService;
	}

	@Override
	public IProcessService getProcessService() {
		return processService;
	}

}
