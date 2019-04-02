package ru.gzpn.spc.csl.ui.common.data.imp;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ru.gzpn.spc.csl.model.EstimateCost;
import ru.gzpn.spc.csl.model.LocalEstimate;
import ru.gzpn.spc.csl.model.Stage;
import ru.gzpn.spc.csl.model.enums.PriceLevel;
import ru.gzpn.spc.csl.model.interfaces.IEstimateCost;

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
			LocalEstimate le = new LocalEstimate();
			result = Optional.of(le);
			le.setDrawing(getDrawing(row).orElse(null));
			le.setCode(getCode(row).get());
			le.setName(getName(row).orElse(null));
			
			EstimateCost estimateCost = new EstimateCost();
			estimateCost.setOzp(getOzp(row).orElse(null));
			estimateCost.setEmm(getEmm(row).orElse(null));
			estimateCost.setZpm(getZpm(row).orElse(null));
			estimateCost.setMatCustomerSupply(getMatCustomerSupply(row).orElse(null));
			estimateCost.setMatContractorSupply(getMatContractorSupply(row).orElse(null));
			estimateCost.setOverhead(getOverhead(row).orElse(null));
			estimateCost.setEstimateProfit(getEstimateProfit(row).orElse(null));
			estimateCost.setDevicesCustomerSupply(getDevicesCustomerSupply(row).orElse(null));
			estimateCost.setDevicesContractorSupply(getDevicesContractorSupply(row).orElse(null));
			estimateCost.setOther(getOther(row).orElse(null));
			estimateCost.setPriceLevel(PriceLevel.getByText(getPriceLevel(row).orElse(null)));
			List<IEstimateCost> costs = new ArrayList<>();
			costs.add(estimateCost);
			le.setEstimateCosts(costs);
			
			Stage stage = new Stage();
			stage.setCode(getStageCode(row).orElse(null));
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
	
	public Optional<String> getStageCode(int row) {
		return getCellTextValue(row, 3);
	}
	
	public Optional<BigDecimal> getOzp(int row) {
		return getCellNumValue(row, 4);
	}
	
	public Optional<BigDecimal> getEmm(int row) {
		return getCellNumValue(row, 5);
	}
	
	public Optional<BigDecimal> getZpm(int row) {
		return getCellNumValue(row, 6);
	}
	
	public Optional<BigDecimal> getMatCustomerSupply(int row) {
		return getCellNumValue(row, 7);
	}
	
	public Optional<BigDecimal> getMatContractorSupply(int row) {
		return getCellNumValue(row, 8);
	}
	
	public Optional<BigDecimal> getOverhead(int row) {
		return getCellNumValue(row, 9);
	}
	
	public Optional<BigDecimal> getEstimateProfit(int row) {
		return getCellNumValue(row, 10);
	}
	
	public Optional<BigDecimal> getDevicesCustomerSupply(int row) {
		return getCellNumValue(row, 11);
	}
	
	public Optional<BigDecimal> getDevicesContractorSupply(int row) {
		return getCellNumValue(row, 12);
	}
	
	public Optional<BigDecimal> getOther(int row) {
		return getCellNumValue(row, 13);
	}
	
	public Optional<String> getPriceLevel(int row) {
		return getCellTextValue(row, 14);
	}
	
	@Override
	public int getTotalAmount() {
		return getLastRowNumber() + 1;
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
