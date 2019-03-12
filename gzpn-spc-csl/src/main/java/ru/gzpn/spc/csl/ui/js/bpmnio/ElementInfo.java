package ru.gzpn.spc.csl.ui.js.bpmnio;

import java.io.Serializable;

public class ElementInfo implements Serializable {
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

		@Override
		public String toString() {
			return "ElementInfo [elementId=" + elementId + ", user=" + user + ", role=" + role + ", status=" + status
					+ ", comment=" + comment + ", isActive=" + isActive + ", isCompleted=" + isCompleted + "]";
		}
		
		
	}