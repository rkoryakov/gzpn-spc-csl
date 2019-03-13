package ru.gzpn.spc.csl.ui.js.bpmnio;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.ui.AbstractJavaScriptComponent;
import com.vaadin.ui.Notification;


@SuppressWarnings("serial")
@StyleSheet({"diagram-js.css", 
	 		 "bpmn.css", 
	 		 "bpmn_connector.css"})
@JavaScript({"bpmn-navigated-viewer.development.js", "jquery.min.js", "bpmn_viewer_connector.js"})
public class BpmnViewer extends AbstractJavaScriptComponent implements BpmnHighlightRpc {
	public static final Logger logger = LoggerFactory.getLogger(BpmnViewer.class);
	public BpmnViewer() {
		registerRpc(new BpmnOverElementRpc() {
			@Override
			public void onElementOver(String elementId) {
				logger.debug("elementId = {}", elementId);
				highlight(elementId, getElementInfo(elementId));
				logger.debug("getElementInfo(elementId) = {}", getElementInfo(elementId));
				logger.debug("getState().getElementInfos() = {}", getState().getElementInfos());
			}
		});
		
		registerRpc(new BpmnClickElementRpc() {

			@Override
			public void onElementClick(String elementId) {
				Notification.show("BPMN element with id = " + elementId + " was clicked");
			}
			
		});
		setSizeFull();
	}
	
	public ElementInfo getElementInfo(String elementId) {
		return getState().getElementInfos().stream()
					.filter(item -> item.elementId.equals(elementId))
						.findFirst().orElse(null);
	}
	public void setElementInfos(List<ElementInfo> elements) {
		getState().getElementInfos().clear();
		getState().getElementInfos().addAll(elements);
	}
	
	public void setBpmnXml(String xml) {
		getState().bpmnXML = xml;
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
