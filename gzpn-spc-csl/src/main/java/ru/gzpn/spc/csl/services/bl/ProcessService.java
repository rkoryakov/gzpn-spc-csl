package ru.gzpn.spc.csl.services.bl;

import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.gzpn.spc.csl.services.bl.interfaces.IProcessService;

@Service
@Transactional
public class ProcessService implements IProcessService {

	@Autowired
	ProcessEngine processEngine;
	
	@Autowired
	RuntimeService runtimeService;
	
	@Autowired
	TaskService taskService;
	
	@Override
	public ProcessEngine getProcessEngine() {
		return processEngine;
	}

	@Override
	public ProcessInstance startEstimateAccountingProcess(Map<String, Object> processVariables) {
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("EstimateAccounting", processVariables);
		
		return processInstance;
	}
}
