package ru.gzpn.spc.csl.ui.createdoc;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.vaadin.data.provider.HierarchicalQuery;

import ru.gzpn.spc.csl.model.utils.NodeFilter;
import ru.gzpn.spc.csl.model.utils.NodeWrapper;
import ru.gzpn.spc.csl.services.bl.ProjectService;
import ru.gzpn.spc.csl.services.bl.UserSettigsService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectTreeDataProviderTest {
	
	@Autowired
	ProjectService projectService;
	@Autowired
	UserSettigsService userSettigsService;

	ProjectTreeDataProvider provider;
	
	@Before
	public void initProviderForLeftTreeSettings() {
		NodeWrapper settings = userSettigsService.getCreateDocSettings().getLeftHierarchySettings();
		provider = new ProjectTreeDataProvider(projectService, settings);
	}
	
	@Test
	public void simpleTest1() {
		HierarchicalQuery<NodeWrapper, NodeFilter> query = new HierarchicalQuery<NodeWrapper, NodeFilter>(null, null);
		assertThat(provider.getChildCount(query)).isGreaterThan(0);
	}
	
	@Test
	public void simpleTest2() {
		HierarchicalQuery<NodeWrapper, NodeFilter> query = new HierarchicalQuery<NodeWrapper, NodeFilter>(null, null);
		assertThat(provider.getChildCount(query)).isEqualTo(10);
	}
	
	@Test
	public void simpleTest3() {
		HierarchicalQuery<NodeWrapper, NodeFilter> query = new HierarchicalQuery<NodeWrapper, NodeFilter>(null, null);
		int count = provider.getChildCount(query);
		assertThat(provider.fetchChildren(query)).allMatch(e->!e.getEntityName().isEmpty());
	}
	
	@Test
	public void simpleTest4() {
		HierarchicalQuery<NodeWrapper, NodeFilter> query = new HierarchicalQuery<NodeWrapper, NodeFilter>(null, null);
		
		int count = provider.getChildCount(query);
		subNodes(query);
	}
	
	private Stream<NodeWrapper> subNodes(HierarchicalQuery<NodeWrapper, NodeFilter> query) {
		Stream<NodeWrapper> result = provider.fetchChildren(query);
		result.forEach(e -> {
			//ProjectTreeDataProvider.logger.debug("[simpleTest4] node {}", e);
			HierarchicalQuery<NodeWrapper, NodeFilter> subQuery = new HierarchicalQuery<NodeWrapper, NodeFilter>(null, e);
			int count = provider.getChildCount(subQuery);
			ProjectTreeDataProvider.logger.debug("[subNodes ] node {} size is {}", e.getGroupFiledValue(), count);
			if (count > 0) {
				subNodes(subQuery);
			}
		});
		return result;
	}
}
