package ru.gzpn.spc.csl.model.presenters.interfaces;

import ru.gzpn.spc.csl.model.interfaces.IObjectEstimate;

public interface IObjectEstimatePresenter extends IObjectEstimate  {
	
	IObjectEstimate getObjectEstimate();
	void setObjectEstimate(IObjectEstimate objectEstimate);
	
	String getCreateDatePresenter();
	String getChangeDatePresenter();
	String getSalariesFundsPresenter();
	String getStagePresenter();
	String getOtherPresenter();
	String getDevicesPresenter();
	String getSMRPresenter();
	String getTotalPresenter();
}
