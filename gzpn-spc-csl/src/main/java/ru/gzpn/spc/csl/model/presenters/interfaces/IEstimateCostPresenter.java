package ru.gzpn.spc.csl.model.presenters.interfaces;

import ru.gzpn.spc.csl.model.interfaces.IEstimateCost;
import ru.gzpn.spc.csl.model.interfaces.ILocalEstimate;

public interface IEstimateCostPresenter extends IEstimateCost {
	ILocalEstimate getLocalEstimate();
	void setLocalEstimate(ILocalEstimate localEstimate);
	
	String getCreateDatePresenter();
	String getChangeDatePresenter();
	String getDocumentCaption();
	String getStageCaption();
	String getEstimateCalculationCaption();
	String getObjectEstimateCaption();
	String getEstimateHeadCaption();
}
