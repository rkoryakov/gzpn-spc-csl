package ru.gzpn.spc.csl.services.bl;

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

@Service
@Transactional
public class ProcessService implements IProcessService {
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
		return runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION, processVariables);
	}

	class CustomEventListener implements ActivitiEventListener {
		
		@Override
		public void onEvent(ActivitiEvent event) {

			ActivitiEntityEvent activitiEntityEvent = (ActivitiEntityEvent) event;
			TaskEntity taskEntity = (TaskEntity) activitiEntityEvent.getEntity();
			String taskDef = taskEntity.getTaskDefinitionKey();
			String formKey = taskEntity.getFormKey();

			if (!CreateDocView.NAME.equals(formKey)) {
				MessageType messageType = MessageType.getMessageTypeByDefinition(taskDef);
				
				String groupId = Role.getRoleByTaskDefinition(taskDef).name();
				identityService.createUserQuery()
					.memberOfGroup(groupId)
						.list().forEach(user -> {
							InternetAddress address = null;
							try {
								address = new InternetAddress(user.getEmail());
							} catch (AddressException e) {
								e.printStackTrace();
							}
							notificationService.sendMessage(messageType, address, "");
							logger.debug("[ON TASK_CREATED  MAILING ] messageType: {}, user: {}, sendTo: {}", messageType, user, address);
						});
	
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
