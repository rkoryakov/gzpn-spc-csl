package ru.gzpn.spc.csl.ui.common.data.imp;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ru.gzpn.spc.csl.model.EstimateCost;
import ru.gzpn.spc.csl.model.LocalEstimate;

public class LocalEstimateExcelParser extends ExcelParser implements IProgress {
	
	protected int processedRow;
	protected Runnable progressListener;
	
	public LocalEstimateExcelParser(InputStream inp) {
		super(inp);
	}

	public List<LocalEstimate> getBeanList(int from, int to) {
		List<LocalEstimate> result = new ArrayList<>();
		return result;
	}
	
	public Optional<LocalEstimate> getBean(int row) {
		Optional<LocalEstimate> result = Optional.empty();
		
		if (getCode(row).isPresent()) {
			EstimateCost estimateCost = new EstimateCost();
			LocalEstimate le = new LocalEstimate();
			le.setDrawing(getDrawing(row).orElse(null));
			le.setCode(getCode(row).get());
			le.setName(getName(row).orElse(null));
			le.setStage(stage);
			processedRow = row;
			progressListener.run();
		}
		return result;
	}
	
	public Optional<String> getDrawing(int row) {
		return getCellTextValue(row, 0);
	}

	public Optional<String> getCode(int row) {
		return getCellTextValue(row, 1);
	}
	
	public Optional<String> getName(int row) {
		return getCellTextValue(row, 2);
	}
	
	@Override
	public int getTotalAmount() {
		return getLastRow() + 1;
	}

	@Override
	public int getProcessed() {
		return processedRow;
	}

	@Override
	public void setOnProcess(Runnable progressListener) {
		this.progressListener = progressListener;
	}
}
