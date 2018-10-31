package ru.gzpn.spc.csl.services.bl;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
	public void getCountTest() {
		DataProjectService.logger.debug("[TEST getCount('Stage', 'name')] - {}", 
				service.getCount("Stage", "name"));
	}

	@Test
	public void getItemsByGroupFieldTest() {
		DataProjectService.logger.debug("[TEST getItemsByGroupField('Stage', 'name')] - {}", 
				service.getBaseRepository().getItemsByGroupField("Stage", "name"));
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
			DataProjectService.logger.debug("Leave synchronized section testLock2()");
		}
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
