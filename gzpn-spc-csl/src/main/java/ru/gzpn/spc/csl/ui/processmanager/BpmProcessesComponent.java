package ru.gzpn.spc.csl.ui.processmanager;

import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;

import ru.gzpn.spc.csl.services.bl.interfaces.IUIService;

@SuppressWarnings("serial")
public class BpmProcessesComponent extends AbstractBpmProcessesComponent {

	private HorizontalSplitPanel horizontalSplitPanel;
	private VerticalSplitPanel procTasksVerticalSplitPanel;
	private VerticalSplitPanel bpmVerticalSplitPanel;

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
		procTasksVerticalSplitPanel = new VerticalSplitPanel();
		procTasksVerticalSplitPanel.setSplitPosition(50f, Unit.PERCENTAGE);
		procTasksVerticalSplitPanel.setFirstComponent(createProcesses());
		procTasksVerticalSplitPanel.setSecondComponent(createTasks());
		return procTasksVerticalSplitPanel;
	}

	public Component createProcesses() {
		return null;
	}

	public Component createTasks() {
		return null;
	}

	public Component createBpmPanel() {
		bpmVerticalSplitPanel = new VerticalSplitPanel();
		bpmVerticalSplitPanel.setSplitPosition(75f, Unit.PERCENTAGE);
		bpmVerticalSplitPanel.setFirstComponent(createBpmViewer());
		bpmVerticalSplitPanel.setSecondComponent(createTaskPropertiesPanel());
		return bpmVerticalSplitPanel;
	}

	public Component createBpmViewer() {
		return null;
	}

	public Component createTaskPropertiesPanel() {
		return null;
	}
}
