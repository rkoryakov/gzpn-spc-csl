package ru.gzpn.spc.csl.model.presenters;

import java.time.format.DateTimeFormatter;

import ru.gzpn.spc.csl.model.LocalEstimate;
import ru.gzpn.spc.csl.model.interfaces.ILocalEstimate;
import ru.gzpn.spc.csl.model.presenters.interfaces.ILocalEstimatePresenter;

@SuppressWarnings("serial")
public class LocalEstimatePresenter extends LocalEstimate implements ILocalEstimatePresenter {
	
	private ILocalEstimate localEstimate;

	public LocalEstimatePresenter(ILocalEstimate iLocalEstimate) {
		this.setId(iLocalEstimate.getId());
		this.setName(iLocalEstimate.getName());
		this.setCode(iLocalEstimate.getCode());
		this.setChangedBy(iLocalEstimate.getChangedBy());
		this.setDrawing(iLocalEstimate.getDrawing());
		this.setStatus(iLocalEstimate.getStatus());
		this.setComment(iLocalEstimate.getComment());
		this.setDocument(iLocalEstimate.getDocument());
		this.setStage(iLocalEstimate.getStage());
		this.setEstimateCalculation(iLocalEstimate.getEstimateCalculation());
		this.setObjectEstimate(iLocalEstimate.getObjectEstimate());
		this.setEstimateHead(iLocalEstimate.getEstimateHead());
		this.setCreateDate(iLocalEstimate.getCreateDate());
		this.setChangeDate(iLocalEstimate.getChangeDate());
		this.setVersion(iLocalEstimate.getVersion());
		this.localEstimate = iLocalEstimate;
	}
	
	@Override
	public ILocalEstimate getLocalEstimate() {
		return localEstimate;
	}

	@Override
	public void setLocalEstimate(ILocalEstimate localEstimate) {
		this.localEstimate = localEstimate;
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
	public String getDocumentCaption() {
		return getDocument().getName();
	}
	
	@Override
	public String getStageCaption() {
		return getStage().getName();
	}
	
	@Override
	public String getEstimateCalculationCaption() {
		return getEstimateCalculation().getName();
	}
	
	@Override
	public String getObjectEstimateCaption() {
		return getObjectEstimate().getName();
	}
	
	@Override
	public String getEstimateHeadCaption() {
		return getEstimateHead().getName();
	}
}
