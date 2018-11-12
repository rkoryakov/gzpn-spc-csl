package ru.gzpn.spc.csl.model.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.atIndex;

import java.util.List;
import java.util.Optional;

import org.junit.Test;

import ru.gzpn.spc.csl.model.utils.ProjectEntityGraph.Rib.LinkedFields;

public class ProjectEntityGraphTest {

	@Test
	public void getPathBetweenNodesTest() {
		List<Entities> result = ProjectEntityGraph.getPathBetweenNodes(Entities.HPROJECT.getName(), Entities.WORK.getName());
		assertThat(result).contains(Entities.HPROJECT, atIndex(0));
		assertThat(result).contains(Entities.CPROJECT, atIndex(1));
		assertThat(result).contains(Entities.PLANOBJECT, atIndex(2));
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
		assertThat(result).contains(Entities.PLANOBJECT, atIndex(1));
		assertThat(result).contains(Entities.WORK, atIndex(0));
	}
	
	@Test
	public void getPathBetweenNodesTest4() {
		List<Entities> result = ProjectEntityGraph.getPathBetweenNodes(Entities.CPROJECT.getName(), Entities.WORK.getName());
		assertThat(result).contains(Entities.CPROJECT, atIndex(0));
		assertThat(result).contains(Entities.PLANOBJECT, atIndex(1));
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
	
	@Test
	public void getLinkedFieldsTest1() {
		Optional<LinkedFields> fields = ProjectEntityGraph.getLinkedFields(Entities.HPROJECT.getName(), Entities.WORK.getName());
		assertThat(fields).isEmpty();
	}
	
	@Test
	public void getLinkedFieldsTest2() {
		Optional<LinkedFields> fields = ProjectEntityGraph.getLinkedFields(Entities.STAGE.getName(), Entities.CPROJECT.getName());
		assertThat(fields.get()).isEqualTo(new LinkedFields("id", "stage_id"));
	}
	
	@Test
	public void getLinkedFieldsTest3() {
		Optional<LinkedFields> fields = ProjectEntityGraph.getLinkedFields(Entities.CPROJECT.getName(), Entities.STAGE.getName());
		assertThat(fields.get()).isEqualTo(new LinkedFields("stage_id", "id"));
	}
	
	@Test
	public void getLinkedFieldsTest4() {
		Optional<LinkedFields> fields = ProjectEntityGraph.getLinkedFields(Entities.HPROJECT.getName(), Entities.CPROJECT.getName());
		assertThat(fields.get()).isEqualTo(new LinkedFields("id", "hp_id"));
	}
	
	@Test
	public void getLinkedFieldsTest5() {
		Optional<LinkedFields> fields = ProjectEntityGraph.getLinkedFields(Entities.CPROJECT.getName(), Entities.HPROJECT.getName());
		assertThat(fields.get()).isEqualTo(new LinkedFields("hp_id", "id"));
	}
	
	@Test
	public void getLinkedFieldsTest6() {
		Optional<LinkedFields> fields = ProjectEntityGraph.getLinkedFields(Entities.PLANOBJECT.getName(), Entities.WORK.getName());
		assertThat(fields.get()).isEqualTo(new LinkedFields("id", "plan_obj_id"));
	}
	
	@Test
	public void getLinkedFieldsTest7() {
		Optional<LinkedFields> fields = ProjectEntityGraph.getLinkedFields(Entities.WORK.getName(), Entities.PLANOBJECT.getName());
		assertThat(fields.get()).isEqualTo(new LinkedFields("plan_obj_id", "id"));
	}
	
	@Test
	public void getLinkedFieldsTest8() {
		Optional<LinkedFields> fields = ProjectEntityGraph.getLinkedFields(Entities.WORK.getName(), Entities.WORK.getName());
		assertThat(fields.get()).isEqualTo(new LinkedFields("id", "id"));
	}
	
	@Test
	public void getLinkedFieldsTest9() {
		Optional<LinkedFields> fields = ProjectEntityGraph.getLinkedFields(Entities.PLANOBJECT.getName(), Entities.PLANOBJECT.getName());
		assertThat(fields.get()).isEqualTo(new LinkedFields("parent_id", "id"));
	}
	
	@Test
	public void getLinkedFieldsTest10() {
		Optional<LinkedFields> fields = ProjectEntityGraph.getLinkedFields(Entities.PHASE.getName(), Entities.PHASE.getName());
		assertThat(fields.get()).isEqualTo(new LinkedFields("parent_id", "id"));
	}
}
