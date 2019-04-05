package ru.gzpn.spc.csl.services.bpm;

import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vaadin.navigator.Navigator;
import com.vaadin.ui.Notification;

import ru.gzpn.spc.csl.services.bl.interfaces.IUserSettigsService;
import ru.gzpn.spc.csl.ui.views.TaskAlreadyAssignedView;
import ru.gzpn.spc.csl.ui.views.TaskAlreadyClosedView;

@Service
public class UserTaskNavigator implements IUserTaskNavigator {
	public static final Logger logger = LoggerFactory.getLogger(UserTaskNavigator.class);
	
	@Autowired
	TaskService taskService;
	@Autowired 
	IUserSettigsService settingsService;
	@Autowired
	HistoryService historyService;
	
	@Override
	public void navigate(Navigator navigator, String taskId) {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		
		if (task != null) {

			String viewId = task.getFormKey();
			/*Role taskRole = Role.getRoleByTaskDefinition(task.getTaskDefinitionKey());*/

			synchronized (this) {
				/*
				 * Has the task already been opened by another user? Or the same user try to
				 * open it again
				 */
				if (task.getAssignee() == null || settingsService.getCurrentUser().equals(task.getAssignee())) {
					navigator.navigateTo(viewId + "/taskId=" + taskId);
					taskService.claim(taskId, settingsService.getCurrentUser());
				} else {
					navigator.navigateTo(TaskAlreadyAssignedView.NAME + "/assignee=" + task.getAssignee() + "&duedate="
							+ task.getDueDate());
				}
			}
		} else {
			/* the task has already been finished */
			HistoricTaskInstance hTask = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
			
			if (hTask != null) {
				navigator.navigateTo(TaskAlreadyClosedView.NAME + "/assignee=" + hTask.getAssignee() + "&duedate="
						+ hTask.getEndTime());
			} else {
				Notification.show("No such task");
			}
		}
	}
}
