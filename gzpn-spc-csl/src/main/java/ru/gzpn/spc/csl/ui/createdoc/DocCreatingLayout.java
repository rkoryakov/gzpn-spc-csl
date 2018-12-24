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
import com.vaadin.ui.HorizontalLayout;
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
	private static final String I18N_USERLAYOUTSETTINGS_DESC = "createdoc.DocCreatingLayout.userLayoutSettings.desc";
	private static final String I18N_ADDTREEITEMBUTTON_DESC = "createdoc.DocCreatingLayout.addTreeItemButton.desc";
	private static final String I18N_DELTREEITEMBUTTON_DESC = "createdoc.DocCreatingLayout.delTreeItemButton.desc";
	
	
	private DataProjectService projectService;
	private DataUserSettigsService dataUserSettigsService;
	private MessageSource messageSource;
	
	private VerticalLayout leftLayot;
	private VerticalLayout rightLayout;
	
	private TextField filterField;
	private TreeGrid<NodeWrapper> projectTree;
	private ProjectTreeDataProvider dataProvider;
	private Button filterSettings;
	private Button userLayoutSettings;
	private Button addTreeItemButton;
	private Button delTreeItemButton;
	
	public DocCreatingLayout(DataProjectService projectService, DataUserSettigsService dataUserSettigsService, MessageSource messageSource) {
		this.projectService = projectService;
		this.dataUserSettigsService = dataUserSettigsService;
		this.messageSource = messageSource;
		NodeWrapper treeSettings = dataUserSettigsService.getCreateDocSettings().getLeftTreeGrid();
		
		dataProvider = new ProjectTreeDataProvider(projectService, treeSettings);

		setSizeFull();
		setSplitPosition(50.0f, Unit.PERCENTAGE);
		setMinSplitPosition(30, Unit.PERCENTAGE);
		addStyleName(ValoTheme.SPLITPANEL_LARGE);
		setFirstComponent(createLeftLayout());
		setSecondComponent(createRightLayout());
	}

	private Component createLeftLayout() {
		leftLayot = new VerticalLayout();
		leftLayot.setMargin(true);
		leftLayot.setSpacing(true);
		leftLayot.setSizeFull();
		leftLayot.addComponent(createTreeGridFeautures());
		leftLayot.addComponent(createProjectTree());
		return leftLayot;
	}
	
	private Component createTreeGridFeautures() {
		AbsoluteLayout layout = new AbsoluteLayout();
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		layout.setStyleName("gzpn-head");
		layout.setHeight(50.0f, Unit.PIXELS);
		layout.setWidth(100.f, Unit.PERCENTAGE);
		horizontalLayout.addComponent(createSearchFilter());
		horizontalLayout.addComponent(createTreeItemButtons());
		layout.addComponent(horizontalLayout, "top:5px; left:5px");
		layout.addComponent(createSettingsButton(), "top:5px; right:5px");
		return layout;
	}

	private Component createSearchFilter() {
		filterField = new TextField();
		filterField.setWidth(200.0f, Unit.PIXELS);
		filterSettings = new Button();
		filterSettings.setIcon(VaadinIcons.FILTER);
		filterSettings.setDescription(getI18nText(I18N_SEARCHSETTINGS_DESC));
		JoinedLayout<TextField, Button> searchComp = new JoinedLayout<>(filterField, filterSettings);
		filterField.setPlaceholder(getI18nText(I18N_SEARCHFIELD_PLACEHOLDER));
		
		filterField.addValueChangeListener(e -> {
			dataProvider.getFilter().setCommonFilter(e.getValue());
			dataProvider.refreshAll();
		});
		return searchComp;
	}
	
	private Component createTreeItemButtons() {
		JoinedLayout<Button, Button> bottons = new JoinedLayout<>();
		addTreeItemButton = new Button(VaadinIcons.PLUS);
		delTreeItemButton = new Button(VaadinIcons.MINUS);
		addTreeItemButton.setDescription(getI18nText(I18N_ADDTREEITEMBUTTON_DESC));
		delTreeItemButton.setDescription(getI18nText(I18N_DELTREEITEMBUTTON_DESC));
		
		bottons.addComponent(addTreeItemButton);
		bottons.addComponent(delTreeItemButton);
		
		return bottons;
	}
	
	private Component createSettingsButton() {
		userLayoutSettings = new Button();
		userLayoutSettings.setIcon(VaadinIcons.COG_O);
		userLayoutSettings.setDescription(getI18nText(I18N_USERLAYOUTSETTINGS_DESC));
		return userLayoutSettings;
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
