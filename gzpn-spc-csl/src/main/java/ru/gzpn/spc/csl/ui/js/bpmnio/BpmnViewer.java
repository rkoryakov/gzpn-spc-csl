package ru.gzpn.spc.csl.ui.js.bpmnio;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.event.Action;
import com.vaadin.ui.AbstractJavaScriptComponent;


@SuppressWarnings("serial")
@StyleSheet({"diagram-js.css", 
	 		 "bpmn.css", 
	 		 "bpmn_connector.css"})
@JavaScript({"bpmn-navigated-viewer.development.js", "jquery.min.js", "bpmn_viewer_connector.js"})
public class BpmnViewer extends AbstractJavaScriptComponent implements BpmnHighlightRpc {
	public static final Logger logger = LoggerFactory.getLogger(BpmnViewer.class);

	public static final Action ELEMENT_CLICK_ACTION = new Action("elementClickAction");
	
	protected Map<Action, Set<Listener>> listeners;
	
	public BpmnViewer() {
		initEventActions();
		
		registerRpc(new BpmnOverElementRpc() {
			@Override
			public void onElementOver(String elementId) {
				//logger.debug("elementId = {}", elementId);
				highlight(elementId, getElementInfo(elementId));
				
				//logger.debug("getElementInfo(elementId) = {}", getElementInfo(elementId));
				//logger.debug("getState().getElementInfos() = {}", getState().getElementInfos());
			}
		});
		
		registerRpc(new BpmnClickElementRpc() {
			@Override
			public void onElementClick(String elementId) {
				//BpmnViewer.this.onElementClick();
				//Notification.show("BPMN element with id = " + elementId + " was clicked");
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
	
	
	protected void onElementClick(String elementId) {
		handleAction(ELEMENT_CLICK_ACTION, elementId);
	}
	
	protected void initEventActions() {
		listeners = new HashMap<>();
		listeners.put(ELEMENT_CLICK_ACTION, new HashSet<>());
	}
	
	protected void handleAction(Action action, String elementId) {
		for (Listener listener : listeners.get(action)) {
		//	listener.componentEvent(new EventObject(elementId));
		}
	}
	
	public void addOnElementClickListener(Listener listener) {
		listeners.get(ELEMENT_CLICK_ACTION).add(listener);
	}

	public void setElementClickListener(Set<Listener> elementClickListeners) {
		listeners.put(ELEMENT_CLICK_ACTION, elementClickListeners);
	}
}
