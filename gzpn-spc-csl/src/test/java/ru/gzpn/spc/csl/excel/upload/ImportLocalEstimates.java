package ru.gzpn.spc.csl.excel.upload;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ru.gzpn.spc.csl.Application;
import ru.gzpn.spc.csl.model.repositories.EstimateCostRepository;
import ru.gzpn.spc.csl.services.bl.interfaces.IEstimateCalculationService;
import ru.gzpn.spc.csl.services.bl.interfaces.ILocalEstimateService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class)
public class ImportLocalEstimates {
	
	@Autowired
	ILocalEstimateService localEstimateService;
	
	@Autowired
	IEstimateCalculationService estimateCalculationService;
	
	@Autowired
	EstimateCostRepository estimateCostRepository;
	
	@Test
	public void isFileExist() {
		assertThat(getClass().getClassLoader().getResource("ru/gzpn/spc/csl/excel/upload/Local_Estimates.xls")).isNotNull();
	}
	
	

}
