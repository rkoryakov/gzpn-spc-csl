package ru.gzpn.spc.csl.services.bl.interfaces;

import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.runtime.ProcessInstance;

public interface IProcessService {
	public ProcessInstance startEstimateAccountingProcess(Map<String, Object> processVariables);
	public ProcessEngine getProcessEngine();
	
}
