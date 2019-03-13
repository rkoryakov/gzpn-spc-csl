package ru.gzpn.spc.csl.model.presenters;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

import ru.gzpn.spc.csl.model.ObjectEstimate;
import ru.gzpn.spc.csl.model.interfaces.IObjectEstimate;
import ru.gzpn.spc.csl.model.presenters.interfaces.IObjectEstimatePresenter;

@SuppressWarnings("serial")
public class ObjectEstimatePresenter extends ObjectEstimate implements IObjectEstimatePresenter {

	private IObjectEstimate objectEstimate;
	private static DecimalFormat decimalFormat = new DecimalFormat();
	static {
		decimalFormat.setMaximumFractionDigits(2);
		decimalFormat.setMinimumFractionDigits(2);
		decimalFormat.setGroupingUsed(true);
	}
	
	public ObjectEstimatePresenter(IObjectEstimate objectEstimate) {
		this.setChangeDate(objectEstimate.getChangeDate());
		this.setCode(objectEstimate.getCode());
		this.setCreateDate(objectEstimate.getCreateDate());
		this.setDevices(objectEstimate.getDevices());
		this.setEstimateCalculation(objectEstimate.getEstimateCalculation());
		this.setEstimateCosts(objectEstimate.getEstimateCosts());
		this.setEstimateHead(objectEstimate.getEstimateHead());
		this.setId(objectEstimate.getId());
		this.setLocalEstimates(objectEstimate.getLocalEstimates());
		this.setName(objectEstimate.getName());
		this.setOther(objectEstimate.getOther());
		this.setSalariesFunds(objectEstimate.getSalariesFunds());
		this.setSMR(objectEstimate.getSMR());
		this.setStage(objectEstimate.getStage());
		this.setTotal(objectEstimate.getTotal());
		this.setVersion(objectEstimate.getVersion());
	}


	@Override
	public IObjectEstimate getObjectEstimate() {
		return objectEstimate;
	}

	@Override
	public void setObjectEstimate(IObjectEstimate objectEstimate) {
		this.objectEstimate = objectEstimate;
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
	public String getSalariesFundsPresenter() {
		BigDecimal bigDecimal = getSalariesFunds().setScale(2, RoundingMode.HALF_UP);
		return decimalFormat.format(bigDecimal);
	}

	@Override
	public String getStagePresenter() {
		return getStage().getName();
	}

	@Override
	public String getOtherPresenter() {
		BigDecimal bigDecimal = getOther().setScale(2, RoundingMode.HALF_UP);
		return decimalFormat.format(bigDecimal);
	}

	@Override
	public String getDevicesPresenter() {
		BigDecimal bigDecimal = getDevices().setScale(2, RoundingMode.HALF_UP);
		return decimalFormat.format(bigDecimal);
	}

	@Override
	public String getSMRPresenter() {
		BigDecimal bigDecimal = getSMR().setScale(2, RoundingMode.HALF_UP);
		return decimalFormat.format(bigDecimal);
	}

	@Override
	public String getTotalPresenter() {
		BigDecimal bigDecimal = getTotal().setScale(2, RoundingMode.HALF_UP);
		return decimalFormat.format(bigDecimal);
	}

}
