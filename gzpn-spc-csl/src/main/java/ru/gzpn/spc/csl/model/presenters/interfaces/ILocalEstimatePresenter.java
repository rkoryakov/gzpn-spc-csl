package ru.gzpn.spc.csl.model.presenters.interfaces;

import ru.gzpn.spc.csl.model.interfaces.ILocalEstimate;

public interface ILocalEstimatePresenter extends ILocalEstimate {

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
