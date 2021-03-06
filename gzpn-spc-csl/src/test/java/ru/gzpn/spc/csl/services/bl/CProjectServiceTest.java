package ru.gzpn.spc.csl.services.bl;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
import ru.gzpn.spc.csl.model.Contract;
import ru.gzpn.spc.csl.model.Document;
import ru.gzpn.spc.csl.model.HProject;
import ru.gzpn.spc.csl.model.LocalEstimate;
import ru.gzpn.spc.csl.model.Mark;
import ru.gzpn.spc.csl.model.Milestone;
import ru.gzpn.spc.csl.model.Phase;
import ru.gzpn.spc.csl.model.PlanObject;
import ru.gzpn.spc.csl.model.Stage;
import ru.gzpn.spc.csl.model.Work;
import ru.gzpn.spc.csl.model.WorkSet;
import ru.gzpn.spc.csl.model.enums.DocType;
import ru.gzpn.spc.csl.model.enums.Entities;
import ru.gzpn.spc.csl.model.enums.TaxType;
import ru.gzpn.spc.csl.model.enums.WorkType;
import ru.gzpn.spc.csl.model.interfaces.ICProject;
import ru.gzpn.spc.csl.model.interfaces.IContract;
import ru.gzpn.spc.csl.model.interfaces.IDocument;
import ru.gzpn.spc.csl.model.interfaces.ILocalEstimate;
import ru.gzpn.spc.csl.model.interfaces.IMark;
import ru.gzpn.spc.csl.model.interfaces.IMilestone;
import ru.gzpn.spc.csl.model.interfaces.IPhase;
import ru.gzpn.spc.csl.model.interfaces.IPlanObject;
import ru.gzpn.spc.csl.model.interfaces.IWork;
import ru.gzpn.spc.csl.model.interfaces.IWorkSet;
import ru.gzpn.spc.csl.model.repositories.CProjectRepository;
import ru.gzpn.spc.csl.model.repositories.ContractRepository;
import ru.gzpn.spc.csl.model.repositories.DocumentRepository;
import ru.gzpn.spc.csl.model.repositories.LocalEstimateRepository;
import ru.gzpn.spc.csl.model.repositories.MilestoneRepository;
import ru.gzpn.spc.csl.model.repositories.PhaseRepository;
import ru.gzpn.spc.csl.model.repositories.PlanObjectRepository;
import ru.gzpn.spc.csl.model.repositories.StageRepository;
import ru.gzpn.spc.csl.model.repositories.WorkRepository;
import ru.gzpn.spc.csl.model.repositories.WorkSetRepository;
import ru.gzpn.spc.csl.model.utils.NodeWrapper;
import ru.gzpn.spc.csl.services.bl.interfaces.IProjectService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CProjectServiceTest {
	@Autowired
	IProjectService service;
	@Autowired
	PhaseRepository phaseRepository;
	@Autowired
	StageRepository stageRepository;
	@Autowired
	CProjectRepository cpRepository;
	@Autowired
	PlanObjectRepository planObjRepository;
	@Autowired
	WorkRepository workRepository;
	@Autowired
	LocalEstimateRepository localEstimateRepository;
	@Autowired
	WorkSetRepository workSetRepository;
	@Autowired 
	DocumentRepository documentRepository;
	@Autowired 
	ContractRepository contractRepository;
	@Autowired
	MilestoneRepository milestoneRepository;
	
	@Test
	@Transactional
	@Commit
	public void fillData() {
		fillPhases();
		fillStages();
		fillContracts();
		fillMarks();
		List<Milestone> milestones = milestoneRepository.findAll();
		// HProjects
		for (int i = 0; i < 10; i ++) {
			if (service.getHPRepository().findByCode("000000" + i).size() == 0) {
				HProject hProject = new HProject();
				hProject.setName("Крупный проект " + i);
				hProject.setCode("000000" + i);
				hProject = service.getHPRepository().save(hProject);
				List<ICProject> cprojects = new ArrayList<>();
				
				// CProjects
				for (int j = 0; j < 5; j ++) {
					CProject cProject = new CProject();
					cProject.setName("Капитальный проект " + i + "_" + j);
					cProject.setCode("00020302-" + i + "_" + j);
					IPhase phase = phaseRepository.findAll().get((int)(6*Math.random()));
					cProject.setPhase(phase);
					Stage stage = stageRepository.findAll().get((int)(3*Math.random()));
					cProject = cpRepository.save(cProject);
					
					// PlanObjects
					List<IPlanObject> planObjects = new ArrayList<>();
					for (int k = 0; k < 5; k ++) {
						PlanObject planObj = new PlanObject("00000" + i + "" + j + "" + k, "Plan Object " + (i * j + k + 1));
						planObjects.add(planObj);
						planObj = planObjRepository.save(planObj);
						// Works
						List<Work> works = new ArrayList<>();
						for (int m = 0; m < 3; m ++) {
							Work work = new Work();
							works.add(work);
							work.setName("Работа " + i + "" + j + "" + k + "" + m);
							work.setCode("10000" + i + "" + j + "" + k + "" + m);
							work.setType(WorkType.PIR);
							work.setPlanObj(planObj);
						}
						for (int m = 3; m < 6; m ++) {
							Work work = new Work();
							works.add(work);
							work.setName("Работа " + i + "" + j + "" + k + "" + m);
							work.setCode("10000" + i + "" + j + "" + k + "" + m);
							work.setType(WorkType.SMR);
							work.setPlanObj(planObj);
						}
						workRepository.saveAll(works);
						
						// WorkSets
						List<IWorkSet> worksetList = new ArrayList<>();
						for (int l = 0; l < 5; l ++) {
							int num = l + i * 5 * j * k * 5 + 1;
							IWorkSet workset = new WorkSet();
							workset.setName("Комплект " + i + "" + j + "" + k + "" + l);
							workset.setCode("12000 " + i + "" + j + "" + k + "" + l);
							workset.setPir(works.get(0));
							workset.setSmr(works.get(3));
							workset.setPlanObject(planObj);
							
							workset = workSetRepository.save((WorkSet)workset);
							worksetList.add((IWorkSet)workset);
							
							// Documents
							for (int o = 0; o < 8; o ++) {
								IDocument document = new Document();
								document.setCode("00000" + i + "" + j + "" + k + "" + l + "" + o + "");
								document.setName("Документ " + i + "" + j + "" + k + "" + l + "" + o + "");
								document.setWork(works.get((int)(works.size()*Math.random())));
								document.setWorkset(workset);
								document.setType((Math.random() > 0.5) ? DocType.LOCAL_ESTIMATE : DocType.SET_OF_DRAWINGS);
								
								// LocalEstimate
								List<ILocalEstimate> estimates = new ArrayList<>();
								for (int p = 0; p < 6; p ++) {
									LocalEstimate estimate = new LocalEstimate("00000" + i + "" + j + "" + k + "" + l + "" + o + "" + p, "Локальная смета " + num);
									estimate.setStage(stage);
									estimate = localEstimateRepository.save(estimate);
									estimates.add(estimate);
								}
								
								document.setLocalEstimates(estimates);
								documentRepository.save((Document)document);
							}
						}
						
						planObj.setWorkList(works.stream().map(e->(IWork)e).collect(Collectors.toList()));
						
					}
					
					cProject.setPlanObjects(planObjects);
					IMilestone milestone = milestones.get(i*5 + j);
				
					cProject = cpRepository.save(cProject);
					milestone.setProject(cProject);
					milestoneRepository.save((Milestone)milestone);
					
					cprojects.add(cProject);
				}
				
				hProject.setCapitalProjects(cprojects);
				service.getHPRepository().save(hProject);
			}
		}
	}
	
	@Transactional
	private void fillMarks() {
		IMark mark = new Mark();
		mark.setName("АС");
		cpRepository.getEntityManager().persist(mark);
	}

	@Transactional
	private void fillContracts() {
		for (int i = 0; i < 10; i ++) {
			IContract contract = new Contract();
			contract.setName("Договор " + i);
			contract.setCode("100374" + i);
			contract.setCustomerINN("12345679" + i);
			contract.setCustomerName("Заказчик " + i);
			contract.setCustormerBank("Банк Заказчика " + i);
			contract.setExecutorBank("Банк исполнителя " + i);
			contract.setExecutorINN("03403731" + i);
			contract.setExecutorName("Исполнитель " + i);
			contract.setSigningDate(LocalDate.of(2018, i + 1, (i + 1)*2));
			contract.setMilestones(fillMilestones(i, contract));
			contractRepository.save((Contract)contract);
		}
	}
	
	@Transactional
	private List<IMilestone> fillMilestones(int from, IContract contract) {
		List<IMilestone> result = new ArrayList<>();
		for (int i = from; i < from + 5; i ++) {
			IMilestone milestone = new Milestone();
			milestone.setCode("883" + from + "76436" + i);
			milestone.setName("Этап договора " + i);
			milestone.setStartDate(LocalDate.of(2018, i - from + 1, (i - from + 1)*2));
			milestone.setEndDate(LocalDate.of(2019, i - from + 1, (i - from + 1)*2));
			milestone.setMilNum(i);
			milestone.setPpNum( i - from + 1);
			milestone.setSum(new BigDecimal(i + "2940" + i + "00"));
			milestone.setTaxType(TaxType.INCLUDING_18_PERC);
			milestone.setContract(contract);
			result.add(milestoneRepository.save((Milestone)milestone));
		}
		
		return result;
	}

	@Transactional
	private void fillPhases() {
		
		Phase phase1 = new Phase("Фаза 1");
		phase1 = phaseRepository.save(phase1);
		
		Phase phase1_1 = new Phase("Фаза 1.1", phase1);
		phase1_1 = phaseRepository.save(phase1_1);
		List<IPhase> children = new ArrayList<>();
		children.add(phase1_1);
		phase1.setChildren(children);
		phase1 = phaseRepository.save(phase1);
		
		Phase phase2 = new Phase("Фаза 2");
		phase2 = phaseRepository.save(phase2);
		
		Phase phase2_1 = new Phase("Фаза 2.1", phase2);
		phase2_1 = phaseRepository.save(phase2_1);
		
		Phase phase2_2 = new Phase("Фаза 2.2", phase2);
		children = new ArrayList<>();
		children.add(phase2_1);
		phase2_2 = phaseRepository.save(phase2_2);
		children.add(phase2_2);
		phase2.setChildren(children);	
		phase2 = phaseRepository.save(phase2);
		
		Phase phase2_2_1 = new Phase("Фаза 2.2.1", phase2_2);
		children = new ArrayList<>();
		phase2_2_1 = phaseRepository.save(phase2_2_1);
		children.add(phase2_2_1);
		phase2_2.setChildren(children);
		phase2_2 = phaseRepository.save(phase2_2);
	}
	
	@Transactional
	private IPhase getOrCreatePhase(Phase phase) {
		
		Optional<Phase> result = phaseRepository.findOne(Example.of(phase));
		IPhase ph = result.get();
		
		if (!result.isPresent()) {
			ph = phaseRepository.save(phase);
		}
		
		return ph;
	}
	
	@Transactional
	private void fillStages() {
		Stage stage1 = new Stage("Стадия 1");
		Stage stage2 = new Stage("Стадия 2");
		Stage stage3 = new Stage("Стадия 3");
		stageRepository.save(stage1);
		stageRepository.save(stage2);
		stageRepository.save(stage3);
	}
	
	@Transactional
	private Stage getOrCreateStage(Stage stage) {
		
		Optional<Stage> result = stageRepository.findOne(Example.of(stage));
		Stage st = result.get();
		
		if (!result.isPresent()) {
			st = stageRepository.save(stage);
		}
		
		return st;
	}
	
	@Transactional
	private PlanObject getOrCreatePlanObj(PlanObject planObject) {
		Optional<PlanObject> result = planObjRepository.findOne(Example.of(planObject));
		PlanObject po = null;
		
		if (result.isPresent()) {
			po = result.get();
		} else {
			po = planObjRepository.save(planObject);
		}
		
		return po;
	}
	
	@Transactional
	private Work getOrCreateWork(Work work) {
		
		Optional<Work> result = workRepository.findOne(Example.of(work));
		Work wk = null;
		
		if (result.isPresent()) {
			wk = result.get();
		} else {
			wk = workRepository.save(work);
		}
		
		return wk;
	}
	
	@Transactional
	private LocalEstimate getOrCreateWork(LocalEstimate estimate) {
		Optional<LocalEstimate> result = localEstimateRepository.findOne(Example.of(estimate));
		LocalEstimate est = null;
		
		if (result.isPresent()) {
			est = result.get();
		} else {
			est = localEstimateRepository.save(estimate);
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
		ProjectService.logger.debug("[TEST getItemsByGroupField('{}', '{}')] - {}", entity, field,
				service.getItemsGroupedByField(entity, field)
				.map(n-> n.getEntityName() + ",  " + n.getGroupField() + " = " + n.getGroupFiledValue())
				.reduce(new StringBuilder(""), (p, s)-> p.append("\n").append(s),  (p, s)-> p.append(s)));
	}
	
	private void getCountByGroupField(String entity, String field) {
		ProjectService.logger.debug("[TEST getCount('{}', '{}')] - {}", entity, field,
				service.getCount(entity, field));
	}
}
