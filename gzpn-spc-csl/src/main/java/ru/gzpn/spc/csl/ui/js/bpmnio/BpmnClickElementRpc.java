package ru.gzpn.spc.csl.ui.js.bpmnio;

import com.vaadin.shared.communication.ServerRpc;

public interface BpmnClickElementRpc extends ServerRpc {
	public void onElementClick(String id);
}