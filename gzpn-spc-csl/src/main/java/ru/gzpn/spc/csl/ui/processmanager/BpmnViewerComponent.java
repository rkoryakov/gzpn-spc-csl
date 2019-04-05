package ru.gzpn.spc.csl.ui.processmanager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

import ru.gzpn.spc.csl.services.bl.interfaces.IProcessManagerService;
import ru.gzpn.spc.csl.ui.js.bpmnio.modeler.ElementInfo;
import ru.gzpn.spc.csl.ui.js.bpmnio.viewer.BpmnViewer;
/**
 * Process instance viewer
 * 
 * */
@SuppressWarnings("serial")
public class BpmnViewerComponent extends AbstractBpmnViewer {
	public static final Logger logger = LoggerFactory.getLogger(BpmnModelerComponent.class);
	private BpmnViewer bpmnViewer;
	private VerticalLayout bpmnViewerLayout;
	
	public BpmnViewerComponent(IProcessManagerService service) {
		super(service);
	}

	public BpmnViewer getBpmnViewer() {
		return bpmnViewer;
	}
	

	@Override
	public VerticalLayout createBodyLayout() {
		VerticalLayout body = new VerticalLayout();
		
		body.setHeight("700px");
		body.setWidth("100%");
		body.setMargin(false);
		body.setSpacing(false);
		
		body.addComponent(createBpmnViewer());
		return body;
	}

	
	private Component createBpmnViewer() {
		bpmnViewerLayout = new VerticalLayout();
		bpmnViewerLayout.setMargin(false);
		bpmnViewerLayout.setSizeFull();
		bpmnViewerLayout.setSpacing(false);
		return bpmnViewerLayout;
	}

	public void updateBpmnViewer(ProcessInstance processInstance) {
		bpmnViewerLayout.removeAllComponents();
		bpmnViewer = new BpmnViewer();
		bpmnViewer.setElementClickListener(getElementClickListeners());
		bpmnViewerLayout.addComponent(bpmnViewer);
		
		ProcessDefinition processDefinition = service.getProcessService().getProcessEngine()
										.getRepositoryService().createProcessDefinitionQuery()
											.processDefinitionId(processInstance.getProcessDefinitionId())
												.singleResult();
		
		InputStream is = service.getProcessService().getProcessEngine()
			.getRepositoryService()
				.getProcessModel(processDefinition.getId());
		
		byte streamData[];
		try {
			streamData = new byte[is.available()];
			is.read(streamData, 0, is.available());
			
			bpmnViewer.setBpmnXml(new String(streamData, "UTF-8"));
			bpmnViewer.setElementInfos(getProcessElementInfos(processInstance));
			
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	public void updateBpmnViewer(HistoricProcessInstance processInstance) {
		bpmnViewerLayout.removeAllComponents();
		bpmnViewer = new BpmnViewer();
		bpmnViewer.setElementClickListener(getElementClickListeners());
		bpmnViewerLayout.addComponent(bpmnViewer);
		
		ProcessDefinition processDefinition = service.getProcessService().getProcessEngine()
										.getRepositoryService().createProcessDefinitionQuery()
											.processDefinitionId(processInstance.getProcessDefinitionId())
												.singleResult();
		
		InputStream is = service.getProcessService().getProcessEngine()
			.getRepositoryService()
				.getProcessModel(processDefinition.getId());
		
		byte streamData[];
		try {
			streamData = new byte[is.available()];
			is.read(streamData, 0, is.available());
			bpmnViewer.setBpmnXml(new String(streamData, "UTF-8"));
			bpmnViewer.setElementInfos(getHistoricProcessElementInfos(processInstance));
			
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	private List<ElementInfo> getProcessElementInfos(ProcessInstance processInstance) {
		List<ElementInfo> result = new ArrayList<>();
		Set<String> definitionKeys = new HashSet<>();
		TaskService taskService = service.getProcessService().getProcessEngine().getTaskService();
		HistoryService historyService = service.getProcessService().getProcessEngine().getHistoryService();
		
		/* active or suspended tasks */
		List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
		for (Task task : tasks) {
			ElementInfo info = new ElementInfo(task.getTaskDefinitionKey());
			
			if (task.getAssignee() != null) {
				info.user = task.getAssignee();
				info.status = "В работе";
				info.openDate = task.getClaimTime();
				info.isActive = true;
			} else {
				info.user = "Не задан";
				info.status = "Ожидает назначения";
				info.isActive = false;
			}
			
			info.createDate = task.getCreateTime();
			info.isCompleted = false;
			info.comment = task.getDescription();
					
			if (task.isSuspended()) {
				info.status = "Приостановлено";
			}
			result.add(info);
			definitionKeys.add(task.getTaskDefinitionKey());
		}
		
		/* completed tasks */
		List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
					.processInstanceId(processInstance.getId()).finished().list();
		for (HistoricTaskInstance hTask : historicTaskInstances) {
			if (!definitionKeys.contains(hTask.getTaskDefinitionKey())) {
				ElementInfo info = new ElementInfo(hTask.getTaskDefinitionKey());
				info.isActive = false;
				info.isCompleted = true;
				info.user = hTask.getAssignee();
				info.status = "Завершено";
				info.createDate = hTask.getCreateTime();
				info.openDate = hTask.getClaimTime();
				info.closeDate = hTask.getEndTime();
				info.comment = hTask.getDescription();
				result.add(info);
				definitionKeys.add(hTask.getTaskDefinitionKey());
			}
		}
		return result;
	}
	
	private List<ElementInfo> getHistoricProcessElementInfos(HistoricProcessInstance processInstance) {
		List<ElementInfo> result = new ArrayList<>();
		HistoryService historyService = service.getProcessService().getProcessEngine().getHistoryService();
		Set<String> definitionKeys = new HashSet<>();
		/* completed tasks */
		List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
					.processInstanceId(processInstance.getId()).finished().list();
		for (HistoricTaskInstance hTask : historicTaskInstances) {
			if (!definitionKeys.contains(hTask.getTaskDefinitionKey())) {
				ElementInfo info = new ElementInfo(hTask.getTaskDefinitionKey());
				info.isActive = false;
				info.isCompleted = true;
				info.user = hTask.getAssignee();
				info.status = "Завершено";
				info.createDate = hTask.getCreateTime();
				info.openDate = hTask.getClaimTime();
				info.closeDate = hTask.getEndTime();
				info.comment = hTask.getDescription();
				result.add(info);
				definitionKeys.add(hTask.getTaskDefinitionKey());
			}
		}
		return result;
	}
	
	public void highlight(String taskId) {
		TaskService taskService = service.getProcessService().getProcessEngine().getTaskService();
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		
		ElementInfo info = new ElementInfo(task.getTaskDefinitionKey());
		if (task.getAssignee() != null) {
			info.user = task.getAssignee();
			info.status = "В работе";
			info.openDate = task.getClaimTime();
		} else {
			info.user = "Не задан";
			info.status = "Ожидает назначения";
			info.isActive = false;
		}
		
		info.createDate = task.getCreateTime();
		info.isCompleted = false;
		info.isActive = true;
		info.comment = task.getDescription();
				
		if (task.isSuspended()) {
			info.status = "Приостановлено";
		}
		
		bpmnViewer.highlight(info.elementId, info);
	}

	public void highlightHistoric(String taskId) {
		HistoryService historyService = service.getProcessService().getProcessEngine().getHistoryService();
		HistoricTaskInstance hTask = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
		
		ElementInfo info = new ElementInfo(hTask.getTaskDefinitionKey());
		info.isActive = false;
		info.isCompleted = true;
		info.user = hTask.getAssignee();
		info.status ="Завершено";
		info.createDate = hTask.getCreateTime();
		info.openDate = hTask.getClaimTime();
		info.closeDate = hTask.getEndTime();
		info.comment = hTask.getDescription();
		
		bpmnViewer.highlight(info.elementId, info);
	}
}
