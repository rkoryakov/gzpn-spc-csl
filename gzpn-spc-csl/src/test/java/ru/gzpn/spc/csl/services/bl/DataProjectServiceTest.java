package ru.gzpn.spc.csl.services.bl;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ru.gzpn.spc.csl.model.BaseEntity;
import ru.gzpn.spc.csl.model.utils.Entities;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataProjectServiceTest {
	@Autowired
	DataProjectService service;
	
	static Object lock;
		
	@BeforeClass
	public static void initLock() {
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
