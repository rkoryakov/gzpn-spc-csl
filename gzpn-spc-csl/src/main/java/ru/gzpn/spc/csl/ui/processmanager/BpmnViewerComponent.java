package ru.gzpn.spc.csl.ui.processmanager;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.activiti.engine.TaskService;
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

	public BpmnViewerComponent(IProcessManagerService service, ProcessInstance processInstance) {
		super(service, processInstance);
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

	public Component createBpmnViewer() {
		bpmnViewer = new BpmnViewer();
		
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
		
		return bpmnViewer;
	}

	private List<ElementInfo> getProcessElementInfos() {
		List<ElementInfo> result = java.util.Collections.emptyList();
		TaskService taskService = service.getProcessService().getProcessEngine().getTaskService(); 
		List<Task> activeTasks = taskService.createTaskQuery().active().list();
		
		return result;
	}
}
