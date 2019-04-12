package ru.gzpn.spc.csl.services.bl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import ru.gzpn.spc.csl.services.bl.interfaces.IContractRegisterService;
import ru.gzpn.spc.csl.services.bl.interfaces.IContractCardService;
import ru.gzpn.spc.csl.services.bl.interfaces.IProcessService;
import ru.gzpn.spc.csl.services.bl.interfaces.IUserSettingsService;

@Service
public class ContractRegisterService implements IContractRegisterService {

	@Autowired
	private IContractCardService contractService;
	@Autowired
	private IUserSettingsService userSettingsService;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private IProcessService processService;
	
	@Override
	public IContractCardService getContractService() {
		return contractService;
	}

	@Override
	public MessageSource getMessageSource() {
		return messageSource;
	}

	@Override
	public IUserSettingsService getUserSettingsService() {
		return userSettingsService;
	}

	@Override
	public IProcessService getProcessService() {
		return processService;
	}

}
