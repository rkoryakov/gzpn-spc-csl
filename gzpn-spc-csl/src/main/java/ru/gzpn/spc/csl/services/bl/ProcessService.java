package ru.gzpn.spc.csl.services.bl;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.gzpn.spc.csl.model.enums.MessageType;
import ru.gzpn.spc.csl.model.enums.Role;
import ru.gzpn.spc.csl.services.bl.interfaces.IProcessService;
import ru.gzpn.spc.csl.services.bl.interfaces.IUserSettigsService;
import ru.gzpn.spc.csl.services.bpm.ITaskNotificationService;
import ru.gzpn.spc.csl.ui.views.CreateDocView;

@SuppressWarnings("serial")
@Service
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

	@Override
	public ProcessEngine getProcessEngine() {
		return processEngine;
	}

	@Override
	public ProcessInstance startEstimateAccountingProcess(Map<String, Object> processVariables) {
		runtimeService.addEventListener(new CustomEventListener(), ActivitiEventType.TASK_CREATED);
		/* skip this formal task as it's initiate event */
		taskService.claim(taskEntity.getId(), userSettings.getCurrentUser());
		taskService.complete(taskEntity.getId());
		return runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION, processVariables);
	}

	class CustomEventListener implements ActivitiEventListener {
		
		@Override
		public void onEvent(ActivitiEvent taskCretaeEvent) {

			ActivitiEntityEvent activitiEntityEvent = (ActivitiEntityEvent) taskCretaeEvent;
			TaskEntity taskEntity = (TaskEntity) activitiEntityEvent.getEntity();
			String taskDef = taskEntity.getTaskDefinitionKey();
			String formKey = taskEntity.getFormKey();

			if (CreateDocView.NAME.equals(formKey)) {

				MessageType messageType = MessageType.getMessageTypeByDefinition(taskDef);
				Role taskRole = Role.getRoleByTaskDefinition(taskDef);

				String comment = " ";
				if (taskRole == Role.EXPERT_ES_ROLE) {
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
