package ru.gzpn.spc.csl.ui.createdoc;


import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.provider.AbstractBackEndHierarchicalDataProvider;
import com.vaadin.data.provider.HierarchicalQuery;

import ru.gzpn.spc.csl.services.bl.DataProjectService;
import ru.gzpn.spc.csl.ui.common.NodeFilter;

public class ProjectTreeDataProvider extends AbstractBackEndHierarchicalDataProvider<NodeWrapper, NodeFilter> {
	private static final long serialVersionUID = 7274680832695288557L;
	
	public static final Logger logger = LoggerFactory.getLogger(ProjectTreeDataProvider.class);
	private DataProjectService projectService;
	private NodeWrapper hierarchySettings;
	private NodeFilter filter = new NodeFilter("");
	
	public ProjectTreeDataProvider(DataProjectService projectService, NodeWrapper settings) {
		this.projectService = projectService;
		this.hierarchySettings = settings;
	}
	
	@Override
	public int getChildCount(HierarchicalQuery<NodeWrapper, NodeFilter> query) {
		NodeWrapper parent = query.getParent();
		long result = 1;

		if (parent != null) {
			result = filter(projectService.getItemsGroupedByValue(parent), filter).count();//projectService.getCount(parent.getEntityName(), parent.getGroupField());
		} else {
			result = filter(projectService.getItemsGroupedByField(hierarchySettings), filter).count();//projectService.getCount(hierarchySettings.getEntityName(), hierarchySettings.getGroupField());
		}
		
		return (int)result;
	}

	@Override
	public boolean hasChildren(NodeWrapper item) {
		return item.hasChild();
	}

	@Override
	protected Stream<NodeWrapper> fetchChildrenFromBackEnd(HierarchicalQuery<NodeWrapper, NodeFilter> query) {
		Stream<NodeWrapper> result;
		NodeWrapper parent = query.getParent();
		
		if (parent != null) {
		//	if (!parent.hasChild()) {
				result = filter(projectService.getItemsGroupedByValue(parent), filter);
			//} else {
			//	result = projectService.getItemsGroupedByValue(parent);
			//}
		} else {
			result = filter(projectService.getItemsGroupedByField(hierarchySettings), filter);
		}

		return result;
	}
	
	protected Stream<NodeWrapper> filter(Stream<NodeWrapper> items, NodeFilter nodeFilter) {
		Stream<NodeWrapper> result = items;
		if (!StringUtils.isEmpty(nodeFilter.getCommonFilter()) 
					|| nodeFilter.hasQueryNodeFilters()) {
			
				result = items.filter(nodeFilter.filter());
		}
		return result;
	}

	public NodeFilter getFilter() {
		return this.filter;
	}
	
}
