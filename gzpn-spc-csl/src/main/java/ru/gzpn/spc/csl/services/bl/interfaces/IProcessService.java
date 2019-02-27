package ru.gzpn.spc.csl.services.bl.interfaces;

import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.runtime.ProcessInstance;

public interface IProcessService {
	/* Process variables */
	public static final String COMMENTS = "comments";
	public static final String DOCUMENTS = "documents";
	public static final String ESTIMATES_FOR_APPROVAL = "estimatesForApproval";

	public static final String SSR_IS_APPROVED = "isSsrApproved";
	
	public ProcessInstance startEstimateAccountingProcess(Map<String, Object> processVariables);
	public ProcessEngine getProcessEngine();
	
}
