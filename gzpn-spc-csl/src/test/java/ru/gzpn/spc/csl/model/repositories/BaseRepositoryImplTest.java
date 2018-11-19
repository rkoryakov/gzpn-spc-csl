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
				HProject.FIELD_NAME, "Havy Project 0", null))
		.size().isEqualTo(125);
		/**
		 * SELECT NEW ru.gzpn.spc.csl.ui.createdoc.NodeWrapper('HProject', 'null', T.null) 
		 * FROM HProject S, LocalEstimate T, CProject E_1 , PlanObject E_2 , Work E_3  
		 * WHERE 
		 * 		S.name = :sourceFieldValue AND S.id = E_1.hproject 
		 * 		AND E_1.id = E_2.cproject  AND E_2.id = E_3.planObj  
		 * 		AND E_3.localEstimate = T.id 
		 */
	}
}
