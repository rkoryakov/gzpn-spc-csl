package ru.gzpn.spc.csl.ui.createdoc;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.provider.AbstractBackEndHierarchicalDataProvider;
import com.vaadin.data.provider.HierarchicalQuery;
import com.vaadin.data.provider.SortOrder;
import com.vaadin.shared.data.sort.SortDirection;

import ru.gzpn.spc.csl.services.bl.interfaces.IDataProjectService;
import ru.gzpn.spc.csl.ui.common.NodeFilter;

public class ProjectTreeDataProvider extends AbstractBackEndHierarchicalDataProvider<NodeWrapper, NodeFilter> {
	private static final long serialVersionUID = 7274680832695288557L;
	
	public static final Logger logger = LoggerFactory.getLogger(ProjectTreeDataProvider.class);
	private IDataProjectService projectService;
	private NodeWrapper hierarchySettings;
	private NodeFilter initialFilter;
	private List<SortOrder<String>> initialSortOrders;
	
	public ProjectTreeDataProvider(IDataProjectService projectService, NodeWrapper settings) {
		this.projectService = projectService;
		this.hierarchySettings = settings;
		initialFilter = new NodeFilter("");
		initialSortOrders = new ArrayList<>();
		initialSortOrders.add(new SortOrder<String>("name", SortDirection.ASCENDING));
	}
	
	@Override
	public int getChildCount(HierarchicalQuery<NodeWrapper, NodeFilter> query) {
		NodeWrapper parent = query.getParent();
		long result = 1;
		NodeFilter filter = null;
		
		if (parent != null) {
			filter = parent.getFilterForChildren();
			result = projectService.getCount(parent.getEntityName(), parent.getGroupField(), parent.getGroupField(), filter.getCommonFilter());
		} else {
			hierarchySettings.setFilterForChildren(initialFilter);
			result = projectService.getCount(hierarchySettings.getEntityName(), 
						hierarchySettings.getGroupField(), 
								hierarchySettings.getGroupField(), initialFilter.getCommonFilter());
		}
		
		return (int)result;
	}

	@Override
	public boolean hasChildren(NodeWrapper node) {
		return node.hasChild();
	}

	@Override
	protected Stream<NodeWrapper> fetchChildrenFromBackEnd(HierarchicalQuery<NodeWrapper, NodeFilter> query) {
		Stream<NodeWrapper> result;
		NodeWrapper parent = query.getParent();
		NodeFilter filter = null;
		List<SortOrder<String>> sortOrders = null;
		
		if (parent != null) {
			filter = parent.getFilterForChildren();
			sortOrders = parent.getSortOredersForChildren();
			result = filter(projectService.getItemsGroupedByValue(parent), filter)
					.peek(element -> {
						element.setFilterForChildren(generateFilter(parent));
						element.setSortOredersForChildren(generateSortOrders(parent));
					});
		} else {
			hierarchySettings.setFilterForChildren(initialFilter);
			hierarchySettings.setSortOredersForChildren(initialSortOrders);
			sortOrders = hierarchySettings.getSortOredersForChildren();
			result = filter(projectService.getItemsGroupedByField(hierarchySettings), initialFilter)
						.peek(element -> {
							element.setFilterForChildren(generateFilter(hierarchySettings));
							element.setSortOredersForChildren(generateSortOrders(hierarchySettings));
						});
		}

		return sort(result, sortOrders);
	}
	
	protected Stream<NodeWrapper> filter(Stream<NodeWrapper> items, NodeFilter nodeFilter) {
		Stream<NodeWrapper> result = items;
		if (StringUtils.isNotEmpty(nodeFilter.getCommonFilter()) 
					|| nodeFilter.hasQueryNodeFilters()) {
				result = items.filter(nodeFilter.filter());
		}
		return result;
	}

	protected Stream<NodeWrapper> sort(Stream<NodeWrapper> items, List<SortOrder<String>> sortOrders) {
		return 
			items.sorted((a, b) -> {
				String leftFieldValue = Objects.toString(a.getGroupFiledValue());
				String rightFieldValue = Objects.toString(b.getGroupFiledValue());
				return leftFieldValue.compareTo(rightFieldValue);
			});
	}
	
	/**
	 * Generate new filter for children nodes
	 * @param node
	 * @return
	 */
	protected NodeFilter generateFilter(NodeWrapper node) {
		// Just return the same now
		return node.getFilterForChildren();
	}
	
	/**
	 * Generate new sort orders for children nodes
	 * @param node
	 * @return
	 */
	protected List<SortOrder<String>> generateSortOrders(NodeWrapper node) {
		// Just return the same now
		return node.getSortOredersForChildren();
	}
}
