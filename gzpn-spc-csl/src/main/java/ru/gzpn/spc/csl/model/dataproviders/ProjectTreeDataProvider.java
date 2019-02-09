package ru.gzpn.spc.csl.model.dataproviders;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;

import com.vaadin.data.provider.AbstractBackEndHierarchicalDataProvider;
import com.vaadin.data.provider.HierarchicalQuery;
import com.vaadin.data.provider.SortOrder;
import com.vaadin.shared.data.sort.SortDirection;

import ru.gzpn.spc.csl.model.CProject;
import ru.gzpn.spc.csl.model.HProject;
import ru.gzpn.spc.csl.model.enums.Entities;
import ru.gzpn.spc.csl.model.utils.NodeFilter;
import ru.gzpn.spc.csl.model.utils.NodeWrapper;
import ru.gzpn.spc.csl.services.bl.interfaces.IProjectService;

public class ProjectTreeDataProvider extends AbstractBackEndHierarchicalDataProvider<NodeWrapper, NodeFilter> {
	private static final long serialVersionUID = 7274680832695288557L;
	
	public static final Logger logger = LoggerFactory.getLogger(ProjectTreeDataProvider.class);
	private IProjectService projectService;
	private NodeWrapper hierarchySettings;
	private NodeFilter initialFilter;
	private List<SortOrder<String>> initialSortOrders;
	
	public ProjectTreeDataProvider(IProjectService projectService, NodeWrapper settings) {
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
		
		result = fetchChildrenFromBackEnd(query).count();
//		if (parent != null) {
//			filter = parent.getFilterForChildren();
//			result = projectService.getCount(parent.getEntityName(), parent.getGroupField(), parent.getGroupField(), filter.getCommonFilter());
//		} else {
//			hierarchySettings.setFilterForChildren(initialFilter);
//			result = projectService.getCount(hierarchySettings.getEntityName(), 
//						hierarchySettings.getGroupField(), 
//								hierarchySettings.getGroupField(), initialFilter.getCommonFilter());
//		}
		
		return (int)result;
	}

	@Override
	public boolean hasChildren(NodeWrapper node) {
		return getChildCount(new HierarchicalQuery<NodeWrapper, NodeFilter>(null, node))  > 0;
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		Set<String> authorities = authentication.getAuthorities()
//				.stream().map(GrantedAuthority::getAuthority)
//					.collect(Collectors.toSet());
//		return node.hasChild() && applyPermissionsFilter(node, authorities);
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
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Set<String> authorities = authentication.getAuthorities()
				.stream().map(GrantedAuthority::getAuthority)
					.collect(Collectors.toSet());
		
		Stream<NodeWrapper> result = items.filter(item -> applyPermissionsFilter(item, authorities));
//		if (StringUtils.isNotEmpty(nodeFilter.getCommonFilter()) 
//					|| nodeFilter.hasQueryNodeFilters()) {
//				result = items.filter(nodeFilter.filter());
//		}
		
		return result;
	}

	protected boolean applyPermissionsFilter(NodeWrapper item, Set<String> authorities) {
		boolean isShown = false;

		if (item.hasId()) {
			Entities entity = Entities.valueOf(item.getEntityName().toUpperCase());

			switch (entity) {

			case CPROJECT:
				Optional<CProject> project = projectService.getCPRepository().findById(item.getId());
				if (project.isPresent()) {
					Set<String> projectRoles = project.get().getAcl().getRoles();
					if (!projectRoles.isEmpty()) {	
						logger.debug("[filter projects] projects roles: {} user roles: {}", projectRoles, authorities);
						isShown = CollectionUtils.containsAny(authorities, projectRoles);
					}
				}
			case HPROJECT:
				
				Optional<HProject> hproject = projectService.getHPRepository().findById(item.getId());
				if (hproject.isPresent()) {
					Set<String> hprojectRoles = hproject.get().getAcl().getRoles();
					if (!hprojectRoles.isEmpty()) {	
						logger.debug("[filter projects] projects roles: {} user roles: {}", hprojectRoles, authorities);
						isShown = CollectionUtils.containsAny(authorities, hprojectRoles);
					}
				}
				
				break;
				
			case CONTRACT:
			case DOCUMENT:
			case ESTIMATECALCULATION:
			case ESTIMATECOST:
			case ESTIMATEHEAD:
			case LOCALESTIMATE:
			case LOCALESTIMATEHISTORY:
			case MILESTONE:
			case OBJECTESTIMATE:
			case PHASE:
			case PLANOBJECT:
			case STAGE:
			case USERSETTINGS:
			case WORK:
			case WORKSET:
			case MARK:
				isShown = true;
				break;
			default:
				break;
			}
		}
		return isShown;
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
