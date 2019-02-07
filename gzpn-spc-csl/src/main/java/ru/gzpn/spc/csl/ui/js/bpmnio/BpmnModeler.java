package ru.gzpn.spc.csl.ui.js.bpmnio;

import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.ui.AbstractJavaScriptComponent;

@SuppressWarnings("serial")
@StyleSheet("bpmn_connector.css")
@JavaScript({"bpmn-modeler.development.js", "jquery.min.js", "bpmn_connector.js"})
public class BpmnModeler extends AbstractJavaScriptComponent {
	
	public void setBpmnXml(String xml) {
		getState().bpmnXML = xml;
	}
	
	public void setCurrentTask(String task) {
		getState().currentTask = task;
	}

	@Override
	protected BpmnState getState() {
		return (BpmnState)super.getState();
	}
}
