package ru.gzpn.spc.csl.ui.js.bpmnio;

import com.vaadin.shared.communication.ClientRpc;


public interface BpmnHighlightRpc extends ClientRpc {
	void highlight(String id, ElementInfo info);
}