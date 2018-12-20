package ru.gzpn.spc.csl.ui.createdoc;

import org.springframework.context.MessageSource;

import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.TreeGrid;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import ru.gzpn.spc.csl.services.bl.DataProjectService;
import ru.gzpn.spc.csl.services.bl.DataUserSettigsService;

public class DocCreatingLayout extends HorizontalSplitPanel {
	private static final long serialVersionUID = -883906550551450076L;
	
	private DataProjectService projectService;
	private DataUserSettigsService dataUserSettigsService;
	private MessageSource messageSource;
	
	private VerticalLayout leftLayot;
	private VerticalLayout rightLayout;
	
	private TreeGrid<NodeWrapper> projectTree;
	private ProjectTreeDataProvider dataProvider;
	private ConfigurableFilterDataProvider<NodeWrapper, NodeFilter, NodeFilter> configurableFilterDataProvider;
	
	public DocCreatingLayout(DataProjectService projectService, DataUserSettigsService dataUserSettigsService, MessageSource messageSource) {
		this.projectService = projectService;
		this.dataUserSettigsService = dataUserSettigsService;
		this.messageSource = messageSource;
		NodeWrapper treeSettings = dataUserSettigsService.getCreateDocSettings().getLeftTreeGrid();
		
		dataProvider = new ProjectTreeDataProvider(projectService, treeSettings);
		configurableFilterDataProvider = dataProvider.withConfigurableFilter(
				(NodeFilter queryFilter, NodeFilter configuredFilter) -> configuredFilter
		);
		
		setSizeFull();
		setSplitPosition(50.0f, Unit.PERCENTAGE);
		addStyleName(ValoTheme.SPLITPANEL_LARGE);
		setFirstComponent(createLeftLayout());
		setSecondComponent(createRightLayout());
		leftLayot.addComponent(createProjectTree());
	}

	private Component createProjectTree() {
		projectTree = new TreeGrid<>();
		projectTree.setDataProvider(dataProvider);
		
		Column<NodeWrapper, Object> name = projectTree.addColumn(NodeWrapper::getGroupFiledValue).setCaption("Name");
		projectTree.addColumn(NodeWrapper::getEntityName).setCaption("Entity");
		projectTree.setHierarchyColumn(name);
		
		return projectTree;
	}

	private Component createLeftLayout() {
		leftLayot = new VerticalLayout();	
		return leftLayot;
	}
	
	private Component createRightLayout() {
		rightLayout = new WorkSetDocumentation(projectService, dataUserSettigsService, messageSource);
		
		return rightLayout;
	}
}

class WorkSetDocumentation extends VerticalLayout {
	private static final long serialVersionUID = -7505276213420043371L;
	
	private DataProjectService projectService;
	private DataUserSettigsService dataUserSettigsService;
	private MessageSource messageSource;
	private TreeGrid<NodeWrapper> docTree;
	private ProjectTreeDataProvider dataProvider;
	private ConfigurableFilterDataProvider<NodeWrapper, NodeFilter, NodeFilter> configurableFilterDataProvider;
	private DocCreatingLayout parent;
	
	public WorkSetDocumentation(DataProjectService projectService, DataUserSettigsService dataUserSettigsService, MessageSource messageSource) {
		this.projectService = projectService;
		this.dataUserSettigsService = dataUserSettigsService;
		this.messageSource = messageSource;
		NodeWrapper treeSettings = dataUserSettigsService.getCreateDocSettings().getRightTreeGrid();
		
		dataProvider = new ProjectTreeDataProvider(projectService, treeSettings);
		configurableFilterDataProvider = dataProvider.withConfigurableFilter(
				(NodeFilter queryFilter, NodeFilter configuredFilter) -> configuredFilter
		);
	}
	
	public void setParent(DocCreatingLayout parent) {
		this.parent = parent;
	}
	
	public void init() {
		
	}
}
