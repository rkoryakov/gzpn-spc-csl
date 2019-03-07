package ru.gzpn.spc.csl.ui.js.bpmnio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.shared.ui.JavaScriptComponentState;

@SuppressWarnings("serial")
public class BpmnState extends JavaScriptComponentState {
	public String bpmnXML = "";
	public List<ElementInfo> elementInfos = new ArrayList<>();
	
	public static class ElementInfo implements Serializable {
		public String elementId;
		public String user;
		public String role;
		public String status;
		public String comment;
		public boolean isActive;
		public boolean isCompleted;
	}
}

