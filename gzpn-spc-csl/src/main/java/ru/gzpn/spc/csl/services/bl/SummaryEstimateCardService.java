package ru.gzpn.spc.csl.services.bl;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.gzpn.spc.csl.services.bl.interfaces.IEstimateCalculationService;
import ru.gzpn.spc.csl.services.bl.interfaces.IEstimateCostService;
import ru.gzpn.spc.csl.services.bl.interfaces.ILocalEstimateService;
import ru.gzpn.spc.csl.services.bl.interfaces.IProcessService;
import ru.gzpn.spc.csl.services.bl.interfaces.IProjectService;
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
	@Autowired
	private IEstimateCalculationService estimateCalculationService;
	@Autowired
	private IProjectService projectService;
	@Autowired
	private IProcessService processService;
	@Autowired 
	private IEstimateCostService estimateCostService;
	
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

	@Override
	public IEstimateCalculationService getEstimateCalculationService() {
		return estimateCalculationService;
	}

	@Override
	public IProjectService getProjectService() {
		return projectService;
	}

	@Override
	public IProcessService getProcessService() {
		return processService;
	}

	@Override
	public IEstimateCostService getEstimateCostService() {
		return estimateCostService;
	}
}
