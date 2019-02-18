package ru.gzpn.spc.csl.services.bl.interfaces;

public interface ISummaryEstimateCardService extends IUIService {
	ILocalEstimateService getLocalEstimateService();
	IEstimateCalculationService getEstimateCalculationService();
	IProjectService getProjectService();
}
