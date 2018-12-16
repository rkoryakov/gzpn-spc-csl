package ru.gzpn.spc.csl.ui.createdoc;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.vaadin.data.provider.AbstractBackEndHierarchicalDataProvider;
import com.vaadin.data.provider.HierarchicalQuery;

import ru.gzpn.spc.csl.services.bl.DataProjectService;
import ru.gzpn.spc.csl.services.bl.DataUserSettigsService;

public class ProjectTreeDataProvider extends AbstractBackEndHierarchicalDataProvider<NodeWrapper, NodeFilter> {

	private DataProjectService projectService;
	private DataUserSettigsService userSettigsService;
	
	private Map<String, List<String>> nodesPath = null;
	
	public ProjectTreeDataProvider(DataProjectService projectService, DataUserSettigsService userSettigsService) {
		this.projectService = projectService;
		this.userSettigsService = userSettigsService;
	}
	
	@Override
	public int getChildCount(HierarchicalQuery<NodeWrapper, NodeFilter> query) {
		int result = (int)fetchChildrenFromBackEnd(query).count();

		return result;
	}

	@Override
	public boolean hasChildren(NodeWrapper item) {
		return item.hasChild();
	}

	@Override
	protected Stream<NodeWrapper> fetchChildrenFromBackEnd(HierarchicalQuery<NodeWrapper, NodeFilter> query) {
		Stream<NodeWrapper> result = null;
		NodeWrapper parent = query.getParent();
		//NodeFilter nodeFilter = query.getFilter().get();
		
		if (parent != null) {
				result = projectService.getItemsGroupedByValue(parent);
		} else {
			final NodeWrapper rootSettingsNode = userSettigsService.getCreateDocSettings().getLeftTreeGrid();
			result = projectService.getItemsGroupedByField(rootSettingsNode);
		}

		return result;
	}
}
