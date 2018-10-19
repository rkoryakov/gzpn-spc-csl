package ru.gzpn.spc.csl.services.bl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Collections;

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
		assertTrue("The size of resulting Map is " + settigsService.getNodesOrder().size(), settigsService.getNodesOrder().size() == 3);
		assertThat(settigsService.getNodesOrder()).containsKeys("HProject", "CProject", "Phase");
		assertThat(settigsService.getNodesOrder()).containsValue(Collections.emptyList());
		settigsService.getNodesOrder().forEach((k, o) -> System.out.println("key " + k + " value " + o));
	}
}
