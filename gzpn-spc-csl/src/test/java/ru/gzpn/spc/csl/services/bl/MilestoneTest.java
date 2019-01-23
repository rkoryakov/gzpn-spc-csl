package ru.gzpn.spc.csl.services.bl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ru.gzpn.spc.csl.model.CProject;
import ru.gzpn.spc.csl.model.Milestone;
import ru.gzpn.spc.csl.model.repositories.CProjectRepository;
import ru.gzpn.spc.csl.model.repositories.MilestoneRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MilestoneTest {
	@Autowired
	MilestoneRepository milestoneRepository;
	@Autowired
	CProjectRepository cpRepository;
	
	@Test
	public void testMilestone() {
		
		List<Milestone> listProjects = milestoneRepository.findAll();
		assertThat(listProjects).anyMatch(item -> item.getProject() != null);
	}
	
	@Test
	public void testCProject() {
		List<CProject> listProjects = cpRepository.findAll();
		
		assertThat(listProjects).allMatch(item -> item.getMilestone() != null);
	}
}
