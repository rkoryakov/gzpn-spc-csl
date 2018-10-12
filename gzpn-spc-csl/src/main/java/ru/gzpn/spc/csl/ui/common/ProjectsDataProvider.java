package ru.gzpn.spc.csl.ui.common;

import java.util.stream.Stream;

import com.vaadin.data.provider.AbstractBackEndHierarchicalDataProvider;
import com.vaadin.data.provider.HierarchicalQuery;

public class ProjectsDataProvider extends AbstractBackEndHierarchicalDataProvider<ProjectNode, ProjectNodeFilter> {

	@Override
	public int getChildCount(HierarchicalQuery<ProjectNode, ProjectNodeFilter> query) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasChildren(ProjectNode item) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected Stream<ProjectNode> fetchChildrenFromBackEnd(HierarchicalQuery<ProjectNode, ProjectNodeFilter> query) {
		// TODO Auto-generated method stub
		return null;
	}

}
