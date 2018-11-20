package ru.gzpn.spc.csl.model.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ru.gzpn.spc.csl.Application;
import ru.gzpn.spc.csl.model.CProject;
import ru.gzpn.spc.csl.model.HProject;
import ru.gzpn.spc.csl.model.LocalEstimate;
import ru.gzpn.spc.csl.model.Phase;
import ru.gzpn.spc.csl.model.Stage;
import ru.gzpn.spc.csl.model.Work;
import ru.gzpn.spc.csl.model.utils.Entities;
import ru.gzpn.spc.csl.services.bl.DataProjectService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class)
public class BaseRepositoryImplTest {

	@Autowired
	DataProjectService service;
	
	@Test
	public void simpleNativeSQLTest() {
		EntityManager em = service.getBaseRepository().getEntityManager();
		assertThat(em.createNativeQuery("SELECT * FROM spc_csl_schema.capital_projects as CP "
				+ "WHERE CP.id > 1", CProject.class).getResultList())
		.size().isGreaterThan(1);
	}
	
	@Test
	public void getItemsGroupedByFieldValueTest1() {
		assertThat(service.getBaseRepository().getItemsGroupedByFieldValue(Entities.HPROJECT.getName(), Entities.LOCALESTIMATE.getName(),
				HProject.FIELD_NAME, "Havy Project 0", null))
		.size().isEqualTo(125);
	}
	
	@Test
	public void getItemsGroupedByFieldValueTest2() {
		assertThat(service.getBaseRepository().getItemsGroupedByFieldValue(Entities.HPROJECT.getName(), Entities.LOCALESTIMATE.getName(),
				HProject.FIELD_NAME, "Havy Project 0", LocalEstimate.FIELD_NAME))
		.size().isEqualTo(5);
	}
	
	@Test
	public void getItemsGroupedByFieldValueTest3() {
		assertThat(service.getBaseRepository().getItemsGroupedByFieldValue(Entities.LOCALESTIMATE.getName(), Entities.HPROJECT.getName(),
				LocalEstimate.FIELD_NAME, "Estimate 5", HProject.FIELD_NAME))
		.size().isEqualTo(10);
	}
	
	@Test
	public void getItemsGroupedByFieldValueTest4() {
		assertThat(service.getBaseRepository().getItemsGroupedByFieldValue(Entities.CPROJECT.getName(), Entities.WORK.getName(),
				CProject.FIELD_NAME, "Capital Project 0_0", Work.FIELD_NAME))
		.size().isEqualTo(5);
	}
	
	@Test
	public void getItemsGroupedByFieldValueTest5() {
		assertThat(service.getBaseRepository().getItemsGroupedByFieldValue(Entities.CPROJECT.getName(), Entities.WORK.getName(),
				CProject.FIELD_NAME, "Capital Project 0_0", null))
		.size().isEqualTo(25);
	}
	
	@Test
	public void getItemsGroupedByFieldValueTest6() {
		assertThat(service.getBaseRepository().getItemsGroupedByFieldValue(Entities.STAGE.getName(), Entities.PHASE.getName(),
				Stage.FIELD_NAME, "Stage 1", null))
		.size().isEqualTo(6);
	}
	
	@Test
	public void getItemsGroupedByFieldValueTest7() {
		assertThat(service.getBaseRepository().getItemsGroupedByFieldValue(Entities.STAGE.getName(), Entities.PHASE.getName(),
				Stage.FIELD_NAME, "Stage 1", Phase.FIELD_NAME))
		.size().isEqualTo(6);
	}
	
	@Test
	public void getItemsGroupedByFieldValueTest8() {
		assertThat(service.getBaseRepository().getItemsGroupedByFieldValue(Entities.PHASE.getName(), Entities.STAGE.getName(),
				Phase.FIELD_NAME, "Phase 1", null))
		.size().isEqualTo(3);
	}
	
	@Test
	public void getItemsGroupedByFieldValueTest9() {
		assertThat(service.getBaseRepository().getItemsGroupedByFieldValue(Entities.PHASE.getName(), Entities.STAGE.getName(),
				Phase.FIELD_NAME, "Phase 1", Stage.FIELD_NAME))
		.size().isEqualTo(3);
	}
	

	@Test
	public void getItemsGroupedByFieldValueTest10() {
		assertThat(service.getBaseRepository().getItemsGroupedByFieldValue(Entities.PHASE.getName(), Entities.PHASE.getName(),
				Phase.FIELD_NAME, "Phase 2", Phase.FIELD_NAME))
		
		/**
		 * SELECT DISTINCT NEW ru.gzpn.spc.csl.ui.createdoc.NodeWrapper('Phase', T) 
		 * FROM Stage S, Phase T, CProject E_1  
		 * WHERE S.name = :sourceFieldValue AND S.id = E_1.stage AND E_1.phase = T.id 
		 */
		.size().isEqualTo(2);
	}
}
