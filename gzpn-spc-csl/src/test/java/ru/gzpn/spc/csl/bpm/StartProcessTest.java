package ru.gzpn.spc.csl.bpm;

import static org.assertj.core.api.Assertions.assertThat;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ru.gzpn.spc.csl.Application;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class)
public class StartProcessTest {

	@Autowired
	RuntimeService runtimeService;
	@Autowired 
	TaskService taskService;
	
	@Test
	public void startPocessTest() throws InterruptedException {
		runtimeService.addEventListener(new MyEventListener(taskService));
		ProcessInstance instance = runtimeService.startProcessInstanceByKey("EstimateAccounting");
		assertThat(instance).isNotNull();
	
		//Thread.sleep(10000);
	}
}

class MyEventListener implements ActivitiEventListener {
	public static final Logger logger = LoggerFactory.getLogger(MyEventListener.class);
	TaskService taskService;
	
	public MyEventListener(TaskService taskService) {
		this.taskService = taskService;
	}
	
	  @Override
	  public void onEvent(ActivitiEvent event) {
	    switch (event.getType()) {

	      case JOB_EXECUTION_SUCCESS:
	    	  logger.debug("A job well done!");
	        break;

	      case JOB_EXECUTION_FAILURE:
	    	  logger.debug("A job has failed...");
	        break;

	      case TASK_CREATED:
	    	  ActivitiEntityEvent ve = (ActivitiEntityEvent) event;
	    	  TaskEntity t = (TaskEntity) ve.getEntity();
	    	  String taskDef = t.getTaskDefinitionKey();
	    	  String taskName = t.getName();
	    	  String taskId = t.getId();
	    	  String piid = t.getProcessInstanceId();
	    	  String processsName = t.getProcessDefinitionId();
	    	  String formKey = t.getFormKey();
	    	  logger.debug("Task taskDef: {}, taskName: {}, taskId: {}, piid: {}, processsName: {}, formKey: {}", taskDef, taskName, taskId, piid, processsName, formKey);
	    	  //List<Task> tasks = taskService.gettascreateTaskQuery().processDefinitionKey("EstimateAccounting").list();
	    	 // tasks.stream().forEach(task -> logger.debug("Task {}", task.getName()));
	    	 // logger.debug("Task created TaskDefinitionKey: {}, Name: {}", task.getTaskDefinitionKey(), task.getName());
	    	 // taskService.claim(taskId, "user");
	    	  //taskService.complete(taskId);
	    	  
	    	  logger.debug("Task variables {}", taskService.getVariableInstancesLocal(taskId));
	      default:
	    	  logger.debug("Event received: " + event.getType());
	    }
	  }

	  @Override
	  public boolean isFailOnException() {
	    // The logic in the onEvent method of this listener is not critical, exceptions
	    // can be ignored if logging fails...
	    return true;
	  }

}