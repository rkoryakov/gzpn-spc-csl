package ru.gzpn.spc.csl.ui.processmanager;

import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;

import ru.gzpn.spc.csl.services.bl.interfaces.IUIService;

@SuppressWarnings("serial")
public class BpmProcessesComponent extends AbstractBpmProcessesComponent {

	private HorizontalSplitPanel horizontalSplitPanel;
	private VerticalSplitPanel activeProcTasksVerticalSplitPanel;
	private VerticalSplitPanel bpmVerticalSplitPanel;
	private BpmnViewerComponent bpmnViewerComponent;
	private VerticalSplitPanel completedProcTasksVerticalSplitPanel;
	private Grid<TaskPresenter> activeTasksGrid;
	private Grid<TaskPresenter> completedTasksGrid;
	private Grid<ProcessPresenter> activeProcessesGrid;
	private Grid<ProcessPresenter> completedProcessesGrid;
	private ActiveProcessesDataProvider activeProcessesDataProvider;
	private HistoricProcessesDataProvider completedProcessesDataProvider;
	private ActiveTasksDataProvider activeTasksDataProvider;
	private HistoricTasksDataProvider completedTasksDataProvider;

	public BpmProcessesComponent(IUIService service) {
		super(service);
	}

	@Override
	public VerticalLayout createBodyLayout() {
		VerticalLayout layout = new VerticalLayout();
		layout.addComponent(createHorizontalSplitPanel());
		
		return layout;
	}

	public Component createHorizontalSplitPanel() {
		horizontalSplitPanel = new HorizontalSplitPanel();
		horizontalSplitPanel.setSplitPosition(35f, Unit.PERCENTAGE);
		horizontalSplitPanel.setFirstComponent(createProcTasksPanel());
		horizontalSplitPanel.setSecondComponent(createBpmPanel());
		return horizontalSplitPanel;
	}

	public Component createProcTasksPanel() {
		TabSheet sheet = new TabSheet();
		
		activeProcTasksVerticalSplitPanel = new VerticalSplitPanel();
		activeProcTasksVerticalSplitPanel.setSplitPosition(50f, Unit.PERCENTAGE);
		activeProcTasksVerticalSplitPanel.setFirstComponent(createActiveProcesses());
		activeProcTasksVerticalSplitPanel.setSecondComponent(createActiveTasks());
		activeProcTasksVerticalSplitPanel.setHeight("700px");
		
		completedProcTasksVerticalSplitPanel = new VerticalSplitPanel();
		completedProcTasksVerticalSplitPanel.setSplitPosition(50f, Unit.PERCENTAGE);
		completedProcTasksVerticalSplitPanel.setFirstComponent(createCompletedProcesses());
		completedProcTasksVerticalSplitPanel.setSecondComponent(createCompletedTasks());
		completedProcTasksVerticalSplitPanel.setSizeFull();
		
		sheet.addTab(activeProcTasksVerticalSplitPanel, "Активные");
		sheet.addTab(completedProcTasksVerticalSplitPanel, "Завершенные");
		sheet.setSizeFull();

		return sheet;
	}

	public Component createActiveProcesses() {
		activeProcessesGrid = new Grid<>();
		activeProcessesDataProvider = new ActiveProcessesDataProvider(service);
		activeProcessesGrid.setDataProvider(activeProcessesDataProvider);
		activeProcessesGrid.addColumn(ProcessPresenter::getProcessId).setCaption("Ид.");
		activeProcessesGrid.addColumn(ProcessPresenter::getProjectCode).setCaption("Проект");
		activeProcessesGrid.addColumn(ProcessPresenter::getCompleteButton);
		activeProcessesGrid.setHeightByRows(6);
		activeProcessesGrid.setSizeFull();
		return activeProcessesGrid;
	}
	
	private Component createCompletedProcesses() {
		completedProcessesGrid = new Grid<>();
		completedProcessesDataProvider = new HistoricProcessesDataProvider(service);
		completedProcessesGrid.setDataProvider(completedProcessesDataProvider);
		completedProcessesGrid.addColumn(ProcessPresenter::getHistoricProcessId).setCaption("Ид.");
		completedProcessesGrid.addColumn(ProcessPresenter::getHistoricProjectCode).setCaption("Проект");
		completedProcessesGrid.addColumn(ProcessPresenter::getRemoveHistoricProcessButton);
		completedProcessesGrid.setHeightByRows(6);
		completedProcessesGrid.setSizeFull();
		return completedProcessesGrid;
	}
	
	public Component createActiveTasks() {
		activeTasksGrid = new Grid<>();
		activeTasksDataProvider = new ActiveTasksDataProvider(service);
		activeTasksGrid.setDataProvider(activeTasksDataProvider);
		activeTasksGrid.addColumn(TaskPresenter::getTaskId).setCaption("Ид.");
		activeTasksGrid.addColumn(TaskPresenter::getTaskDefName).setCaption("Задача");
		activeTasksGrid.addColumn(TaskPresenter::getCompleteButton);
		activeTasksGrid.setHeightByRows(6);
		return activeTasksGrid;
	}

	private Component createCompletedTasks() {
		completedTasksGrid = new Grid<>();
		completedTasksDataProvider = new HistoricTasksDataProvider(service);
		completedTasksGrid.setDataProvider(completedTasksDataProvider);
		completedTasksGrid.addColumn(TaskPresenter::getHistoricTaskId).setCaption("Ид.");
		completedTasksGrid.addColumn(TaskPresenter::getHistoricTaskDefName).setCaption("Задача");
		completedTasksGrid.addColumn(TaskPresenter::getRemoveHistoricTaskButton);
		completedTasksGrid.setHeightByRows(6);
		return completedTasksGrid;
	}
	
	public Component createBpmPanel() {
		bpmVerticalSplitPanel = new VerticalSplitPanel();
		bpmVerticalSplitPanel.setSplitPosition(75f, Unit.PERCENTAGE);
		bpmVerticalSplitPanel.setFirstComponent(createBpmViewer());
		bpmVerticalSplitPanel.setSecondComponent(createTaskPropertiesPanel());
		return bpmVerticalSplitPanel;
	}

	public Component createBpmViewer() {
		bpmnViewerComponent = new BpmnViewerComponent(service);
		return bpmnViewerComponent;
	}

	public Component createTaskPropertiesPanel() {
		return null;
	}
}
