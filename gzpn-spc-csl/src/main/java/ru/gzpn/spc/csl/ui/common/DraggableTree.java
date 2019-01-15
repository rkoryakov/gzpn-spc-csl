package ru.gzpn.spc.csl.ui.common;

import com.vaadin.ui.Tree;
import com.vaadin.ui.TreeGrid;

public class DraggableTree<T> extends Tree<T> {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	@Override
	public TreeGrid<T> getCompositionRoot() {
		return (TreeGrid<T>)super.getCompositionRoot();
	}
}
