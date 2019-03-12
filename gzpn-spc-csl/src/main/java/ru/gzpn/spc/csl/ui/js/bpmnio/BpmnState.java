package ru.gzpn.spc.csl.ui.js.bpmnio;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.shared.ui.JavaScriptComponentState;

@SuppressWarnings("serial")
public class BpmnState extends JavaScriptComponentState {
	public String bpmnXML = "";
	public List<ElementInfo> elementInfos = new ArrayList<>();
	
	
	public String getBpmnXML() {
		return bpmnXML;
	}
//	public void setBpmnXML(String bpmnXML) {
//		this.bpmnXML = bpmnXML;
//	}

	public List<ElementInfo> getElementInfos() {
		return elementInfos;
	}
//	public void setElementInfos(List<ElementInfo> elementInfos) {
//		this.elementInfos = elementInfos;
//	}

}

