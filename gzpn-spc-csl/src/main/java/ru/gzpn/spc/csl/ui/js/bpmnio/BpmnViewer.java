package ru.gzpn.spc.csl.ui.js.bpmnio;

import java.util.List;

import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.shared.communication.ClientRpc;
import com.vaadin.shared.communication.ServerRpc;
import com.vaadin.ui.AbstractJavaScriptComponent;
import com.vaadin.ui.Notification;

import ru.gzpn.spc.csl.ui.js.bpmnio.BpmnState.ElementInfo;

@SuppressWarnings("serial")
@StyleSheet({"diagram-js.css", 
	 		 "bpmn.css", 
	 		 "bpmn_connector.css"})
@JavaScript({"bpmn-viewer.development.js", "jquery.min.js", "bpmn_viewer_connector.js"})
public class BpmnViewer extends AbstractJavaScriptComponent implements BpmnHighlightRpc {
	
	public BpmnViewer() {
		registerRpc(new BpmnClickElementRpc() {
			@Override
			public void onElementClick(String elementId) {
				highlight(elementId, getElementInfo(elementId));
				Notification.show("BPMN element with id = " + elementId + " was clicked");
			}
		});
		setSizeFull();
	}
	
	protected ElementInfo getElementInfo(String elementId) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setBpmnXml(String xml) {
		getState().bpmnXML = xml;
	}
	
	public void setElementInfos(List<ElementInfo> elements) {
		getState().elementInfos = elements;
	}
	
	@Override
	protected BpmnState getState() {
		return (BpmnState)super.getState();
	}

	@Override
	public void highlight(String elementId, ElementInfo info) {
		getRpcProxy(BpmnHighlightRpc.class).highlight(elementId, info);
	}
}

interface BpmnClickElementRpc extends ServerRpc {
	void onElementClick(String id);
}

interface BpmnHighlightRpc extends ClientRpc {
	void highlight(String id, ElementInfo info);
}