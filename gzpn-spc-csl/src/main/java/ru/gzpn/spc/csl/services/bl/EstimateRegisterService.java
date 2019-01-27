package ru.gzpn.spc.csl.services.bl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import ru.gzpn.spc.csl.services.bl.interfaces.IEstimateCalculationService;
import ru.gzpn.spc.csl.services.bl.interfaces.IEstimateRegisterService;
import ru.gzpn.spc.csl.services.bl.interfaces.IUserSettigsService;

@Service
public class EstimateRegisterService implements IEstimateRegisterService {
	
	@Autowired
	private IEstimateCalculationService estimateCalculationService;
	@Autowired
	private IUserSettigsService userSettingsService;
	@Autowired
	private MessageSource messageSource;
	
	@Override
	public IEstimateCalculationService getEstimateCalculationService() {
		return estimateCalculationService;
	}

	@Override
	public MessageSource getMessageSource() {
		return messageSource;
	}
	
	@Override
	public IUserSettigsService getUserSettingsService() {
		return userSettingsService;
	}

}
