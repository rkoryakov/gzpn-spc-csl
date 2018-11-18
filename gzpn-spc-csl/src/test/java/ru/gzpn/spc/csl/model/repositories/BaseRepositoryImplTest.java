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
	public void getItemsGroupedByEntityValueTest() {
		assertThat(service.getBaseRepository().getItemsGroupedByEntityValue(Entities.HPROJECT.getName(), Entities.LOCALESTIMATE.getName(),
				HProject.FIELD_NAME, "Havy Project 0", LocalEstimate.FIELD_NAME))
		.size().isGreaterThan(1);
/**  SELECT NEW ru.gzpn.spc.csl.ui.createdoc.NodeWrapper('LocalEstimate', 'name', T.name) FROM 
		 LocalEstimate T, HProject S, CProject E_1 , PlanObject E_2 , Work E_3 , LocalEstimate E_4  
		 WHERE 
		 	S.name = :sourceFieldValue AND HProject.id = CProject.hp_id  AND 
		 	CProject.id = PlanObject.cp_id  AND PlanObject.id = Work.plan_obj_id  AND 
		 	Work.plan_obj_id = LocalEstimate.id GROUP BY T.name
	*/
	}
}
