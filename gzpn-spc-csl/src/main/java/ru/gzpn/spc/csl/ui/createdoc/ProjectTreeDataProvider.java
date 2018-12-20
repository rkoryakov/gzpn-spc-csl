package ru.gzpn.spc.csl.ui.createdoc;

import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.provider.AbstractBackEndHierarchicalDataProvider;
import com.vaadin.data.provider.HierarchicalQuery;

import ru.gzpn.spc.csl.services.bl.DataProjectService;

public class ProjectTreeDataProvider extends AbstractBackEndHierarchicalDataProvider<NodeWrapper, NodeFilter> {
	private static final long serialVersionUID = 7274680832695288557L;
	
	public static final Logger logger = LoggerFactory.getLogger(ProjectTreeDataProvider.class);
	private DataProjectService projectService;
	private NodeWrapper settings;
	
	public ProjectTreeDataProvider(DataProjectService projectService, NodeWrapper settings) {
		this.projectService = projectService;
		this.settings = settings;
	}
	
	@Override
	public int getChildCount(HierarchicalQuery<NodeWrapper, NodeFilter> query) {
		NodeWrapper parent = query.getParent();
		long result;
		
		if (parent != null) {
			result = projectService.getCount(parent.getEntityName(), parent.getGroupField());
		} else {
			result = projectService.getCount(settings.getEntityName(), settings.getGroupField());
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
		NodeFilter nodeFilter = query.getFilter().orElse(new NodeFilter(""));
		
		if (parent != null) {
			result = filter(projectService.getItemsGroupedByValue(parent), nodeFilter);
		} else {
			result = filter(projectService.getItemsGroupedByField(settings), nodeFilter);
		}

		return result;
	}
	
	protected Stream<NodeWrapper> filter(Stream<NodeWrapper> items, NodeFilter nodeFilter) {
		return items.filter(nodeFilter.filter());
	}
}
