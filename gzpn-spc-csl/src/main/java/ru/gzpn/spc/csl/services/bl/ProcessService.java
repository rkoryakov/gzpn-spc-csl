package ru.gzpn.spc.csl.services.bl;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.gzpn.spc.csl.model.enums.MessageType;
import ru.gzpn.spc.csl.model.enums.Role;
import ru.gzpn.spc.csl.model.interfaces.IEstimateCalculation;
import ru.gzpn.spc.csl.model.presenters.interfaces.IDocumentPresenter;
import ru.gzpn.spc.csl.services.bl.interfaces.IEstimateCalculationService;
import ru.gzpn.spc.csl.services.bl.interfaces.IProcessService;
import ru.gzpn.spc.csl.services.bl.interfaces.IUserSettigsService;
import ru.gzpn.spc.csl.services.bpm.ITaskNotificationService;
import ru.gzpn.spc.csl.ui.views.CreateDocView;

@SuppressWarnings("serial")
@Service()
@Transactional
public class ProcessService implements IProcessService, Serializable {
	public static final Logger logger = LoggerFactory.getLogger(ProcessService.class);
	public static final String PROCESS_DEFINITION = "EstimateAccounting";

	@Autowired
	ProcessEngine processEngine;
	@Autowired
	RuntimeService runtimeService;
	@Autowired
	TaskService taskService;
	@Autowired
	IdentityService identityService;
	@Autowired
	ITaskNotificationService notificationService;
	@Autowired
	IUserSettigsService userSettings;
	@Autowired
	IEstimateCalculationService estimateCalculationService;
	
	@PostConstruct
	public void initProcessEngine() {
		runtimeService.addEventListener(new CustomEventListener(), ActivitiEventType.TASK_CREATED);
	}
	
	@Override
	public ProcessEngine getProcessEngine() {
		return processEngine;
	}

	@Override
	public ProcessInstance startEstimateAccountingProcess(Map<String, Object> processVariables) {
	
		ProcessInstance instance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION, processVariables);
		Task task = taskService.createTaskQuery().processInstanceId(instance.getId())
				.taskCandidateGroup(Role.CREATOR_ROLE.name()).singleResult();
	
		String user = (String)processVariables.get(INITIATOR);
		taskService.claim(task.getId(), user);
		
		/* create SSR */
		@SuppressWarnings("unchecked")
		Set<IDocumentPresenter> docs = (Set<IDocumentPresenter>) processVariables.get(DOCUMENTS);
		IEstimateCalculation ssr = estimateCalculationService.createEstimateCalculationByDocuments(docs);
		logger.debug("[startEstimateAccountingProcess] ssr = {}", ssr);
		logger.debug("[startEstimateAccountingProcess] ssrId  = {}", ssr.getId());
		logger.debug("[startEstimateAccountingProcess] user = {}", user);
		
		runtimeService.setVariable(instance.getId(), SSR_ID, ssr.getId());
		runtimeService.setVariable(instance.getId(), CPROJECT_CODE, ssr.getProject().getCode());
		logger.debug("[startEstimateAccountingProcess] ssrId from process  = {}", runtimeService.getVariable(instance.getId(), "ssrId"));
		/* complete */
		taskService.complete(task.getId());		
		
		return instance;
	}

	@Override
	public void completeTask(String taskId) {
		taskService.complete(taskId);
	}
	
	@Override
	public boolean isAssigneeForTask(String taskId, String user) {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		return user.equals(task.getAssignee());
	}
	
	@Override
	public Object getProcessVariableByTaskId(String taskId, String varName) {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		return runtimeService.getVariable(task.getProcessInstanceId(), varName);
	}
	
	@Override
	public Object getProcessVariable(String processInstanceId, String varName) {
		return runtimeService.getVariable(processInstanceId, varName);
	}
	
	@Override
	public void setProcessVariable(String taskId, String varName, Object value) {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		runtimeService.setVariable(task.getProcessInstanceId(), varName, value);
	}
	
	
	
	class CustomEventListener implements ActivitiEventListener {
		
		@Override
		public void onEvent(ActivitiEvent taskCretaeEvent) {

			ActivitiEntityEvent activitiEntityEvent = (ActivitiEntityEvent) taskCretaeEvent;
			TaskEntity taskEntity = (TaskEntity) activitiEntityEvent.getEntity();
			String taskDef = taskEntity.getTaskDefinitionKey();
			String formKey = taskEntity.getFormKey();
			logger.debug("[ON TASK_CREATED FIRE ON taskDef = {}, ProcessInstanceId = {}]", taskDef, taskEntity.getProcessInstanceId());
			
			if (!CreateDocView.NAME.equals(formKey)) {
				MessageType messageType = MessageType.getMessageTypeByDefinition(taskDef);
				Role taskRole = Role.getRoleByTaskDefinition(taskDef);

				String comment = " ";
				if (taskRole == Role.EXPERT_ES_ROLE) {
					comment = (String) runtimeService.getVariable(taskEntity.getProcessInstanceId(),
							IProcessService.COMMENTS);
				} else if (taskRole == Role.APPROVER_NTC_ROLE) {
					comment = (String) runtimeService.getVariable(taskEntity.getProcessInstanceId(),
							IProcessService.COMMENTS);
				}
				
				for (User user : identityService.createUserQuery().memberOfGroup(taskRole.name()).list()) {
					InternetAddress address = null;
					try {
						address = new InternetAddress(user.getEmail());
						logger.debug("[ON TASK_CREATED  MAILING ] messageType: {}, user: {}, sendTo: {}", messageType,
								user, address);
						notificationService.sendMessage(messageType, address, taskEntity.getId(), URLEncoder.encode(comment, "utf-8") );
						
					} catch (AddressException e) {
						logger.error(e.getMessage());
					} catch (UnsupportedEncodingException e) {
						logger.error(e.getMessage());
					}
				}
			}
		}

		@Override
		public boolean isFailOnException() {
			// The logic in the onEvent method of this listener is not critical, exceptions
			// can be ignored if logging fails...
			return true;
		}
	}
}
