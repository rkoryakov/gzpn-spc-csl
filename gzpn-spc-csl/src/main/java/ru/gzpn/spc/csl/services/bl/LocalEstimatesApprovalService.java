package ru.gzpn.spc.csl.services.bl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.gzpn.spc.csl.services.bl.interfaces.ILocalEstimateService;
import ru.gzpn.spc.csl.services.bl.interfaces.ILocalEstimatesApprovalService;
import ru.gzpn.spc.csl.services.bl.interfaces.IProcessService;
import ru.gzpn.spc.csl.services.bl.interfaces.IUserSettingsService;

@Service
@Transactional
public class LocalEstimatesApprovalService implements ILocalEstimatesApprovalService {
	@Autowired
	private IUserSettingsService userSettingsService;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private ILocalEstimateService localEstimateService;
	@Autowired
	private IProcessService processService;
	
	@Override
	public MessageSource getMessageSource() {
		return messageSource;
	}

	@Override
	public IUserSettingsService getUserSettingsService() {
		return userSettingsService;
	}

	@Override
	public ILocalEstimateService getLocalEstimateService() {
		return localEstimateService;
	}

	@Override
	public IProcessService getProcessService() {
		return processService;
	}
}
