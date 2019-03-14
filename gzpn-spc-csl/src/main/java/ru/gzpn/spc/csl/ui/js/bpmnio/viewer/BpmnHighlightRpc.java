package ru.gzpn.spc.csl.ui.js.bpmnio.viewer;

import com.vaadin.shared.communication.ClientRpc;

import ru.gzpn.spc.csl.ui.js.bpmnio.modeler.ElementInfo;


public interface BpmnHighlightRpc extends ClientRpc {
	void highlight(String id, ElementInfo info);
}