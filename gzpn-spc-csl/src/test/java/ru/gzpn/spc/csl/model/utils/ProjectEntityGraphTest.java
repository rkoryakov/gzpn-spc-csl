package ru.gzpn.spc.csl.model.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.atIndex;

import java.util.List;

import org.junit.Test;

public class ProjectEntityGraphTest {

	@Test
	public void getPathBetweenNodesTest() {
		List<Entities> result = ProjectEntityGraph.getPathBetweenNodes(Entities.HPROJECT.getName(), Entities.WORK.getName());
		assertThat(result).contains(Entities.HPROJECT, atIndex(0));
		assertThat(result).contains(Entities.CPROJECT, atIndex(1));
		assertThat(result).contains(Entities.PLAN_OBJECT, atIndex(2));
		assertThat(result).contains(Entities.WORK, atIndex(3));
	}
	
	@Test
	public void getPathBetweenNodesTest2() {
		List<Entities> result = ProjectEntityGraph.getPathBetweenNodes(Entities.HPROJECT.getName(), Entities.STAGE.getName());
		assertThat(result).contains(Entities.HPROJECT, atIndex(0));
		assertThat(result).contains(Entities.CPROJECT, atIndex(1));
		assertThat(result).contains(Entities.STAGE, atIndex(2));
	}
	
	@Test
	public void getPathBetweenNodesTest3() {
		List<Entities> result = ProjectEntityGraph.getPathBetweenNodes(Entities.WORK.getName(), Entities.HPROJECT.getName());
		assertThat(result).contains(Entities.HPROJECT, atIndex(3));
		assertThat(result).contains(Entities.CPROJECT, atIndex(2));
		assertThat(result).contains(Entities.PLAN_OBJECT, atIndex(1));
		assertThat(result).contains(Entities.WORK, atIndex(0));
	}
	
	@Test
	public void getPathBetweenNodesTest4() {
		List<Entities> result = ProjectEntityGraph.getPathBetweenNodes(Entities.CPROJECT.getName(), Entities.WORK.getName());
		assertThat(result).contains(Entities.CPROJECT, atIndex(0));
		assertThat(result).contains(Entities.PLAN_OBJECT, atIndex(1));
		assertThat(result).contains(Entities.WORK, atIndex(2));
	}
	
	@Test
	public void getPathBetweenNodesTest5() {
		List<Entities> result = ProjectEntityGraph.getPathBetweenNodes(Entities.HPROJECT.getName(), Entities.CPROJECT.getName());
		assertThat(result).contains(Entities.HPROJECT, atIndex(0));
		assertThat(result).contains(Entities.CPROJECT, atIndex(1));
	}
	
	@Test
	public void getPathBetweenNodesTest6() {
		List<Entities> result = ProjectEntityGraph.getPathBetweenNodes(Entities.STAGE.getName(), Entities.CPROJECT.getName());
		assertThat(result).contains(Entities.STAGE, atIndex(0));
		assertThat(result).contains(Entities.CPROJECT, atIndex(1));
	}
	
	@Test
	public void getPathBetweenNodesTest7() {
		List<Entities> result = ProjectEntityGraph.getPathBetweenNodes(Entities.STAGE.getName(), Entities.STAGE.getName());
		assertThat(result).contains(Entities.STAGE, atIndex(0));
	}
}
