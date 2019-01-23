package ru.gzpn.spc.csl.ui.estimatereg;

import ru.gzpn.spc.csl.model.interfaces.IEstimateCalculation;

public interface IEstimateCalculationPresenter extends IEstimateCalculation {

	public IEstimateCalculation getEstimateCalculation();
	public void setEstimateCalculation(IEstimateCalculation estimateCalculation);
	public String getCreateDatePresenter();
	public String getChangeDatePresenter();
	String getCProjectCaption();
}
