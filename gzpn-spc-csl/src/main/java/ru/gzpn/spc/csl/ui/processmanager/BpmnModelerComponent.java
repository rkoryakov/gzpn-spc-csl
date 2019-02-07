package ru.gzpn.spc.csl.ui.processmanager;

import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

import ru.gzpn.spc.csl.services.bl.interfaces.IUIService;
import ru.gzpn.spc.csl.ui.js.bpmnio.BpmnModeler;

@SuppressWarnings("serial")
public class BpmnModelerComponent extends AbstractBpmnModelerComponent {

	private BpmnModeler bpmnModeler;

	public BpmnModelerComponent(IUIService service) {
		super(service);
	}

	@Override
	public VerticalLayout createBodyLayout() {
		VerticalLayout body = new VerticalLayout();
		body.setSizeFull();
		body.setMargin(false);
		body.setSpacing(false);
		
		body.addComponent(createBpmnModeler());
		return body;
	}

	public Component createBpmnModeler() {
		bpmnModeler = new BpmnModeler();
		bpmnModeler.setBpmnXml("TODO");
		return bpmnModeler;
	}
}
