package ru.gzpn.spc.csl.ui.processmanager;

import java.util.Optional;

import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

import ru.gzpn.spc.csl.services.bl.interfaces.IUIService;

@SuppressWarnings("serial")
public class BpmProcessesComponent extends AbstractBpmProcessesComponent {

	private HorizontalSplitPanel horizontalSplitPanel;
	private VerticalLayout bpmVerticalLayout;
	private BpmnViewerComponent bpmnViewerComponent;
	private Grid<TaskPresenter> activeTasksGrid;
	private Grid<TaskPresenter> completedTasksGrid;
	private Grid<ProcessPresenter> activeProcessesGrid;
	private Grid<ProcessPresenter> completedProcessesGrid;
	private ActiveProcessesDataProvider activeProcessesDataProvider;
	private HistoricProcessesDataProvider completedProcessesDataProvider;
	private ActiveTasksDataProvider activeTasksDataProvider;
	private HistoricTasksDataProvider completedTasksDataProvider;
	private VerticalLayout activeProcTasksLayout;
	private VerticalLayout completedProcTasksLayout;
	private VerticalLayout propertiesPanelLayout;
	private Panel propertiesPanel;

	public BpmProcessesComponent(IUIService service) {
		super(service);
	}

	@Override
	public VerticalLayout createBodyLayout() {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(false);
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
		
		activeProcTasksLayout = new VerticalLayout();
		activeProcTasksLayout.setMargin(false);
		activeProcTasksLayout.addComponent(createActiveProcesses());
		activeProcTasksLayout.addComponent(createActiveTasks());
		
		completedProcTasksLayout = new VerticalLayout();
		completedProcTasksLayout.setSizeFull();
		completedProcTasksLayout.setMargin(false);
		completedProcTasksLayout.addComponent(createCompletedProcesses());
		completedProcTasksLayout.addComponent(createCompletedTasks());
		
		sheet.addTab(activeProcTasksLayout, "Активные");
		sheet.addTab(completedProcTasksLayout, "Завершенные");
		sheet.setSizeFull();

		return sheet;
	}

	public Component createActiveProcesses() {
		activeProcessesGrid = new Grid<>();
		activeProcessesDataProvider = new ActiveProcessesDataProvider(service);
		activeProcessesGrid.setDataProvider(activeProcessesDataProvider);
		activeProcessesGrid.addColumn(ProcessPresenter::getProcessId).setCaption("Ид.").setWidth(110);
		activeProcessesGrid.addColumn(ProcessPresenter::getProjectCode).setCaption("Проект");
		activeProcessesGrid.addComponentColumn(ProcessPresenter::getCompleteButton).setWidth(90);
		activeProcessesGrid.setSizeFull();
		activeProcessesGrid.addSelectionListener(selectEvent -> {
			Optional<ProcessPresenter> selectedItem = selectEvent.getFirstSelectedItem();
			if (selectedItem.isPresent()) {
				activeTasksDataProvider.setProcessInstance(selectedItem.get().getProcessInstance());
				bpmnViewerComponent.updateBpmnViewer(selectedItem.get().getProcessInstance());
				activeTasksDataProvider.refreshAll();
			}
		});
		return activeProcessesGrid;
	}
	
	private Component createCompletedProcesses() {
		completedProcessesGrid = new Grid<>();
		completedProcessesDataProvider = new HistoricProcessesDataProvider(service);
		completedProcessesGrid.setDataProvider(completedProcessesDataProvider);
		completedProcessesGrid.addColumn(ProcessPresenter::getHistoricProcessId).setCaption("Ид.").setWidth(110);
		completedProcessesGrid.addColumn(ProcessPresenter::getHistoricProjectCode).setCaption("Проект");
		completedProcessesGrid.addComponentColumn(ProcessPresenter::getRemoveHistoricProcessButton).setWidth(90);
		completedProcessesGrid.setSizeFull();
		completedProcessesGrid.addSelectionListener(selectEvent -> {
			Optional<ProcessPresenter> selectedItem = selectEvent.getFirstSelectedItem();
			if (selectedItem.isPresent()) {
				completedTasksDataProvider.setHistoryProcessInstance(selectedItem.get().getHistoryProcessInstance());
				bpmnViewerComponent.updateBpmnViewer(selectedItem.get().getHistoryProcessInstance());
				completedTasksDataProvider.refreshAll();
			}
		});
		return completedProcessesGrid;
	}
	
	public Component createActiveTasks() {
		activeTasksGrid = new Grid<>();
		activeTasksDataProvider = new ActiveTasksDataProvider(service);
		activeTasksGrid.setDataProvider(activeTasksDataProvider);
		activeTasksGrid.addColumn(TaskPresenter::getTaskId).setCaption("Ид.").setWidth(110);
		activeTasksGrid.addColumn(TaskPresenter::getTaskDefName).setCaption("Задача");
		activeTasksGrid.addComponentColumn(TaskPresenter::getCompleteButton).setWidth(90);
		activeTasksGrid.setSizeFull();
		return activeTasksGrid;
	}

	private Component createCompletedTasks() {
		completedTasksGrid = new Grid<>();
		completedTasksDataProvider = new HistoricTasksDataProvider(service);
		completedTasksGrid.setDataProvider(completedTasksDataProvider);
		completedTasksGrid.addColumn(TaskPresenter::getHistoricTaskId).setCaption("Ид.").setWidth(110);
		completedTasksGrid.addColumn(TaskPresenter::getHistoricTaskDefName).setCaption("Задача");
		completedTasksGrid.addComponentColumn(TaskPresenter::getRemoveHistoricTaskButton).setWidth(90);
		completedTasksGrid.setSizeFull();
		return completedTasksGrid;
	}
	
	public Component createBpmPanel() {
		bpmVerticalLayout = new VerticalLayout();
		bpmVerticalLayout.addComponent(createBpmViewer());
		bpmVerticalLayout.addComponent(createTaskPropertiesPanel());
//		bpmVerticalLayout.setExpandRatio(bpmVerticalLayout.getComponent(0), 3.0f);
//		bpmVerticalLayout.setExpandRatio(bpmVerticalLayout.getComponent(1), 1.0f);
		return bpmVerticalLayout;
	}

	public Component createBpmViewer() {
		bpmnViewerComponent = new BpmnViewerComponent(service);
		bpmnViewerComponent.setSizeFull();
		return bpmnViewerComponent;
	}

	public Component createTaskPropertiesPanel() {
		propertiesPanel = new Panel("Атрибуты задачи");		
		propertiesPanel.setSizeFull();
		return propertiesPanel;
	}
}
