package ru.gzpn.spc.csl.ui.js.bpmnio;

import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.ui.AbstractJavaScriptComponent;

@SuppressWarnings("serial")
@StyleSheet({"diagram-js.css", 
			 "bpmn.css", 
			 "bpmn_connector.css"})
@JavaScript({"bpmn-modeler.development.js", "jquery.min.js", "bpmn_modeler_connector.js"})
public class BpmnModeler extends AbstractJavaScriptComponent {
	
	public BpmnModeler() {
		setSizeFull();
	}
	
	public void setBpmnXml(String xml) {
		getState().bpmnXML = xml;
	}

	@Override
	protected BpmnState getState() {
		return (BpmnState)super.getState();
	}
}
