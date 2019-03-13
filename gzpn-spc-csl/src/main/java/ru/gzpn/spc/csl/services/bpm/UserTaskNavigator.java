package ru.gzpn.spc.csl.services.bpm;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vaadin.navigator.Navigator;

import ru.gzpn.spc.csl.model.enums.Role;
import ru.gzpn.spc.csl.services.bl.interfaces.IUserSettigsService;
import ru.gzpn.spc.csl.ui.views.TaskAlreadyAssignedView;

@Service
public class UserTaskNavigator implements IUserTaskNavigator {
	public static final Logger logger = LoggerFactory.getLogger(UserTaskNavigator.class);
	
	@Autowired
	TaskService taskService;
	@Autowired 
	IUserSettigsService settingsService;
	
	
	@Override
	public void navigate(Navigator navigator, String taskId) {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		
		String viewId = task.getFormKey();
		Role taskRole = Role.getRoleByTaskDefinition(task.getTaskDefinitionKey());

		synchronized (this) {
			/*  Has the task already been opened by another user? Or the came user try to open it again  */
			if (task.getAssignee() == null || settingsService.getCurrentUser().equals(task.getAssignee()) ) {
				navigator.navigateTo(viewId + "/taskId=" + taskId);
				taskService.claim(taskId, settingsService.getCurrentUser());
			} else {
				navigator.navigateTo(TaskAlreadyAssignedView.NAME + "/assignee=" + task.getAssignee() + "&duedate=" + task.getDueDate());
			}
		}
	}
}
