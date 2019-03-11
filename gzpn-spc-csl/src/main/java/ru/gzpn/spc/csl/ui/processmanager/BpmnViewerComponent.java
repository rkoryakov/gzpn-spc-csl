package ru.gzpn.spc.csl.ui.processmanager;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

import ru.gzpn.spc.csl.services.bl.interfaces.IProcessManagerService;
import ru.gzpn.spc.csl.ui.js.bpmnio.BpmnState.ElementInfo;
import ru.gzpn.spc.csl.ui.js.bpmnio.BpmnViewer;
/**
 * Process instance viewer
 * 
 * */
@SuppressWarnings("serial")
public class BpmnViewerComponent extends AbstractBpmnViewer {
	public static final Logger logger = LoggerFactory.getLogger(BpmnModelerComponent.class);
	private BpmnViewer bpmnViewer;

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
		bpmnViewer = new BpmnViewer();
		return bpmnViewer;
	}

	public void updateBpmnViewer(ProcessInstance processInstance) {
		
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
			bpmnViewer.setElementInfos(getProcessElementInfos());
			
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	private List<ElementInfo> getProcessElementInfos() {
		List<ElementInfo> result = java.util.Collections.emptyList();
		TaskService taskService = service.getProcessService().getProcessEngine().getTaskService();
		HistoryService historyService = service.getProcessService().getProcessEngine().getHistoryService();
		
		/* active or suspended tasks */
		List<Task> tasks = taskService.createTaskQuery().list();
		for (Task task : tasks) {
			ElementInfo info = new ElementInfo(task.getId());
			info.setUser(task.getAssignee());
			info.setCompleted(false);
			info.setStatus(task.getAssignee() != null ? "In Progress" : "Waiting for assignment");
			info.setActive(true);
			
			if (task.isSuspended()) {
				info.setStatus("Suspended");
			}
			result.add(info);
		}
		
		/* completed tasks */
		List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery().finished().list();
		for (HistoricTaskInstance hTask : historicTaskInstances) {
			ElementInfo info = new ElementInfo(hTask.getId());
			info.setActive(false);
			info.setCompleted(true);
			info.setUser(hTask.getAssignee());
			info.setStatus("Completed");
			result.add(info);
		}
		return result;
	}
}
