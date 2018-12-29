package ru.gzpn.spc.csl.ui.createdoc;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import com.vaadin.data.ValueProvider;
import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.TreeGrid;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import ru.gzpn.spc.csl.model.WorkSet;
import ru.gzpn.spc.csl.model.jsontypes.ColumnSettings;
import ru.gzpn.spc.csl.model.jsontypes.CreateDocSettingsJson;
import ru.gzpn.spc.csl.services.bl.DataProjectService;
import ru.gzpn.spc.csl.services.bl.DataUserSettigsService;
import ru.gzpn.spc.csl.services.bl.WorkSetService;
import ru.gzpn.spc.csl.ui.common.JoinedLayout;
import ru.gzpn.spc.csl.ui.common.NodeFilter;

public class DocCreatingLayout extends HorizontalSplitPanel {
	private static final long serialVersionUID = -883906550551450076L;
	private static final Logger logger = LoggerFactory.getLogger(DocCreatingLayout.class);
	
	private static final String I18N_SEARCHFIELD_PLACEHOLDER = "createdoc.DocCreatingLayout.worksetFilterField.placeholder";
	private static final String I18N_SEARCHSETTINGS_DESC = "createdoc.DocCreatingLayout.worksetFilterSettingsButton.desc";
	private static final String I18N_USERLAYOUTSETTINGS_DESC = "createdoc.DocCreatingLayout.userLayoutSettings.desc";
	private static final String I18N_ADDTREEITEMBUTTON_DESC = "createdoc.DocCreatingLayout.addWorksetButton.desc";
	private static final String I18N_DELTREEITEMBUTTON_DESC = "createdoc.DocCreatingLayout.delWorksetButton.desc";
	private static final String I18N_TREE_CAPTION = "createdoc.DocCreatingLayout.projectTreeCaption";
	/* worksetGrid column captions*/
	private static final String I18N_WORKSET_COLUMN_NAME = "createdoc.DocCreatingLayout.worksetGrid.columns.name";
	private static final String I18N_WORKSET_COLUMN_CODE = "createdoc.DocCreatingLayout.worksetGrid.columns.code";
	private static final String I18N_WORKSET_COLUMN_PIR = "createdoc.DocCreatingLayout.worksetGrid.columns.pir";
	private static final String I18N_WORKSET_COLUMN_SMR = "createdoc.DocCreatingLayout.worksetGrid.columns.smr";
	private static final String I18N_WORKSET_COLUMN_PLANOBJ = "createdoc.DocCreatingLayout.worksetGrid.columns.planobj";
	private static final String I18N_WORKSET_COLUMN_ID = "createdoc.DocCreatingLayout.worksetGrid.columns.id";
	private static final String I18N_WORKSET_COLUMN_VERSION = "createdoc.DocCreatingLayout.worksetGrid.columns.ver";
	private static final String I18N_WORKSET_COLUMN_CREATEDATE = "createdoc.DocCreatingLayout.worksetGrid.columns.createdate";
	private static final String I18N_WORKSET_COLUMN_CHANGEDATE = "createdoc.DocCreatingLayout.worksetGrid.columns.changedate";
	
	private static final int WORKSET_GRID_ROWS = 14;
	private static final int WORKSET_GRID_HEIGHT = 568;
	private static final int TREEPANEL_HEIGHT_CORRECTION = 38;
	private static final int MAIN_MIN_SPLIT_POSITION = 30;
	private static final int LEFT_MIN_SPLIT_POSITION = 200;
	
	private DataProjectService projectService;
	private DataUserSettigsService dataUserSettigsService;
	private MessageSource messageSource;
	private CreateDocSettingsJson docSettingsJson;
	
	private VerticalLayout leftLayot;
	private VerticalLayout rightLayout;
	
	private TextField worksetFilterField;
	private Button worksetFilterSettingsButton;
	
	private Tree<NodeWrapper> projectTree;
	private Grid<WorkSet> worksetGrid;
	private ProjectTreeDataProvider projectTreeDataProvider;
	private WorksetDataProvider worksetDataProvider;
	
	private Button userLayoutSettings;
	private Button addWorksetButton;
	private Button delWorksetButton;
	
	
	public DocCreatingLayout(DataProjectService projectService, WorkSetService worksetService, DataUserSettigsService dataUserSettigsService, MessageSource messageSource) {
		this.projectService = projectService;
		this.dataUserSettigsService = dataUserSettigsService;
		this.messageSource = messageSource;
		docSettingsJson = dataUserSettigsService.getCreateDocSettings();
		NodeWrapper treeSettings = docSettingsJson.getLeftHierarchySettings();
		
		projectTreeDataProvider = new ProjectTreeDataProvider(projectService, treeSettings);
		worksetDataProvider = new WorksetDataProvider(worksetService);

		setSizeFull();
		
		setSplitPosition(docSettingsJson.getMainSplitPosition(), Unit.PERCENTAGE);
		setMinSplitPosition(MAIN_MIN_SPLIT_POSITION, Unit.PERCENTAGE);
		addStyleName(ValoTheme.SPLITPANEL_LARGE);
		setFirstComponent(createLeftLayout());
		setSecondComponent(createRightLayout());
	}

	private Component createLeftLayout() {
		leftLayot = new VerticalLayout();
		leftLayot.setMargin(true);
		leftLayot.setSpacing(true);
		leftLayot.setSizeFull();
		
		HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();
		splitPanel.setSizeFull();
		splitPanel.setMinSplitPosition(LEFT_MIN_SPLIT_POSITION, Unit.PIXELS);
		splitPanel.setSplitPosition(docSettingsJson.getLeftSplitPosition(), Unit.PIXELS);
		splitPanel.addComponent(createProjectTree());
		splitPanel.addComponent(createWorksetGrid());
		
		leftLayot.addComponent(createLeftHeadFeautures());
		leftLayot.addComponent(splitPanel);
		
		return leftLayot;
	}
	
	private Component createProjectTree() {
		projectTree = new Tree<>();
		Panel panel = new Panel(getI18nText(I18N_TREE_CAPTION));
		projectTree.setDataProvider(projectTreeDataProvider);
		projectTree.setItemCaptionGenerator(NodeWrapper::getNodeCaption);
		projectTree.setItemIconGenerator(new ProjectItemIconGenerator());
		projectTree.setSizeFull();
		projectTree.setHeight(WORKSET_GRID_HEIGHT - TREEPANEL_HEIGHT_CORRECTION, Unit.PIXELS);
		panel.setContent(projectTree);
		panel.setSizeFull();
		return panel;
	}
	
	private Component createWorksetGrid() {
		worksetGrid = new Grid<>();
		List<ColumnSettings> columnSettings = dataUserSettigsService
												.getCreateDocSettings()
													.getLeftWorksetColumns();
		columnSettings.sort((cs1, cs2) -> 
			Integer.compare(cs1.getOrderIndex(), cs2.getOrderIndex())
		);
		columnSettings.forEach(this::addWorksetGridColumn);
		worksetGrid.setSizeFull();
		worksetGrid.setHeightByRows(WORKSET_GRID_ROWS);
		worksetGrid.setColumnReorderingAllowed(true);
		worksetGrid.setDataProvider(worksetDataProvider);
		
		return worksetGrid;
	}

	private void addWorksetGridColumn(ColumnSettings settings) {
		switch (settings.getEntityFieldName()) {		
		case WorkSet.FIELD_NAME:
			addWorksetGridColumn(settings, WorkSet::getName, I18N_WORKSET_COLUMN_NAME);
			break;
		case WorkSet.FIELD_CODE:
			addWorksetGridColumn(settings, WorkSet::getCode, I18N_WORKSET_COLUMN_CODE);
			break;
		case WorkSet.FIELD_PIR:
			addWorksetGridColumn(settings, WorkSet::getPir, I18N_WORKSET_COLUMN_PIR);
			break;
		case WorkSet.FIELD_SMR:
			addWorksetGridColumn(settings, WorkSet::getSmr, I18N_WORKSET_COLUMN_SMR);
			break;
		case WorkSet.FIELD_PLAN_OBJECT:
			addWorksetGridColumn(settings, WorkSet::getPlanObject, I18N_WORKSET_COLUMN_PLANOBJ);
			break;
		case WorkSet.FIELD_ID:
			addWorksetGridColumn(settings, WorkSet::getId, I18N_WORKSET_COLUMN_ID);
			break;
		case WorkSet.FIELD_VERSION:
			addWorksetGridColumn(settings, WorkSet::getVersion, I18N_WORKSET_COLUMN_VERSION);
			break;
		case WorkSet.FIELD_CREATE_DATE:
			addWorksetGridColumn(settings, WorkSet::getCreateDate, I18N_WORKSET_COLUMN_CREATEDATE);
			break;
		case WorkSet.FIELD_CHANGE_DATE:
			addWorksetGridColumn(settings, WorkSet::getChangeDate, I18N_WORKSET_COLUMN_CHANGEDATE);
			break;
			default:
		}
	}
	
	private <T> void addWorksetGridColumn(ColumnSettings settings, ValueProvider<WorkSet, T> provider, String i18nCaption) {
		Column<WorkSet, T> column = worksetGrid.addColumn(provider);
		column.setSortProperty(settings.getEntityFieldName());
		column.setSortable(true);
		column.setCaption(getI18nText(i18nCaption));
		Double width = settings.getWidth();
		
		if (!Objects.isNull(width) && Double.isFinite(width) && width > 1) {
			column.setWidth(width);
		} else {
			column.setWidthUndefined();
		}
		column.setHidden(!settings.isShown());
	}
	
	private Component createLeftHeadFeautures() {
		AbsoluteLayout layout = new AbsoluteLayout();
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		layout.setStyleName("gzpn-head");
		layout.setHeight(50.0f, Unit.PIXELS);
		layout.setWidth(100.f, Unit.PERCENTAGE);
		horizontalLayout.addComponent(createWorksetFilter());
		horizontalLayout.addComponent(createWorksetButtons());
		layout.addComponent(createSettingsButton(), "top:5px; left:5px");
		layout.addComponent(horizontalLayout, "top:5px; right:5px");
		return layout;
	}

	private Component createWorksetFilter() {
		worksetFilterField = new TextField();
		worksetFilterField.setWidth(200.0f, Unit.PIXELS);
		worksetFilterSettingsButton = new Button();
		worksetFilterSettingsButton.setIcon(VaadinIcons.FILTER);
		worksetFilterSettingsButton.setDescription(getI18nText(I18N_SEARCHSETTINGS_DESC));
		JoinedLayout<TextField, Button> searchComp = new JoinedLayout<>(worksetFilterField, worksetFilterSettingsButton);
		worksetFilterField.setPlaceholder(getI18nText(I18N_SEARCHFIELD_PLACEHOLDER));
		
		worksetFilterField.addValueChangeListener(e -> {

		});
		return searchComp;
	}

	private Component createWorksetButtons() {
		JoinedLayout<Button, Button> bottons = new JoinedLayout<>();
		addWorksetButton = new Button(VaadinIcons.PLUS);
		delWorksetButton = new Button(VaadinIcons.MINUS);
		addWorksetButton.setDescription(getI18nText(I18N_ADDTREEITEMBUTTON_DESC));
		delWorksetButton.setDescription(getI18nText(I18N_DELTREEITEMBUTTON_DESC));
		addWorksetButton.setEnabled(false);
		delWorksetButton.setEnabled(false);
		
		bottons.addComponent(addWorksetButton);
		bottons.addComponent(delWorksetButton);
		
		return bottons;
	}

	private Component createSettingsButton() {
		userLayoutSettings = new Button();
		userLayoutSettings.setIcon(VaadinIcons.COG_O);
		userLayoutSettings.setDescription(getI18nText(I18N_USERLAYOUTSETTINGS_DESC));
		return userLayoutSettings;
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
		NodeWrapper treeSettings = dataUserSettigsService.getCreateDocSettings().getRightHierarchySettings();
		
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
