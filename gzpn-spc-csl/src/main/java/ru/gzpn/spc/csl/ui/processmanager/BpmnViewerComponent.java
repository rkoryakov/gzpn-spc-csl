package ru.gzpn.spc.csl.ui.processmanager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
import ru.gzpn.spc.csl.ui.js.bpmnio.BpmnViewer;
import ru.gzpn.spc.csl.ui.js.bpmnio.ElementInfo;
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
		bpmnViewerLayout.setSizeFull();
		return bpmnViewerLayout;
	}

	public void updateBpmnViewer(ProcessInstance processInstance) {
		bpmnViewerLayout.removeAllComponents();
		bpmnViewer = new BpmnViewer();
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
			//bpmnViewer.setElementInfos(getProcessElementInfos());
			
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	private List<ElementInfo> getProcessElementInfos(ProcessInstance processInstance) {
		List<ElementInfo> result = new ArrayList<>();
		TaskService taskService = service.getProcessService().getProcessEngine().getTaskService();
		HistoryService historyService = service.getProcessService().getProcessEngine().getHistoryService();
		
		/* active or suspended tasks */
		List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
		for (Task task : tasks) {
			ElementInfo info = new ElementInfo(task.getTaskDefinitionKey());
			
			if (task.getAssignee() != null) {
				info.user = task.getAssignee();
				info.status = "В работе";
			} else {
				info.user = "На задан";
				info.status = "Ожидает назначения";
			}
			
			info.isCompleted = false;
			info.isActive = true;
			
			if (task.isSuspended()) {
				info.status = "Приостановлено";
			}
			result.add(info);
		}
		
		/* completed tasks */
		List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
					.processInstanceId(processInstance.getId()).finished().list();
		for (HistoricTaskInstance hTask : historicTaskInstances) {
			ElementInfo info = new ElementInfo(hTask.getTaskDefinitionKey());
			info.isActive = false;
			info.isCompleted = true;
			info.user = hTask.getAssignee();
			info.status ="Завершено";
			result.add(info);
		}
		return result;
	}
}
