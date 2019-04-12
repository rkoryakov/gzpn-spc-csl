package ru.gzpn.spc.csl.ui.processmanager;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Stream;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.context.MessageSource;

import com.vaadin.data.provider.AbstractBackEndDataProvider;
import com.vaadin.data.provider.Query;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import ru.gzpn.spc.csl.model.CProject;
import ru.gzpn.spc.csl.model.interfaces.ICProject;
import ru.gzpn.spc.csl.services.bl.interfaces.IProcessManagerService;
import ru.gzpn.spc.csl.services.bl.interfaces.IProcessService;
import ru.gzpn.spc.csl.services.bl.interfaces.IUIService;
import ru.gzpn.spc.csl.services.bl.interfaces.IUserSettingsService;
import ru.gzpn.spc.csl.ui.common.I18n;

public abstract class AbstractBpmProcessesComponent extends VerticalLayout implements I18n {
	protected IProcessManagerService service;
	protected MessageSource messageSource;
	protected IUserSettingsService userSettingsService;
	protected String user;
	protected VerticalLayout bodyLayout;
	
	public AbstractBpmProcessesComponent(IUIService service) {
		this.service = (IProcessManagerService)service;
		this.messageSource = service.getMessageSource();
		this.userSettingsService = service.getUserSettingsService();
		this.user = userSettingsService.getCurrentUser();		
		
		setSpacing(false);
		setMargin(false);

		initEventActions();
		createHeadFutures();
		createBody();
		createFooter();
		refreshUiElements();
	}

	protected void refreshUiElements() {
		
	}

	protected void createFooter() {
		
	}

	public void createBody() {
		this.bodyLayout = createBodyLayout();
		this.addComponent(bodyLayout);
	}

	public abstract VerticalLayout createBodyLayout();

	private void createHeadFutures() {
		
	}

	private void initEventActions() {
		
	}
	

	@SuppressWarnings("serial")
	public static class ActiveProcessesDataProvider extends AbstractBackEndDataProvider<ProcessPresenter, Void> {
		private IProcessManagerService service;
		
		public ActiveProcessesDataProvider(IProcessManagerService service) {
			this.service = service;
		}
		
		@Override
		protected Stream<ProcessPresenter> fetchFromBackEnd(Query<ProcessPresenter, Void> query) {
			Stream<ProcessPresenter> result = Stream.empty();
	
				result = service.getProcessService().getProcessEngine()
							.getRuntimeService().createProcessInstanceQuery().list()
								.stream().map(item -> new ProcessPresenter(service, item, this));
			
			return result;
		}

		@Override
		protected int sizeInBackEnd(Query<ProcessPresenter, Void> query) {
			return (int)service.getProcessService().getProcessEngine()
						.getRuntimeService().createProcessInstanceQuery().count();
						
		}
	}
	
	@SuppressWarnings("serial")
	public static class HistoricProcessesDataProvider extends AbstractBackEndDataProvider<ProcessPresenter, Void> {
		private IProcessManagerService service;
		
		public HistoricProcessesDataProvider(IProcessManagerService service) {
			this.service = service;
		}
		
		@Override
		protected Stream<ProcessPresenter> fetchFromBackEnd(Query<ProcessPresenter, Void> query) {
			Stream<ProcessPresenter> result = Stream.empty();

			result = service.getProcessService().getProcessEngine()
						.getHistoryService().createHistoricProcessInstanceQuery().finished().list()
							.stream().map(item -> new ProcessPresenter(service, item, this));
			
			return result;
		}

		@Override
		protected int sizeInBackEnd(Query<ProcessPresenter, Void> query) {
			return (int)service.getProcessService().getProcessEngine()
						.getHistoryService().createHistoricProcessInstanceQuery().finished().count();
						
		}
	}
	
	@SuppressWarnings("serial")
	public static class ActiveTasksDataProvider extends AbstractBackEndDataProvider<TaskPresenter, Void> {
		private IProcessManagerService service;
		private ProcessInstance processInstance; 
		
		public ActiveTasksDataProvider(IProcessManagerService service) {
			this.service = service;
		}
		
		public ActiveTasksDataProvider(IProcessManagerService service, ProcessInstance processInstance) {
			this.service = service;
			this.processInstance = processInstance;
		}
		
		public void setProcessInstance(ProcessInstance processInstance) {
			this.processInstance = processInstance;
		}
		
		@Override
		protected Stream<TaskPresenter> fetchFromBackEnd(Query<TaskPresenter, Void> query) {
			Stream<TaskPresenter> result = Stream.empty();
			if (processInstance != null) {
				result = service.getProcessService().getProcessEngine().getTaskService()
							.createTaskQuery().processInstanceId(processInstance.getId())
								.list().stream().map(item -> new TaskPresenter(service, item));
			}
			
			return result;
		}

		@Override
		protected int sizeInBackEnd(Query<TaskPresenter, Void> query) {
			int result = 0;
			if (processInstance != null) {
				result = (int)service.getProcessService().getProcessEngine().getTaskService()
							.createTaskQuery().processInstanceId(processInstance.getId())
								.count();
			}
			return result;
		}
	}
	
	@SuppressWarnings("serial")
	public static class HistoricTasksDataProvider extends AbstractBackEndDataProvider<TaskPresenter, Void> {
		
		private IProcessManagerService service;
		private HistoricProcessInstance historyProcessInstance;
		
		public HistoricTasksDataProvider(IProcessManagerService service) {
			this.service = service;
		}
		
		public HistoricTasksDataProvider(IProcessManagerService service, HistoricProcessInstance historyProcessInstance) {
			this.service = service;
			this.historyProcessInstance = historyProcessInstance;
		}
		
		public void setHistoryProcessInstance(HistoricProcessInstance historyProcessInstance) {
			this.historyProcessInstance = historyProcessInstance;
		}
		
		@Override
		protected Stream<TaskPresenter> fetchFromBackEnd(Query<TaskPresenter, Void> query) {
			Stream<TaskPresenter> result = Stream.empty();
			if (historyProcessInstance != null) {
				result = service.getProcessService().getProcessEngine()
						.getHistoryService().createHistoricTaskInstanceQuery()
							.processInstanceId(historyProcessInstance.getId())
								.list().stream().map(item -> new TaskPresenter(service, item));
			}
			return result;
		}

		@Override
		protected int sizeInBackEnd(Query<TaskPresenter, Void> query) {
			int result = 0;
			if (historyProcessInstance != null) {
				result = (int)service.getProcessService().getProcessEngine()
							.getHistoryService().createHistoricTaskInstanceQuery()
								.processInstanceId(historyProcessInstance.getId()).count();
			}
			return result;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static class ProcessPresenter {
		
		private IProcessManagerService service;
		private ProcessInstance processInstance;
		private HistoricProcessInstance historyProcessInstance;
		private Button completeButton;
		private Button removeHistoricProcessButton;
		
		private AbstractBackEndDataProvider dataProvider;
		private String cProjectCode;
		private ICProject cProject;
		
		public ProcessPresenter(IProcessManagerService service, ProcessInstance processInstance, AbstractBackEndDataProvider dataProvider) {
			this.service = service;
			this.processInstance = processInstance;
			this.dataProvider = dataProvider;
		}

		public ProcessPresenter(IProcessManagerService service, HistoricProcessInstance historyProcessInstance, AbstractBackEndDataProvider dataProvider) {
			this.service = service;
			this.historyProcessInstance = historyProcessInstance;
			this.dataProvider = dataProvider;
		}
		
		
		public ProcessInstance getProcessInstance() {
			return processInstance;
		}

		public void setProcessInstance(ProcessInstance processInstance) {
			this.processInstance = processInstance;
		}

		public HistoricProcessInstance getHistoryProcessInstance() {
			return historyProcessInstance;
		}

		public void setHistoryProcessInstance(HistoricProcessInstance historyProcessInstance) {
			this.historyProcessInstance = historyProcessInstance;
		}

		public String getProcessId() {
			return processInstance.getId();
		}
		
		public String getProjectCode() {
			if (cProjectCode == null) {
				cProjectCode = (String) service.getProcessService().getProcessVariable(processInstance.getId(),
						IProcessService.CPROJECT_CODE);
			}
			return cProjectCode;
		}
		
		public ICProject getProject() {
			if (cProject == null) { 
				Optional<CProject> project = service.getProjectService().getCPRepository().findCProjectByCode(getProjectCode());
				cProject = project.orElse(null);
			}
			return cProject;
		}
		
		public String getProjectCaption() {
			String result = "";
			ICProject project = getProject();
			if (project != null) {
				result = project.getCode() + " - " + project.getName();
			}
			return result;
		}
		public String getHistoricProcessId() {
			return historyProcessInstance.getId();
		}
		
		public String getHistoricProjectCode() {
			String result = "";
			HistoricVariableInstance instance = service.getProcessService().getProcessEngine()
						.getHistoryService().createHistoricVariableInstanceQuery()
							.processInstanceId(historyProcessInstance.getId())
								.variableName(IProcessService.CPROJECT_CODE).singleResult();
			
			if (instance != null) {
				result = (String)instance.getValue();
			}
			return result;
		}
		
		public Button getCompleteButton() {
			if (completeButton == null) {
				completeButton = new Button();
				completeButton.setIcon(VaadinIcons.STOP);
				completeButton.setDescription("Завершить принудительно");
				completeButton.setStyleName(ValoTheme.BUTTON_BORDERLESS);
				completeButton.addStyleName(ValoTheme.BUTTON_LINK);
				completeButton.addClickListener(clickEvent -> {
					service.getProcessService().getProcessEngine()
						.getRuntimeService().deleteProcessInstance(processInstance.getId(), "Принудительно завершен");
					dataProvider.refreshAll();
					Notification.show("Информация", "Инстанция процесса " + processInstance.getId() + " завершена" , Type.TRAY_NOTIFICATION);
				});
			}
			return completeButton;
		}
		
		public Button getRemoveHistoricProcessButton() {
			if (removeHistoricProcessButton == null) {
				removeHistoricProcessButton = new Button();
				removeHistoricProcessButton.setIcon(VaadinIcons.TRASH);
				removeHistoricProcessButton.setDescription("Удалить из истории");
				removeHistoricProcessButton.setStyleName(ValoTheme.BUTTON_BORDERLESS);
				removeHistoricProcessButton.addStyleName(ValoTheme.BUTTON_LINK);
				removeHistoricProcessButton.addClickListener(clickEvent -> {
					service.getProcessService().getProcessEngine()
						.getHistoryService().deleteHistoricProcessInstance(historyProcessInstance.getId());
					dataProvider.refreshAll();
					Notification.show("Информация", "Инстанция процесса " + historyProcessInstance.getId() + " удалена из истории" , Type.TRAY_NOTIFICATION);
				});
			}
			return removeHistoricProcessButton;
		}
	}
	
	public static class TaskPresenter {
		
		private IProcessManagerService service;
		private Task task;
		private HistoricTaskInstance historicTaskInstance;
		private Button completeButton;
		private Button removeHistoricTaskButton;
		
		public TaskPresenter(IProcessManagerService service, Task task) {
			this.task = task;
			this.service = service;
		}
		
		public TaskPresenter(IProcessManagerService service, HistoricTaskInstance historicTaskInstance) {
			this.historicTaskInstance = historicTaskInstance;
			this.service = service;
		}
		
		public String getExecutor() {
			String result = null;
			if (task != null) {
				result = task.getAssignee();
			} else if (historicTaskInstance != null) {
				result = historicTaskInstance.getAssignee();
			}
			return result;
		}
		
		public String getStatus() {
			String result = null;
			if (task != null) {
				if (task.getAssignee() != null) {
					result = "В работе";
				} else if (task.isSuspended()) {
					result = "Приостановлено";
				} else {
					result = "Ожидает назначения";
				}
			} else if (historicTaskInstance != null) {
				result = "Завершено";
			}
			return result;
		}
		
		public Date getCreateDate() {
			Date result = null;
			if (task != null) {
				result = task.getCreateTime();
			} else if (historicTaskInstance != null) {
				result = historicTaskInstance.getCreateTime();
			}
			return result;
		}
		
		public LocalDateTime getCreateLocalDateTime() {
			LocalDateTime result = null;
			if (getCreateDate() != null) {
				result = LocalDateTime.ofInstant(getCreateDate().toInstant(), ZoneId.systemDefault());
			}
			return result;
		}
		
		public Date getOpenDate() {
			Date result = null;
			if (task != null) {
				result = task.getClaimTime();
			} else if (historicTaskInstance != null) {
				result = historicTaskInstance.getClaimTime();
			}
			return result;
		}
		
		public LocalDateTime getOpenLocalDateTime() {
			LocalDateTime result = null;
			if (getOpenDate() != null) {
				result = LocalDateTime.ofInstant(getOpenDate().toInstant(), ZoneId.systemDefault());
			}
			return result;
		}
		
		public Date getCloseDate() {
			Date result = null;
			if (historicTaskInstance != null) {
				result = historicTaskInstance.getEndTime();
			}
			return result;
		}

		public LocalDateTime getCloseLocalDateTime() {
			LocalDateTime result = null;
			if (getCloseDate() != null) {
				result = LocalDateTime.ofInstant(getCloseDate().toInstant(), ZoneId.systemDefault());
			}
			return result;
		}
		
		public String getTaskId() {
			return task.getId();
		}
		
		public String getTaskDefName() {
			return task.getName();
		}
		
		public String getHistoricTaskId() {
			return historicTaskInstance.getId();
		}
		
		public String getHistoricTaskDefName() {
			return historicTaskInstance.getName();
		}
	
		public Button getCompleteButton() {
			if (completeButton == null) {
				completeButton = new Button();
				completeButton.setIcon(VaadinIcons.STOP);
				completeButton.setDescription("Завершить принудительно");
				completeButton.setStyleName(ValoTheme.BUTTON_BORDERLESS);
				completeButton.addStyleName(ValoTheme.BUTTON_LINK);
			}
			
			return completeButton;
		}
		
		public Button getRemoveHistoricTaskButton() {
			if (removeHistoricTaskButton == null) {
				removeHistoricTaskButton = new Button();
				removeHistoricTaskButton.setIcon(VaadinIcons.TRASH);
				removeHistoricTaskButton.setDescription("Удалить из истории");
				removeHistoricTaskButton.setStyleName(ValoTheme.BUTTON_BORDERLESS);
				removeHistoricTaskButton.addStyleName(ValoTheme.BUTTON_LINK);
			}
			return removeHistoricTaskButton;
		}
	}
}


