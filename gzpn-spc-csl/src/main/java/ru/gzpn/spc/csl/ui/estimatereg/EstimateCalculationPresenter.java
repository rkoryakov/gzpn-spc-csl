package ru.gzpn.spc.csl.ui.estimatereg;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

import ru.gzpn.spc.csl.model.EstimateCalculation;
import ru.gzpn.spc.csl.model.interfaces.IEstimateCalculation;

@SuppressWarnings("serial")
public class EstimateCalculationPresenter extends EstimateCalculation implements IEstimateCalculationPresenter {

	private IEstimateCalculation estimateCalculation;
	
	public EstimateCalculationPresenter(IEstimateCalculation iestimateCalculation) {
		this.setId(iestimateCalculation.getId());
		this.setCode(iestimateCalculation.getCode());
		this.setName(iestimateCalculation.getName());
		this.setHandler(iestimateCalculation.getHandler());
		this.setProject(iestimateCalculation.getProject());
		this.setCreateDate(iestimateCalculation.getCreateDate());
		this.setChangeDate(iestimateCalculation.getChangeDate());
		this.setVersion(iestimateCalculation.getVersion());
		this.estimateCalculation = iestimateCalculation;
	}

	@Override
	public IEstimateCalculation getEstimateCalculation() {
		return estimateCalculation;
	}
	
	@Override
	public void setEstimateCalculation(IEstimateCalculation estimateCalculation) {
		this.estimateCalculation = estimateCalculation;
	}

	@Override
	public String getCreateDatePresenter() {
		return DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss").format(this.getCreateDate());
	}
	
	@Override
	public String getChangeDatePresenter() {
		return DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss").format(this.getChangeDate());
	}
	
	@Override
	public String getCProjectCaption() {
		String result = "---";
		if (Objects.nonNull(getProject())) {
			result = getProject().getName();
		}
		return result;
	}
}
