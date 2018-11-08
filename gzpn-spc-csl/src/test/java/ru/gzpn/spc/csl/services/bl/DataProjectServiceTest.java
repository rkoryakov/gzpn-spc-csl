package ru.gzpn.spc.csl.services.bl;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ru.gzpn.spc.csl.model.BaseEntity;
import ru.gzpn.spc.csl.model.CProject;
import ru.gzpn.spc.csl.model.HProject;
import ru.gzpn.spc.csl.model.interfaces.ICProject;
import ru.gzpn.spc.csl.model.utils.Entities;
import ru.gzpn.spc.csl.ui.createdoc.NodeWrapper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataProjectServiceTest {
	@Autowired
	DataProjectService service;
	@Autowired
	DataUserSettigsService userService;
	
	static Object lock;
		
	@BeforeClass
	public static void init() {
		lock = new Object();
	}
	
	@Test
	public void getCountByGroupFieldTest() {
		getCountByGroupField("Stage", "name");
		getCountByGroupField("Stage", "id");
		getCountByGroupField("Stage", "changeTime");
	}

	@Test
	public void getItemsGroupedByFieldTest() {
		getItemsGroupedByField("Stage", "name");
		getItemsGroupedByField("Stage", "id");
		getItemsGroupedByField("Stage", "changeTime");
	}
	
	@Test
	public void getItemsGroupedByValueTest() {

		if (service.getHPRepository().findByProjectId("00020302").size() == 0) {
			HProject hProject = new HProject();
			hProject.setName("Крупный проект 1");
			hProject.setProjectId("00020302");
			List<ICProject> cprojects = new ArrayList<>();

			for (int i = 0; i < 5; i++) {
				CProject cProject = new CProject();
				cProject.setName("Капитальный проект " + (i + 1));
				cProject.setProjectId("00020302-" + (i + 1));
				cprojects.add(cProject);
			}
			hProject.setCapitalProjects(cprojects);
			service.getHPRepository().save(hProject);
		}
		// PostgreSQL doesn't keep nanoseconds in timestamp fields. It keeps milliseconds rather
		// for instance if we need 2018-11-08T09:59:17.833
		LocalDateTime createDate = LocalDateTime.of(2018, 11, 8, 9, 59, 17); // 2018-11-08T09:59:17
		createDate = createDate.plus(833, ChronoUnit.MILLIS);
		//DataProjectService.logger.debug("[getItemsGroupedByValueTest] createDate = {}", createDate);
		
		Stream<NodeWrapper> result = service.getItemsGroupedByFieldValue(Entities.CPROJECT.getName(), "version", 0, "name");
		assertThat(result).size().isEqualTo(5);
		result = service.getItemsGroupedByFieldValue(Entities.CPROJECT.getName(), "version", 0, null);
		assertThat(result).allMatch(e -> 
			e.hasEntityItem()
		);
//		result.forEach(element -> {
//					DataProjectService.logger.debug("[getItemsGroupedByValueTest] Entity: {}, Field name: {}, Field value: {}",
//							element.getEntityName(), element.getGroupFiled(), element.getGroupFiledValue());
//				});
	}
	
	
	@Test
	public void testLock() {
		DataProjectService.logger.debug("Enter into testLock() method");
		synchronized (lock) {
			DataProjectService.logger.debug("Enter into synchronized section testLock()");
			DataProjectService.logger.debug("Call testLock2()");
			testLock2();
			DataProjectService.logger.debug("Leave synchronized section testLock()");
		}
	}
	
	public void testLock2() {
		DataProjectService.logger.debug("Enter into testLock2() method");
		synchronized (lock) {
			DataProjectService.logger.debug("Enter into synchronized section testLock2()");
			Class c = BaseEntity.class;
			c = Entities.getEntityClass("HProject");
			DataProjectService.logger.debug(" class c = {}", c.getName());
			DataProjectService.logger.debug("Leave synchronized section testLock2()");
		}
	}
	
	private void getItemsGroupedByField(String entity, String field) {
		DataProjectService.logger.debug("[TEST getItemsByGroupField('{}', '{}')] - {}", entity, field,
				service.getItemsGroupedByField(entity, field)
				.map(n-> n.getEntityName() + ",  " + n.getGroupFiled() + " = " + n.getGroupFiledValue())
				.reduce(new StringBuilder(""), (p, s)-> p.append("\n").append(s),  (p, s)-> p.append(s)));
	}
	
	private void getCountByGroupField(String entity, String field) {
		DataProjectService.logger.debug("[TEST getCount('{}', '{}')] - {}", entity, field,
				service.getCount(entity, field));
	}
	
	public static void main(String[] args) {
		lock = new Object();
		DataProjectServiceTest test = new DataProjectServiceTest();
		test.testLock();
		
		(new Thread(()-> {test.testLock();})).start();
		(new Thread(()-> {test.testLock();})).start();
		(new Thread(()-> {test.testLock();})).start();
		(new Thread(()-> {test.testLock();})).start();
		(new Thread(()-> {test.testLock();})).start();
		(new Thread(()-> {test.testLock();})).start();
		(new Thread(()-> {test.testLock();})).start();
		(new Thread(()-> {test.testLock();})).start();
	}
}
