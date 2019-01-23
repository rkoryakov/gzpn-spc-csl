package ru.gzpn.spc.csl.model.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.atIndex;

import java.util.List;
import java.util.Optional;

import org.junit.Test;

import ru.gzpn.spc.csl.model.enums.Entities;
import ru.gzpn.spc.csl.model.utils.ProjectEntityGraph.Rib.LinkedFields;

public class ProjectEntityGraphTest {

	@Test
	public void getLinkedFieldsTest1() {
		Optional<LinkedFields> fields = ProjectEntityGraph.getLinkedFields(Entities.HPROJECT.getName(), Entities.WORK.getName());
		assertThat(fields).isEmpty();
	}
	
	@Test
	public void getLinkedFieldsTest2() {
		Optional<LinkedFields> fields = ProjectEntityGraph.getLinkedFields(Entities.STAGE.getName(), Entities.CPROJECT.getName());
		assertThat(fields.get()).isEqualTo(new LinkedFields("id", "stage"));
	}
	
	@Test
	public void getLinkedFieldsTest3() {
		Optional<LinkedFields> fields = ProjectEntityGraph.getLinkedFields(Entities.CPROJECT.getName(), Entities.STAGE.getName());
		assertThat(fields.get()).isEqualTo(new LinkedFields("stage", "id"));
	}
	
	@Test
	public void getLinkedFieldsTest4() {
		Optional<LinkedFields> fields = ProjectEntityGraph.getLinkedFields(Entities.HPROJECT.getName(), Entities.CPROJECT.getName());
		assertThat(fields.get()).isEqualTo(new LinkedFields("id", "hproject"));
	}
	
	@Test
	public void getLinkedFieldsTest5() {
		Optional<LinkedFields> fields = ProjectEntityGraph.getLinkedFields(Entities.CPROJECT.getName(), Entities.HPROJECT.getName());
		assertThat(fields.get()).isEqualTo(new LinkedFields("hproject", "id"));
	}
	
	@Test
	public void getLinkedFieldsTest6() {
		Optional<LinkedFields> fields = ProjectEntityGraph.getLinkedFields(Entities.PLANOBJECT.getName(), Entities.WORK.getName());
		assertThat(fields.get()).isEqualTo(new LinkedFields("id", "planObj"));
	}
	
	@Test
	public void getLinkedFieldsTest7() {
		Optional<LinkedFields> fields = ProjectEntityGraph.getLinkedFields(Entities.WORK.getName(), Entities.PLANOBJECT.getName());
		assertThat(fields.get()).isEqualTo(new LinkedFields("planObj", "id"));
	}
	
	@Test
	public void getLinkedFieldsTest8() {
		Optional<LinkedFields> fields = ProjectEntityGraph.getLinkedFields(Entities.WORK.getName(), Entities.WORK.getName());
		assertThat(fields.get()).isEqualTo(new LinkedFields("id", "id"));
	}
	
	@Test
	public void getLinkedFieldsTest9() {
		Optional<LinkedFields> fields = ProjectEntityGraph.getLinkedFields(Entities.PLANOBJECT.getName(), Entities.PLANOBJECT.getName());
		assertThat(fields.get()).isEqualTo(new LinkedFields("id", "parent"));
	}
	
	@Test
	public void getLinkedFieldsTest10() {
		Optional<LinkedFields> fields = ProjectEntityGraph.getLinkedFields(Entities.PHASE.getName(), Entities.PHASE.getName());
		assertThat(fields.get()).isEqualTo(new LinkedFields("id", "parent"));
	}
	
	@Test
	public void getLinkedFieldsTest11() {
		Optional<LinkedFields> fields = ProjectEntityGraph.getLinkedFields(Entities.ESTIMATECOST.name(), Entities.OBJECTESTIMATE.name());
		assertThat(fields.get()).isEqualTo(new LinkedFields("objectEstimate", "estimateCosts"));
	}
	
	@Test
	public void getLinkedFieldsTest12() {
		Optional<LinkedFields> fields = ProjectEntityGraph.getLinkedFields(Entities.OBJECTESTIMATE.name(), Entities.ESTIMATEHEAD.name());
		assertThat(fields.get()).isEqualTo(new LinkedFields("estimateHead", "objectEstimates"));
	}
	
	@Test
	public void getLinkedFieldsTest13() {
		Optional<LinkedFields> fields = ProjectEntityGraph.getLinkedFields(Entities.ESTIMATECALCULATION.name(), Entities.OBJECTESTIMATE.name());
		assertThat(fields.get()).isEqualTo(new LinkedFields("objectEstimates", "estimateCalculation"));
	}
	
	@Test
	public void getLinkedFieldsTest14() {
		Optional<LinkedFields> fields = ProjectEntityGraph.getLinkedFields(Entities.CONTRACT.name(), Entities.MILESTONE.name());
		assertThat(fields.get()).isEqualTo(new LinkedFields("milestones", "contract"));
	}
	
	@Test
	public void getLinkedFieldsTest15() {
		Optional<LinkedFields> fields = ProjectEntityGraph.getLinkedFields(Entities.WORK.name(), Entities.WORKSET.name());
		assertThat(fields.get()).isEqualTo(new LinkedFields("workSet", "id"));
	}
	
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
	public void getPathBetweenNodesTest8() {
		List<Entities> result = ProjectEntityGraph.getPathBetweenNodes(Entities.HPROJECT.getName(), Entities.LOCALESTIMATE.getName());
		assertThat(result).contains(Entities.HPROJECT, atIndex(0));
		assertThat(result).contains(Entities.CPROJECT, atIndex(1));
		assertThat(result).contains(Entities.STAGE, atIndex(2));
		assertThat(result).contains(Entities.LOCALESTIMATE, atIndex(3));
	}
	
	@Test
	public void getPathBetweenNodesTest9() {
		List<Entities> result = ProjectEntityGraph.getPathBetweenNodes(Entities.LOCALESTIMATE.getName(), Entities.HPROJECT.getName());
		assertThat(result).contains(Entities.HPROJECT, atIndex(3));
		assertThat(result).contains(Entities.CPROJECT, atIndex(2));
		assertThat(result).contains(Entities.STAGE, atIndex(1));
		assertThat(result).contains(Entities.LOCALESTIMATE, atIndex(0));
	}
	
	@Test
	public void getPathBetweenNodesTest10() {
		List<Entities> result = ProjectEntityGraph.getPathBetweenNodes(Entities.STAGE.getName(), Entities.LOCALESTIMATE.getName());
		assertThat(result).contains(Entities.STAGE, atIndex(0));
		assertThat(result).contains(Entities.LOCALESTIMATE, atIndex(1));
	}
	
	@Test
	public void getPathBetweenNodesTest11() {
		List<Entities> result = ProjectEntityGraph.getPathBetweenNodes(Entities.LOCALESTIMATE.getName(), Entities.PHASE.getName());
		assertThat(result).contains(Entities.LOCALESTIMATE, atIndex(0));
		assertThat(result).contains(Entities.STAGE, atIndex(1));
		assertThat(result).contains(Entities.CPROJECT, atIndex(2));
		assertThat(result).contains(Entities.PHASE, atIndex(3));
	}
		
	@Test
	public void getPathBetweenNodesTest12() {
		List<Entities> result = ProjectEntityGraph.getPathBetweenNodes(Entities.HPROJECT.getName(), Entities.CONTRACT.getName());
		assertThat(result).contains(Entities.HPROJECT, atIndex(0));
		assertThat(result).contains(Entities.CPROJECT, atIndex(1));
		assertThat(result).contains(Entities.MILESTONE, atIndex(2));
		assertThat(result).contains(Entities.CONTRACT, atIndex(3));
	}
	
	@Test
	public void getPathBetweenNodesTest13() {
		List<Entities> result = ProjectEntityGraph.getPathBetweenNodes(Entities.ESTIMATECOST.name(), Entities.PHASE.name());
		assertThat(result).contains(Entities.ESTIMATECOST, atIndex(0));
		assertThat(result).contains(Entities.LOCALESTIMATE, atIndex(1));
		assertThat(result).contains(Entities.STAGE, atIndex(2));
		assertThat(result).contains(Entities.CPROJECT, atIndex(3));
		assertThat(result).contains(Entities.PHASE, atIndex(4));
	}
	
	@Test
	public void getPathBetweenNodesTest14() {
		List<Entities> result = ProjectEntityGraph.getPathBetweenNodes(Entities.OBJECTESTIMATE.name(), Entities.CONTRACT.name());
		assertThat(result).contains(Entities.CONTRACT, atIndex(4));
		assertThat(result).contains(Entities.MILESTONE, atIndex(3));
		assertThat(result).contains(Entities.CPROJECT, atIndex(2));
		assertThat(result).contains(Entities.STAGE, atIndex(1));
		assertThat(result).contains(Entities.OBJECTESTIMATE, atIndex(0));
	}
	
	@Test
	public void getPathBetweenNodesTest15() {
		List<Entities> result = ProjectEntityGraph.getPathBetweenNodes(Entities.WORKSET.name(), Entities.OBJECTESTIMATE.name());
		assertThat(result).contains(Entities.OBJECTESTIMATE, atIndex(3));
		assertThat(result).contains(Entities.LOCALESTIMATE, atIndex(2));
		assertThat(result).contains(Entities.WORK, atIndex(1));
		assertThat(result).contains(Entities.WORKSET, atIndex(0));
	}
}
