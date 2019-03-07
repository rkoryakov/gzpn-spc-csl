package ru.gzpn.spc.csl.ui.processmanager;

import java.io.IOException;
import java.io.InputStream;

import org.activiti.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

import ru.gzpn.spc.csl.services.bl.ProcessService;
import ru.gzpn.spc.csl.services.bl.interfaces.IProcessManagerService;
import ru.gzpn.spc.csl.ui.js.bpmnio.BpmnModeler;

@SuppressWarnings("serial")
public class BpmnModelerComponent extends AbstractBpmnModelerComponent {
	public static final Logger logger = LoggerFactory.getLogger(BpmnModelerComponent.class);
	private BpmnModeler bpmnModeler;

	public BpmnModelerComponent(IProcessManagerService service) {
		super(service);
	}

	@Override
	public VerticalLayout createBodyLayout() {
		VerticalLayout body = new VerticalLayout();
		
		body.setHeight("700px");
		body.setWidth("100%");
		body.setMargin(false);
		body.setSpacing(false);
		
		body.addComponent(createBpmnModeler());
		return body;
	}

	public Component createBpmnModeler() {
		bpmnModeler = new BpmnModeler();
		ProcessDefinition processDefinition = service.getProcessService().getProcessEngine()
										.getRepositoryService().createProcessDefinitionQuery().processDefinitionKeyLike(ProcessService.PROCESS_DEFINITION).latestVersion().singleResult();
		
		InputStream is = service.getProcessService().getProcessEngine()
			.getRepositoryService()
				.getProcessModel(processDefinition.getId());//getResourceAsStream("772501", "/Users/macbookmacbook/git/gzpn-spc-csl/gzpn-spc-csl/bin/main/processes/estimate_accounting.bpmn20.xml");
		
		byte streamData[];
		try {
			streamData = new byte[is.available()];
			is.read(streamData, 0, is.available());
			bpmnModeler.setBpmnXml(new String(streamData, "UTF-8"));
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		
		return bpmnModeler;
	}
}
