package ru.gzpn.spc.csl.services.bl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataUserSettigsServiceTest {

	@Autowired
	DataUserSettigsService settigsService;
	
	@Test
	public void getNodesOrder() {
	//assertTrue("The size of resulting Map is " + settigsService.getDefaultNodesPath().size(), settigsService.getDefaultNodesPath().size() == 4);
	}
	
	@Test
	public void testAccessToEm() {
		DataUserSettigsService.logger.debug("[TEST SELECT COUNT(DISTINCT s.name) from Stage s] - {}", 
				settigsService.geEntityManager().createQuery("SELECT COUNT(DISTINCT s.name) from Stage s", Long.class).getSingleResult());
	}
}
