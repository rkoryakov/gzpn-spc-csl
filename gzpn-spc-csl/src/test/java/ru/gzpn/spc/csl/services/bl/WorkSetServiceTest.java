package ru.gzpn.spc.csl.services.bl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.test.context.junit4.SpringRunner;

import ru.gzpn.spc.csl.model.interfaces.IWorkSet;
import ru.gzpn.spc.csl.model.jsontypes.CreateDocSettingsJson;
import ru.gzpn.spc.csl.model.utils.NodeWrapper;
import ru.gzpn.spc.csl.services.bl.interfaces.IProjectService;
import ru.gzpn.spc.csl.services.bl.interfaces.IUserSettigsService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WorkSetServiceTest extends WorkSetService {
	private static final int MAX_RESULTS = 100;
	@Autowired
	IUserSettigsService settings;
	@Autowired 
	IProjectService projetcService;
	
	@Test
	public void getItemsByNodeEmptyResultTest() {
		List<Order> orders = new ArrayList<>();
		NodeWrapper parentNode = ((CreateDocSettingsJson)settings.getUserSettings()).getLeftDefaultNodesHierarchy();
		Stream<IWorkSet> result = getItemsByNode(parentNode, 0, MAX_RESULTS);
		assertThat(result).size().isZero();
	}
	
	@Test
	public void getItemsByNodeCProjectTest() {
		List<Order> orders = new ArrayList<>();
		NodeWrapper parentNode = ((CreateDocSettingsJson)settings.getUserSettings()).getLeftDefaultNodesHierarchy();
		List<NodeWrapper> hProjectLevel = projetcService.getItemsGroupedByField(parentNode)
				.collect(Collectors.toList());
		assertThat(hProjectLevel).size().isNotZero();
		
		List<NodeWrapper> cProjectLevel = projetcService.getItemsGroupedByValue(hProjectLevel.get(0))
				.collect(Collectors.toList());
		assertThat(cProjectLevel).size().isNotZero();
		
		orders.add(createSortOrder("name", Direction.ASC));
		orders.add(createSortOrder("code", Direction.DESC));
		
		Stream<IWorkSet> result = getItemsByNode(cProjectLevel.get(0), 0, MAX_RESULTS);

		assertThat(result).size().isEqualTo(25);
	}
	
	@Test
	public void getItemsByNodeStageTest() {
		List<Order> orders = new ArrayList<>();
		NodeWrapper parentNode = ((CreateDocSettingsJson)settings.getUserSettings()).getLeftDefaultNodesHierarchy();
		List<NodeWrapper> hProjectLevel = projetcService.getItemsGroupedByField(parentNode)
				.collect(Collectors.toList());
		assertThat(hProjectLevel).size().isNotZero();
		
		List<NodeWrapper> cProjectLevel = projetcService.getItemsGroupedByValue(hProjectLevel.get(0))
				.collect(Collectors.toList());
		assertThat(cProjectLevel).size().isNotZero();
		
		List<NodeWrapper> stageLevel = projetcService.getItemsGroupedByValue(cProjectLevel.get(0))
				.collect(Collectors.toList());
		assertThat(cProjectLevel).size().isNotZero();
		
		orders.add(createSortOrder("name", Direction.ASC));
		orders.add(createSortOrder("code", Direction.DESC));
		
		Stream<IWorkSet> result = getItemsByNode(stageLevel.get(0), 0, MAX_RESULTS);

		assertThat(result.count()).isEqualTo(25);
		  
		result = Stream.empty();
		assertThat(result.count()).isEqualTo(0);
	}
}
