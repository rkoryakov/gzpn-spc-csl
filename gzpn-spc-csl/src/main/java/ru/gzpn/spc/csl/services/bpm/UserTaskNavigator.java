package ru.gzpn.spc.csl.services.bpm;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vaadin.navigator.Navigator;

@Service
public class UserTaskNavigator implements IUserTaskNavigator {
	public static final Logger logger = LoggerFactory.getLogger(UserTaskNavigator.class);
	
	@Autowired
	TaskService taskService;
	
	@Override
	public void navigate(Navigator navigator, String taskId) {
		Task task = taskService.createTaskQuery()
						.processDefinitionKey("EstimateAccounting")
							.taskId(taskId).singleResult();
		
	}

}
