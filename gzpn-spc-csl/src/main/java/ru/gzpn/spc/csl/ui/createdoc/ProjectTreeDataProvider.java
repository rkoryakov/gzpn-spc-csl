package ru.gzpn.spc.csl.ui.createdoc;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.provider.AbstractBackEndHierarchicalDataProvider;
import com.vaadin.data.provider.HierarchicalQuery;

import ru.gzpn.spc.csl.model.interfaces.ICProject;
import ru.gzpn.spc.csl.model.interfaces.IPhase;
import ru.gzpn.spc.csl.model.interfaces.IStage;
import ru.gzpn.spc.csl.services.bl.DataProjectService;
import ru.gzpn.spc.csl.services.bl.DataUserSettigsService;

public class ProjectTreeDataProvider extends AbstractBackEndHierarchicalDataProvider<NodeTracker, NodeFilter> {

	@Autowired
	private DataProjectService projectService;
	@Autowired
	private DataUserSettigsService userSettigsService;
	
	private Map<String, List<String>> nodesPath = null;
	
	@Override
	public int getChildCount(HierarchicalQuery<NodeTracker, NodeFilter> query) {
		int result = 0;

		NodeTracker parent = query.getParent();

		if (parent instanceof ICProject) {
			// TODO: check the current node and grouping fields (NodeWalker)
			GroupWrapper currentNode = parent.pollCurrent();
		
			
		} else if (parent instanceof IStage) {
		} else if (parent instanceof IPhase) {
		} else {
			
			//  there is no parent in the IHProject
			
			// TODO: get the first entity & grouping fields from the user's settings
			// execute query and get the result
			
			GroupWrapper nodeWrapper = userSettigsService.getDefaultNodesPath().poll();
			result = (int)projectService.getCount(nodeWrapper.getEntityName(), nodeWrapper.getGroupByFiled());
			
		}

		return result;
	}

	@Override
	public boolean hasChildren(NodeTracker item) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected Stream<NodeTracker> fetchChildrenFromBackEnd(HierarchicalQuery<NodeTracker, NodeFilter> query) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
