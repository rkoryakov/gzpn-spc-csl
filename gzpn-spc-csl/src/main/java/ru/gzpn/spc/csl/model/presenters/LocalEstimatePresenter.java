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
	public String getChangedBy() {
		String result = super.getChangedBy();
		if (result == null) {
			result = "";
		}
		
		return result;
	}
	
	@Override
	public ILocalEstimate getLocalEstimate() {
		localEstimate.setName(this.getName());
		localEstimate.setCode(this.getCode());
		localEstimate.setChangedBy(this.getChangedBy());
		localEstimate.setDrawing(this.getDrawing());
		localEstimate.setStatus(this.getStatus());
		localEstimate.setComment(this.getComment());
		localEstimate.setDocument(this.getDocument());
		localEstimate.setStage(this.getStage());
		localEstimate.setEstimateCalculation(this.getEstimateCalculation());
		localEstimate.setObjectEstimate(this.getObjectEstimate());
		localEstimate.setEstimateHead(this.getEstimateHead());
		
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
		String result = "";
		if (getEstimateCalculation() != null) {
			result = getEstimateCalculation().getName();
		}
		return result;
	}
	
	@Override
	public String getObjectEstimateCaption() {
		String result = "";
		if (getObjectEstimate() != null) {
			result = getObjectEstimate().getName();
		}
		return result;
	}
	
	@Override
	public String getEstimateHeadCaption() {
		String result = "";
		if (getEstimateHead() != null) {
			result = getEstimateHead().getName();
		}
		return result;
	}
}
