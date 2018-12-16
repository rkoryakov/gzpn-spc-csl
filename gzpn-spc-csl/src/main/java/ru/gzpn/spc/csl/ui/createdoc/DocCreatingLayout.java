package ru.gzpn.spc.csl.ui.createdoc;

import org.springframework.context.MessageSource;

import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.ui.Component;
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
	private ConfigurableFilterDataProvider<NodeWrapper, NodeFilter, String> configurableFilterDataProvider;
	
	public DocCreatingLayout(DataProjectService projectService, DataUserSettigsService dataUserSettigsService, MessageSource messageSource) {
		this.projectService = projectService;
		this.dataUserSettigsService = dataUserSettigsService;
		this.messageSource = messageSource;
		
		dataProvider = new ProjectTreeDataProvider(projectService, dataUserSettigsService);
		configurableFilterDataProvider = dataProvider.withConfigurableFilter(
				(NodeFilter queryFilter, String configuredFilter) -> {
					queryFilter.setCommonFilter(configuredFilter);
					return queryFilter;
				}
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
		projectTree.addColumn(NodeWrapper::getEntityName).setCaption("Name");
		return projectTree;
	}

	private Component createLeftLayout() {
		leftLayot = new VerticalLayout();	
		return leftLayot;
	}
	
	private Component createRightLayout() {
		rightLayout = new VerticalLayout();
		
		return rightLayout;
	}
}
