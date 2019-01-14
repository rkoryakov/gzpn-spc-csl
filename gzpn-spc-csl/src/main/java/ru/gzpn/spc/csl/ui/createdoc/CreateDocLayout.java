package ru.gzpn.spc.csl.ui.createdoc;

import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import com.vaadin.data.ValueProvider;
import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
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
import com.vaadin.ui.components.grid.HeaderCell;
import com.vaadin.ui.components.grid.HeaderRow;
import com.vaadin.ui.themes.ValoTheme;

import ru.gzpn.spc.csl.model.interfaces.IWorkSet;
import ru.gzpn.spc.csl.model.jsontypes.ColumnHeaderGroup;
import ru.gzpn.spc.csl.model.jsontypes.ColumnSettings;
import ru.gzpn.spc.csl.model.jsontypes.UserSettingsJson;
import ru.gzpn.spc.csl.model.utils.NodeFilter;
import ru.gzpn.spc.csl.model.utils.NodeWrapper;
import ru.gzpn.spc.csl.services.bl.interfaces.IDataProjectService;
import ru.gzpn.spc.csl.services.bl.interfaces.IDataUserSettigsService;
import ru.gzpn.spc.csl.services.bl.interfaces.IWorkSetService;
import ru.gzpn.spc.csl.ui.common.JoinedLayout;
import ru.gzpn.spc.csl.ui.common.data.export.Exporter;

public class CreateDocLayout extends HorizontalSplitPanel {
	private static final long serialVersionUID = -883906550551450076L;
	private static final Logger logger = LoggerFactory.getLogger(CreateDocLayout.class);
	
	private static final String I18N_SEARCHFIELD_PLACEHOLDER = "createdoc.DocCreatingLayout.worksetFilterField.placeholder";
	private static final String I18N_SEARCHSETTINGS_DESC = "createdoc.DocCreatingLayout.worksetFilterSettingsButton.desc";
	private static final String I18N_USERLAYOUTSETTINGS_DESC = "createdoc.DocCreatingLayout.userLayoutSettings.desc";
	private static final String I18N_ADDWORKSETBUTTON_DESC = "createdoc.DocCreatingLayout.addWorksetButton.desc";
	private static final String I18N_DELWORKSETBUTTON_DESC = "createdoc.DocCreatingLayout.delWorksetButton.desc";
	private static final String I18N_DOWNLOADWORKSETBUTTON_DESC = "createdoc.DocCreatingLayout.downloadWorksetButton.desc";
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
	private static final int LEFT_MIN_SPLIT_POSITION = 25;
	
	private IDataProjectService projectService;
	private IDataUserSettigsService dataUserSettigsService;
	private MessageSource messageSource;
	private UserSettingsJson docSettingsJson;
	
	private VerticalLayout leftLayot;
	private VerticalLayout rightLayout;
	
	private TextField worksetFilterField;
	private Button worksetFilterSettingsButton;
	
	private Tree<NodeWrapper> projectTree;
	private Grid<IWorkSet> worksetGrid;
	private ProjectTreeDataProvider projectTreeDataProvider;
	private WorksetDataProvider worksetDataProvider;
	
	private Button userLayoutSettings;
	private Button addWorksetButton;
	private Button delWorksetButton;
	private Button downloadWorksetButton;
	
	
	public CreateDocLayout(IDataProjectService projectService, 
							IWorkSetService worksetService, 
							IDataUserSettigsService dataUserSettigsService, 
							MessageSource messageSource) {
		
		this.projectService = projectService;
		this.dataUserSettigsService = dataUserSettigsService;
		this.messageSource = messageSource;
		docSettingsJson = dataUserSettigsService.getUserSettings();
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
		leftLayot.setMargin(false);
		leftLayot.setSpacing(false);
		leftLayot.setSizeFull();
		
		HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();
		splitPanel.setSizeFull();
		splitPanel.setSplitPosition(docSettingsJson.getLeftSplitPosition(), Unit.PERCENTAGE);
		splitPanel.setMinSplitPosition(LEFT_MIN_SPLIT_POSITION, Unit.PERCENTAGE);
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
		projectTree.addSelectionListener(listener -> {
			Optional<NodeWrapper> selected = listener.getFirstSelectedItem();
			if (selected.isPresent()) {
				worksetDataProvider.setParentNode(selected.get());
			}
			worksetDataProvider.refreshAll();
		});
		
		panel.setContent(projectTree);
		panel.setSizeFull();
		return panel;
	}
	
	
	private Component createWorksetGrid() {
		worksetGrid = new Grid<>();
		UserSettingsJson userSettings = dataUserSettigsService.getUserSettings();
		List<ColumnSettings> columnSettings = userSettings.getLeftWorksetColumns();
		
		columnSettings.sort((cs1, cs2) -> 
			Integer.compare(cs1.getOrderIndex(), cs2.getOrderIndex())
		);
		columnSettings.forEach(this::addWorksetGridColumn);
		worksetGrid.setSizeFull();
		worksetGrid.setHeightByRows(WORKSET_GRID_ROWS);
		worksetGrid.setColumnReorderingAllowed(true);
		worksetDataProvider.setShownColumns(columnSettings);
		worksetGrid.setDataProvider(worksetDataProvider);
		// test header groups
		userSettings.getLeftColumnHeaders();
		addWorksetHeaderColumns(userSettings);
		
		return worksetGrid;
	}

	private void addWorksetHeaderColumns(UserSettingsJson userSettings) {
		final String headStyle = "v-grid-header-align-left";
		if (userSettings.hasLeftColumnHeaders()) {
			List<ColumnHeaderGroup> groups = userSettings.getLeftColumnHeaders();
			Deque<List<ColumnHeaderGroup>> childGroups = new LinkedList<>();
			childGroups.push(groups);
		
			HeaderRow headerRow = worksetGrid.prependHeaderRow();
			headerRow.setStyleName(headStyle);
			
			while (!childGroups.isEmpty()) {
				HeaderRow subRow = worksetGrid.prependHeaderRow();
				subRow.setStyleName(headStyle);
				List<ColumnHeaderGroup> list = childGroups.pop();
				Iterator<ColumnHeaderGroup> it = list.iterator();
				
				while (it.hasNext()) {
					ColumnHeaderGroup g = it.next();
					// TODO: fix the bug
					if (g.hasChildrenGroups()) {
						childGroups.push(g.getChildren());
						HeaderCell groupCell = subRow.join(getWorksetColumnIds(g.getChildren()));
						groupCell.setText(g.getCaption());
						it = g.getChildren().iterator();
					
					} else if (g.hasColumns()) {
							HeaderCell groupCell = headerRow.join(g.getColumns().stream().map(column -> 
									column.getEntityFieldName()
										.substring(IWorkSet.ENTITYNAME_DOT.length()))
											.toArray(String[]::new));
							
							groupCell.setText(g.getCaption());
							childGroups.pollFirst();
					}
				}
			}
		}
	}
	
	private String[] getWorksetColumnIds(List<ColumnHeaderGroup> groups) {
		Deque<List<ColumnHeaderGroup>> childGroups = new LinkedList<>();
		childGroups.push(groups);
		Set<String> columnIds = new HashSet<>();
		
		while (!childGroups.isEmpty()) {
			List<ColumnHeaderGroup> list = childGroups.pop();
			Iterator<ColumnHeaderGroup> it = list.listIterator();
			while (it.hasNext()) {
				ColumnHeaderGroup g = it.next();
				
				if (g.hasChildrenGroups()) {
					childGroups.push(g.getChildren());
					it = g.getChildren().iterator();
					
				} else if (g.hasColumns()) {
					columnIds.addAll(g.getColumns().stream().map(c -> 
								c.getEntityFieldName()
									.substring(IWorkSet.ENTITYNAME_DOT.length())).collect(Collectors.toList()));
					//childGroups.pollFirst();
				}
			}
		}
		
		return columnIds.toArray(new String[0]);
	}

	private void addWorksetGridColumn(ColumnSettings settings) {
		switch (settings.getEntityFieldName()) {		
		case IWorkSet.FIELD_NAME:
			addWorksetGridColumn(settings, IWorkSet::getName, I18N_WORKSET_COLUMN_NAME);
			break;
		case IWorkSet.FIELD_CODE:
			addWorksetGridColumn(settings, IWorkSet::getCode, I18N_WORKSET_COLUMN_CODE);
			break;
		case IWorkSet.FIELD_PIR:
			addWorksetGridColumn(settings, IWorkSet::getPirCaption, I18N_WORKSET_COLUMN_PIR);
			break;
		case IWorkSet.FIELD_SMR:
			addWorksetGridColumn(settings, IWorkSet::getSmrCaption, I18N_WORKSET_COLUMN_SMR);
			break;
		case IWorkSet.FIELD_PLAN_OBJECT:
			addWorksetGridColumn(settings, IWorkSet::getPlanObject, I18N_WORKSET_COLUMN_PLANOBJ);
			break;
		case IWorkSet.FIELD_ID:
			addWorksetGridColumn(settings, IWorkSet::getId, I18N_WORKSET_COLUMN_ID);
			break;
		case IWorkSet.FIELD_VERSION:
			addWorksetGridColumn(settings, IWorkSet::getVersion, I18N_WORKSET_COLUMN_VERSION);
			break;
		case IWorkSet.FIELD_CREATE_DATE:
			addWorksetGridColumn(settings, IWorkSet::getCreateDate, I18N_WORKSET_COLUMN_CREATEDATE);
			break;
		case IWorkSet.FIELD_CHANGE_DATE:
			addWorksetGridColumn(settings, IWorkSet::getChangeDate, I18N_WORKSET_COLUMN_CHANGEDATE);
			break;
			default:
		}
	}
	
	private <T> void addWorksetGridColumn(ColumnSettings settings, ValueProvider<IWorkSet, T> provider, String i18nCaption) {
		Column<IWorkSet, T> column = worksetGrid.addColumn(provider);
		column.setSortProperty(settings.getEntityFieldName());
		column.setSortable(true);
		column.setCaption(getI18nText(i18nCaption));
		Double width = settings.getWidth();
		
		if (!Objects.isNull(width) && Double.isFinite(width) && width > 1) {
			column.setWidth(width);
		} else {
			column.setWidthUndefined();
		}
		
		if (!settings.isShown()) {	
			column.setHidden(true);
		}
		
		column.setId(settings.getEntityFieldName().substring(IWorkSet.ENTITYNAME_DOT.length()));
	}
	
	private Component createLeftHeadFeautures() {
		AbsoluteLayout layout = new AbsoluteLayout();
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		layout.setStyleName("gzpn-head");
		layout.setHeight(50.0f, Unit.PIXELS);
		layout.setWidth(100.f, Unit.PERCENTAGE);
		horizontalLayout.addComponent(createWorksetFilter());
		horizontalLayout.addComponent(createWorksetButtons());
		horizontalLayout.addComponent(createExcelButton());
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
			worksetDataProvider.getFilter().setCommonTextFilter(e.getValue());
			worksetDataProvider.refreshAll();
			logger.debug("[worksetFilterField.ValueChangeListener] {}", e.getValue());
		});
		return searchComp;
	}

	private Component createWorksetButtons() {
		JoinedLayout<Button, Button> bottons = new JoinedLayout<>();
		addWorksetButton = new Button(VaadinIcons.PLUS);
		delWorksetButton = new Button(VaadinIcons.MINUS);
		addWorksetButton.setDescription(getI18nText(I18N_ADDWORKSETBUTTON_DESC));
		delWorksetButton.setDescription(getI18nText(I18N_DELWORKSETBUTTON_DESC));
		addWorksetButton.setEnabled(false);
		delWorksetButton.setEnabled(false);
		
		bottons.addComponent(addWorksetButton);
		bottons.addComponent(delWorksetButton);
		
		return bottons;
	}

	private Component createExcelButton() {
		downloadWorksetButton = new Button(VaadinIcons.TABLE);
		downloadWorksetButton.setDescription(getI18nText(I18N_DOWNLOADWORKSETBUTTON_DESC));
		StreamResource excelStreamResource = new StreamResource(
				(StreamResource.StreamSource) () -> Exporter.exportAsExcel(worksetGrid), "workset.xls");
		FileDownloader excelFileDownloader = new FileDownloader(excelStreamResource);
		excelFileDownloader.extend(downloadWorksetButton);
		
		return downloadWorksetButton;
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
	
	private IDataProjectService projectService;
	private IDataUserSettigsService dataUserSettigsService;
	private MessageSource messageSource;
	private TreeGrid<NodeWrapper> docTree;
	private ProjectTreeDataProvider dataProvider;
	private ConfigurableFilterDataProvider<NodeWrapper, NodeFilter, NodeFilter> configurableFilterDataProvider;
	private CreateDocLayout parent;
	
	public WorkSetDocumentation(IDataProjectService projectService, IDataUserSettigsService dataUserSettigsService, MessageSource messageSource) {
		this.projectService = projectService;
		this.dataUserSettigsService = dataUserSettigsService;
		this.messageSource = messageSource;
		NodeWrapper treeSettings = dataUserSettigsService.getUserSettings().getRightHierarchySettings();
		
		dataProvider = new ProjectTreeDataProvider(projectService, treeSettings);
		configurableFilterDataProvider = dataProvider.withConfigurableFilter(
				(NodeFilter queryFilter, NodeFilter configuredFilter) -> configuredFilter
		);
	}
	
	public void setParent(CreateDocLayout parent) {
		this.parent = parent;
	}
	
	public void init() {
		
	}
	
	private String getI18nText(String key) {
		return messageSource.getMessage(key, null, VaadinSession.getCurrent().getLocale());
	}
}