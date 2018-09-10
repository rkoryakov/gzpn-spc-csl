package ru.gazprom_neft.example.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ru.gazprom_neft.example.model.IProjectsRepository;
import ru.gazprom_neft.example.model.IStageObjRepository;
import ru.gazprom_neft.example.model.Location;
import ru.gazprom_neft.example.model.Project;
import ru.gazprom_neft.example.model.Stage;
import ru.gazprom_neft.example.model.StageObject;

@Service
@Transactional
public class ApplicationService {

	@Autowired
	private IProjectsRepository repository;
	@Autowired
	private IStageObjRepository stageRepository;

	public static Logger logger = LoggerFactory.getLogger(ApplicationService.class);

	public void initRepository() throws JsonProcessingException {
		if (!repository.findAll().iterator().hasNext()) {

			StageObject stageObject = new StageObject("Example StageObject");

			List<String> list = new ArrayList<>();
			list.add("street 1");
			list.add("street 2");
			list.add("street 3");

			Stage stage = new Stage("Example stage 1", stageObject);

			Project project1 = new Project("Project 1", stage);

			Project project2 = new Project("Project 2", stage);

			Location loc = new Location("USA", "New York");
			loc.setSubLocation(new Location("Region 1", "Region 1"));
			loc.setStreets(list);
			project1.setLocation(loc);
			project2.setLocation(loc);
			stageObject.setLocation(loc);

			repository.save(project1);
			repository.save(project2);
		}
		List<Project> list = repository.findStageObjects();
		list.forEach((a) -> {
			logger.debug("location {}", a.getLocation());
		});

		List<StageObject> list2 = stageRepository.findStageObjects();
		list2.forEach((a) -> {
			logger.debug("location {}", a.getLocation());
		});
//		logger.debug("location {}",
//				repository.findAll().get(0).getStage().getStageObjects().get(0).getLocation().getStreets());

//		StageObject stageObject = new StageObject("Example StageObject 2");
//		StageObject.Location loc = stageObject.new Location("USA", "New York");
//		loc.setSubLocation(stageObject.new Location("Region 1", "Region 1"));
//		List<String> list = new ArrayList<>();
//		list.add("street 1");
//		list.add("street 2");
//		list.add("street 3");
//		loc.setStreets(list);
//		stageObject.setLocation(loc);
//		Stage stage = new Stage("Example stage 1", stageObject);
//
//		Project project1 = new Project("Project 1", stage);
//		Project project2 = new Project("Project 2", stage);
//		repository.save(project1);
//		repository.save(project2);
//		List<Project> projects = repository.findAll();
//		for (Project prj : projects) {
//			prj.getStage().setStageObjects(null);
//		}

		ObjectMapper mapper = new ObjectMapper();
		// logger.debug(mapper.writeValueAsString(projects));
	}
}
