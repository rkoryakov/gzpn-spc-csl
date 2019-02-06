package ru.gzpn.spc.csl.services.bl;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.gzpn.spc.csl.services.bl.interfaces.ILocalEstimateService;
import ru.gzpn.spc.csl.services.bl.interfaces.ISummaryEstimateCardService;
import ru.gzpn.spc.csl.services.bl.interfaces.IUserSettigsService;
@Service
@Transactional
public class SummaryEstimateCardService implements ISummaryEstimateCardService {

	@Autowired
	private IUserSettigsService userSettingsService;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private ILocalEstimateService localEstimateService;
	
	@Override
	public MessageSource getMessageSource() {
		return messageSource;
	}

	@Override
	public IUserSettigsService getUserSettingsService() {
		return userSettingsService;
	}

	@Override
	public ILocalEstimateService getLocalEstimateService() {
		return localEstimateService;
	}
}
