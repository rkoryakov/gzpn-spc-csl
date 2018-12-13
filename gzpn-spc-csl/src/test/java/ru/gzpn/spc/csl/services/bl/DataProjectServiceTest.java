package ru.gzpn.spc.csl.services.bl;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import ru.gzpn.spc.csl.model.CProject;
import ru.gzpn.spc.csl.model.HProject;
import ru.gzpn.spc.csl.model.LocalEstimate;
import ru.gzpn.spc.csl.model.Phase;
import ru.gzpn.spc.csl.model.PlanObject;
import ru.gzpn.spc.csl.model.Stage;
import ru.gzpn.spc.csl.model.Work;
import ru.gzpn.spc.csl.model.enums.WorkType;
import ru.gzpn.spc.csl.model.interfaces.ICProject;
import ru.gzpn.spc.csl.model.interfaces.IPhase;
import ru.gzpn.spc.csl.model.interfaces.IPlanObject;
import ru.gzpn.spc.csl.model.interfaces.IWork;
import ru.gzpn.spc.csl.model.repositories.LocalEstimateRepository;
import ru.gzpn.spc.csl.model.repositories.PhaseRepository;
import ru.gzpn.spc.csl.model.repositories.PlanObjectRepository;
import ru.gzpn.spc.csl.model.repositories.StageRepository;
import ru.gzpn.spc.csl.model.repositories.WorkRepository;
import ru.gzpn.spc.csl.model.utils.Entities;
import ru.gzpn.spc.csl.ui.createdoc.NodeWrapper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataProjectServiceTest {
	@Autowired
	DataProjectService service;
		
	@Test
	@Transactional
	@Commit
	public void fillData() {
		PhaseRepository phaseRepository = service.getPhaseRepository();
		StageRepository stageRepository = service.getStageRepository();
		fillPhases();
		fillStages();
		
		// HProjects
		for (int i = 0; i < 10; i ++) {
			if (service.getHPRepository().findByCode("000000" + i).size() == 0) {
				HProject hProject = new HProject();
				hProject.setName("Havy Project " + i);
				hProject.setCode("000000" + i);
				hProject = service.getHPRepository().save(hProject);
				List<ICProject> cprojects = new ArrayList<>();
				
				// CProjects
				for (int j = 0; j < 5; j ++) {
					CProject cProject = new CProject();
					cProject.setName("Capital Project " + i + "_" + j);
					cProject.setCode("00020302-" + i + "_" + j);
					IPhase phase = phaseRepository.findAll().get((int)(6*Math.random()));
					cProject.setPhase(phase);
					Stage stage = stageRepository.findAll().get((int)(3*Math.random()));
					cProject.setStage(stage);
					cProject = service.getCPRepository().save(cProject);
					
					// PlanObjects
					List<IPlanObject> planObjects = new ArrayList<>();
					for (int k = 0; k < 5; k ++) {
						PlanObject p = new PlanObject("00000" + i + "" + j + "" + k, "Object " + (i * j + k + 1), "AC");
						planObjects.add(p);
						
						// Works
						List<IWork> workList = new ArrayList<>();
						for (int l = 0; l < 5; l ++) {
							int num = l + i * 5 * j * k * 5 + 1;
							Work work = new Work("00000" + i + "" + j + "" + k + "" + l, "Work " + num, WorkType.SMR);
							
							// LocalEstimate
							LocalEstimate estimate = new LocalEstimate("00000" + i + "" + j + "" + k + "" + l, "Estimate " + num);
							estimate.setStage(stage);
							estimate = service.getLocalEstimateRepository().save(estimate);
							work.setLocalEstimate(estimate);
							work = service.getWorkRepository().save(work);
							workList.add(work);
						}
						p.setWorkList(workList);
						p = service.getPlanObjectRepository().save(p);
					}
					cProject.setPlanObjects(planObjects);
					
					cProject = service.getCPRepository().save(cProject);
					cprojects.add(cProject);
				}
				
				
				hProject.setCapitalProjects(cprojects);
				service.getHPRepository().save(hProject);
			}
		}
	}
	
	@Transactional
	private void fillPhases() {
		
		Phase phase1 = new Phase("Phase 1");
		phase1 = service.getPhaseRepository().save(phase1);
		
		Phase phase1_1 = new Phase("Phase 1.1", phase1);
		phase1_1 = service.getPhaseRepository().save(phase1_1);
		List<IPhase> children = new ArrayList<>();
		children.add(phase1_1);
		phase1.setChildren(children);
		phase1 = service.getPhaseRepository().save(phase1);
		
		Phase phase2 = new Phase("Phase 2");
		phase2 = service.getPhaseRepository().save(phase2);
		
		Phase phase2_1 = new Phase("Phase 2.1", phase2);
		phase2_1 = service.getPhaseRepository().save(phase2_1);
		
		Phase phase2_2 = new Phase("Phase 2.2", phase2);
		children = new ArrayList<>();
		children.add(phase2_1);
		phase2_2 = service.getPhaseRepository().save(phase2_2);
		children.add(phase2_2);
		phase2.setChildren(children);	
		phase2 = service.getPhaseRepository().save(phase2);
		
		Phase phase2_2_1 = new Phase("Phase 2.2.1", phase2_2);
		children = new ArrayList<>();
		phase2_2_1 = service.getPhaseRepository().save(phase2_2_1);
		children.add(phase2_2_1);
		phase2_2.setChildren(children);
		phase2_2 = service.getPhaseRepository().save(phase2_2);
	}
	
	@Transactional
	private IPhase getOrCreatePhase(Phase phase) {
		PhaseRepository repository = service.getPhaseRepository();
		Optional<Phase> result = repository.findOne(Example.of(phase));
		IPhase ph = result.get();
		
		if (!result.isPresent()) {
			ph = repository.save(phase);
		}
		
		return ph;
	}
	
	@Transactional
	private void fillStages() {
		Stage stage1 = new Stage("Stage 1");
		Stage stage2 = new Stage("Stage 2");
		Stage stage3 = new Stage("Stage 3");
		service.getStageRepository().save(stage1);
		service.getStageRepository().save(stage2);
		service.getStageRepository().save(stage3);
	}
	
	@Transactional
	private Stage getOrCreateStage(Stage stage) {
		StageRepository repository = service.getStageRepository();
		Optional<Stage> result = repository.findOne(Example.of(stage));
		Stage st = result.get();
		
		if (!result.isPresent()) {
			st = repository.save(stage);
		}
		
		return st;
	}
	
	@Transactional
	private PlanObject getOrCreatePlanObj(PlanObject planObject) {
		PlanObjectRepository repository = service.getPlanObjectRepository();
		Optional<PlanObject> result = repository.findOne(Example.of(planObject));
		PlanObject po = null;
		
		if (result.isPresent()) {
			po = result.get();
		} else {
			po = repository.save(planObject);
		}
		
		return po;
	}
	
	@Transactional
	private Work getOrCreateWork(Work work) {
		WorkRepository repository = service.getWorkRepository();
		Optional<Work> result = repository.findOne(Example.of(work));
		Work wk = null;
		
		if (result.isPresent()) {
			wk = result.get();
		} else {
			wk = repository.save(work);
		}
		
		return wk;
	}
	
	@Transactional
	private LocalEstimate getOrCreateWork(LocalEstimate estimate) {
		LocalEstimateRepository repository = service.getLocalEstimateRepository();
		Optional<LocalEstimate> result = repository.findOne(Example.of(estimate));
		LocalEstimate est = null;
		
		if (result.isPresent()) {
			est = result.get();
		} else {
			est = repository.save(estimate);
		}
		
		return est;
	}
	
	@Test
	public void getCountByGroupFieldTest() {
		getCountByGroupField("Stage", "name");
		getCountByGroupField("Stage", "id");
		getCountByGroupField("Stage", "changeDate");
	}

	@Test
	public void getItemsGroupedByFieldTest() {
		getItemsGroupedByField("Stage", "name");
		getItemsGroupedByField("Stage", "id");
		getItemsGroupedByField("Stage", "changeDate");
	}
	
	@Test
	public void getItemsGroupedByValueTest() {

		// PostgreSQL doesn't keep nanoseconds in timestamp fields. It keeps milliseconds instead
		// for instance if we need 2018-11-08T09:59:17.833
		LocalDateTime createDate = LocalDateTime.of(2018, 11, 8, 9, 59, 17); // 2018-11-08T09:59:17
		createDate = createDate.plus(833, ChronoUnit.MILLIS);
		//DataProjectService.logger.debug("[getItemsGroupedByValueTest] createDate = {}", createDate);
		
		Stream<NodeWrapper> result = service.getItemsGroupedByFieldValue(Entities.CPROJECT.getName(), "version", 0, "name");
		assertThat(result).size().isEqualTo(0);
		result = service.getItemsGroupedByFieldValue(Entities.CPROJECT.getName(), "version", 0, null);
		assertThat(result).allMatch(e -> 
			e.hasEntityItem()
		);
//		result.forEach(element -> {
//					DataProjectService.logger.debug("[getItemsGroupedByValueTest] Entity: {}, Field name: {}, Field value: {}",
//							element.getEntityName(), element.getGroupFiled(), element.getGroupFiledValue());
//				});
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
}
