package ru.gzpn.spc.csl.services.bl.interfaces;

import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.runtime.ProcessInstance;

public interface IProcessService {
	/* Process variables */
	public static final String INITIATOR = "initiator";
	public static final String COMMENTS = "comments";
	public static final String DOCUMENTS = "documents";
	public static final String ESTIMATES_FOR_APPROVAL = "estimatesForApproval";
	public static final String SSR_IS_APPROVED = "isSsrApproved";
	public static final String SSR_IS_CREATED = "isSsrCreated";
	public static final String CPROJECT_CODE = "cprojectCode";
	public static final String SSR_ID = "ssrId";
	public static final String CONTRACT_ID = "contractId";
	
	public ProcessInstance startEstimateAccountingProcess(Map<String, Object> processVariables);
	public ProcessEngine getProcessEngine();
	boolean isAssigneeForTask(String taskId, String user);
	Object getProcessVariableByTaskId(String taskId, String varName);
	void setProcessVariable(String taskId, String varName, Object value);
	Object getProcessVariable(String processInstanceId, String varName);
	void completeTask(String taskId);
	
}
