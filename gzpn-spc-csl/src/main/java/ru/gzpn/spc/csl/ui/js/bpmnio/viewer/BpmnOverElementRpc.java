package ru.gzpn.spc.csl.ui.js.bpmnio.viewer;

import com.vaadin.shared.communication.ServerRpc;

public interface BpmnOverElementRpc extends ServerRpc {
	public void onElementOver(String id);
}
