package ru.gzpn.spc.csl.ui.js.bpmnio;

import java.io.Serializable;
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

	public static class ElementInfo implements Serializable {
		public String elementId;
		public String user;
		public String role;
		public String status;
		public String comment;
		public boolean isActive;
		public boolean isCompleted;
		
		public ElementInfo(String elementId) {
			this.elementId = elementId;
		}

		public String getUser() {
			return user;
		}
		public void setUser(String user) {
			this.user = user;
		}

		public String getRole() {
			return role;
		}
		public void setRole(String role) {
			this.role = role;
		}

		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}

		public String getComment() {
			return comment;
		}
		public void setComment(String comment) {
			this.comment = comment;
		}

		public boolean isActive() {
			return isActive;
		}
		public void setActive(boolean isActive) {
			this.isActive = isActive;
		}

		public boolean isCompleted() {
			return isCompleted;
		}
		public void setCompleted(boolean isCompleted) {
			this.isCompleted = isCompleted;
		}
		
		
	}
}

