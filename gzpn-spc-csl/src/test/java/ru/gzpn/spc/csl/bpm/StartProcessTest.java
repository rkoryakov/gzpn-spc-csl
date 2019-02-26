package ru.gzpn.spc.csl.bpm;

import static org.assertj.core.api.Assertions.assertThat;

import org.activiti.engine.IdentityService;
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
	public static final Logger logger = LoggerFactory.getLogger(StartProcessTest.class);
	@Autowired
	RuntimeService runtimeService;
	@Autowired 
	TaskService taskService;
	@Autowired 
	IdentityService identityService;
	
	
	@Test
	public void startPocessTest() throws InterruptedException {
		runtimeService.addEventListener(new MyEventListener(taskService));
		identityService.setAuthenticatedUserId("user");
		ProcessInstance instance = runtimeService.startProcessInstanceByKey("EstimateAccounting");
		
		taskService.createTaskQuery().taskCandidateGroup("users").list().forEach(task -> {
			logger.debug("[startPocessTest] claim task {}", task.getId());
			taskService.setAssignee(task.getId(), "user");
			taskService.setOwner(task.getId(), "user");
			taskService.claim(task.getId(), "user");
		});
		
		logger.debug("[startPocessTest] taskAssignee(\"user\") = {}", taskService.createTaskQuery().taskAssignee("user").count());
		 taskService.createTaskQuery().taskAssignee("user").list().forEach(task -> {
			 taskService.complete(task.getId());
			 logger.debug("[startPocessTest] task.getId() = {} is completed", task.getId());
		 });
		 
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

	      case TASK_ASSIGNED:
	    	  ActivitiEntityEvent ve = (ActivitiEntityEvent) event;
	    	  TaskEntity task = (TaskEntity) ve.getEntity();
	    	  logger.debug("Task assigned to {}", task.getOwner());
	    	  break;
	      case TASK_CREATED:
	    	  ve = (ActivitiEntityEvent) event;
	    	  task = (TaskEntity) ve.getEntity();
	    	  String taskDef = task.getTaskDefinitionKey();
	    	  String taskName = task.getName();
	    	  String taskId = task.getId();
	    	  String piid = task.getProcessInstanceId();
	    	  String processsName = task.getProcessDefinitionId();
	    	  String formKey = task.getFormKey();
	    	  logger.debug("Task created, taskDef: {}, taskName: {}, taskId: {}, piid: {}, processsName: {}, formKey: {}, owner: {}, candidates: {}", 
	    			  taskDef, taskName, taskId, piid, processsName, formKey,  task.getOwner(), task.getCandidates());
	    	  //List<Task> tasks = taskService.gettascreateTaskQuery().processDefinitionKey("EstimateAccounting").list();
	    	 // tasks.stream().forEach(task -> logger.debug("Task {}", task.getName()));
	    	 // logger.debug("Task created TaskDefinitionKey: {}, Name: {}", task.getTaskDefinitionKey(), task.getName());
	    	 // taskService.claim(taskId, "user");
	    	  //taskService.complete(taskId);
	    	 
	    	  logger.debug("Task variables {}", taskService.getVariableInstancesLocal(taskId));
	      default:
	    	  //logger.debug("Event received: " + event.getType());
	    }
	  }

	  @Override
	  public boolean isFailOnException() {
	    // The logic in the onEvent method of this listener is not critical, exceptions
	    // can be ignored if logging fails...
	    return true;
	  }

}