package ru.gzpn.spc.csl.ui.createdoc;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TreeGrid;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import ru.gzpn.spc.csl.services.bl.DataProjectService;
import ru.gzpn.spc.csl.services.bl.DataUserSettigsService;
import ru.gzpn.spc.csl.ui.common.JoinedLayout;
import ru.gzpn.spc.csl.ui.common.NodeFilter;

public class DocCreatingLayout extends HorizontalSplitPanel {
	private static final long serialVersionUID = -883906550551450076L;
	private static final Logger logger = LoggerFactory.getLogger(DocCreatingLayout.class);
	
	private static final String I18N_SEARCHFIELD_PLACEHOLDER = "createdoc.DocCreatingLayout.searchField.placeholder";
	private static final String I18N_SEARCHSETTINGS_DESC = "createdoc.DocCreatingLayout.searchField.searchSettings.desc";
	
	private DataProjectService projectService;
	private DataUserSettigsService dataUserSettigsService;
	private MessageSource messageSource;
	
	private VerticalLayout leftLayot;
	private VerticalLayout rightLayout;
	
	private TextField searchField;
	private TreeGrid<NodeWrapper> projectTree;
	private ProjectTreeDataProvider dataProvider;
	private Button searchSettings;
	private Button settings;
	
	public DocCreatingLayout(DataProjectService projectService, DataUserSettigsService dataUserSettigsService, MessageSource messageSource) {
		this.projectService = projectService;
		this.dataUserSettigsService = dataUserSettigsService;
		this.messageSource = messageSource;
		NodeWrapper treeSettings = dataUserSettigsService.getCreateDocSettings().getLeftTreeGrid();
		
		dataProvider = new ProjectTreeDataProvider(projectService, treeSettings);

		setSizeFull();
		setSplitPosition(50.0f, Unit.PERCENTAGE);
		addStyleName(ValoTheme.SPLITPANEL_LARGE);
		setFirstComponent(createLeftLayout());
		setSecondComponent(createRightLayout());
	}

	private Component createLeftLayout() {
		leftLayot = new VerticalLayout();
		leftLayot.setMargin(true);
		leftLayot.setSpacing(true);
		leftLayot.setSizeFull();
		leftLayot.addComponent(createTreeFeautures());
		leftLayot.addComponent(createProjectTree());
		return leftLayot;
	}
	
	private Component createTreeFeautures() {
		AbsoluteLayout layout = new AbsoluteLayout();
		layout.setStyleName("gzpn-head");
		layout.setHeight(50.0f, Unit.PIXELS);
		layout.setWidth(100.f, Unit.PERCENTAGE);
		
		layout.addComponent(createSearchFilter(), "top:5px; left:5px");
		layout.addComponent(createSettingsButton(), "top:5px; right:5px");
		return layout;
	}

	private Component createSearchFilter() {
		searchField = new TextField();
		searchSettings = new Button();
		searchSettings.setIcon(VaadinIcons.FILTER);
		searchSettings.setDescription(getI18nText(I18N_SEARCHSETTINGS_DESC));
		JoinedLayout<TextField, Button> searchComp = new JoinedLayout<>(searchField, searchSettings);
		searchField.setPlaceholder(getI18nText(I18N_SEARCHFIELD_PLACEHOLDER));
		
		searchField.addValueChangeListener(e -> {
			dataProvider.getFilter().setCommonFilter(e.getValue());
			dataProvider.refreshAll();
		});
		return searchComp;
	}
	
	private Component createSettingsButton() {
		settings = new Button();
		settings.setIcon(VaadinIcons.COG_O);
		return settings;
	}

	private Component createProjectTree() {
		projectTree = new TreeGrid<>();
		projectTree.setDataProvider(dataProvider);
		projectTree.setColumnReorderingAllowed(true);
		projectTree.setSizeFull();
		
		Column<NodeWrapper, String> name = projectTree.addColumn(node -> 
			Objects.isNull(node.getGroupFiledValue()) ? "" : node.getGroupFiledValue().toString()
		).setCaption("Name");
		name.setSortable(true);
		projectTree.addColumn(NodeWrapper::getEntityName).setCaption("Entity");
		projectTree.setHierarchyColumn(name);
		
		return projectTree;
	}
	
	private Component createRightLayout() {
		rightLayout = new WorkSetDocumentation(projectService, dataUserSettigsService, messageSource);
		
		return rightLayout;
	}
	
	private String getI18nText(String key) {
		return messageSource.getMessage(key, null, VaadinSession.getCurrent().getLocale());
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
	
	private String getI18nText(String key) {
		return messageSource.getMessage(key, null, VaadinSession.getCurrent().getLocale());
	}
}
