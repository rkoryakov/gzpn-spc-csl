package ru.gzpn.spc.csl.ui.createdoc;

import java.util.Deque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Holds the information about the current entity(node) and grouping fields.
 * Used while creating tree structure entities where a node is an entity or 
 * is a group by some field of the current entity
 */
public abstract class NodeTracker {
	private static final Logger logger = LoggerFactory.getLogger(NodeTracker.class);
	private Deque<GroupWrapper> nodePath;
	
	public boolean trackNext(NodeTracker node) {
		node.setNodePath(nodePath);
		return !nodePath.isEmpty();
	}
	
	public GroupWrapper pollCurrent() {
		return nodePath.poll();
	}
	
	public Deque<GroupWrapper> getNodePath() {
		return nodePath;
	}
	public void setNodePath(Deque<GroupWrapper> nodePath) {
		this.nodePath = nodePath;
	}
}