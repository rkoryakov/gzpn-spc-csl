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
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.Registration;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.HeaderCell;
import com.vaadin.ui.components.grid.HeaderRow;
import com.vaadin.ui.themes.ValoTheme;

import ru.gzpn.spc.csl.model.interfaces.IDocument;
import ru.gzpn.spc.csl.model.interfaces.IWorkSet;
import ru.gzpn.spc.csl.model.jsontypes.ColumnHeaderGroup;
import ru.gzpn.spc.csl.model.jsontypes.ColumnSettings;
import ru.gzpn.spc.csl.model.jsontypes.CreateDocSettingsJson;
import ru.gzpn.spc.csl.model.utils.NodeWrapper;
import ru.gzpn.spc.csl.services.bl.DocumentService;
import ru.gzpn.spc.csl.services.bl.interfaces.ICreateDocService;
import ru.gzpn.spc.csl.services.bl.interfaces.IProjectService;
import ru.gzpn.spc.csl.services.bl.interfaces.IUserSettigsService;
import ru.gzpn.spc.csl.services.bl.interfaces.IWorkSetService;
import ru.gzpn.spc.csl.ui.common.DraggableTree;
import ru.gzpn.spc.csl.ui.common.I18n;
import ru.gzpn.spc.csl.ui.common.JoinedLayout;
import ru.gzpn.spc.csl.ui.common.data.export.Exporter;

public class CreateDocLayout extends HorizontalSplitPanel implements I18n {
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
	
	private static final int WORKSET_GRID_ROWS = 15;
	private static final int WORKSET_GRID_ROW_HEIGHT = 38;
	private static final int MAIN_MIN_SPLIT_POSITION = 30;
	private static final int LEFT_MIN_SPLIT_POSITION = 25;
	private static final int HEAD_ROW_HEIGHT = 37;
	
	private String currentUser;
	
	private IProjectService projectService;
	private IUserSettigsService settingsService;
	private MessageSource messageSource;
	private IWorkSetService worksetService;
	private ICreateDocService createDocService;
	
	private CreateDocSettingsJson docSettingsJson;
	
	private VerticalLayout leftLayout;
	private VerticalLayout rightLayout;
	
	private TextField worksetFilterField;
	private Button worksetFilterSettingsButton;
	
	private DraggableTree<NodeWrapper> projectTree;
	private Grid<IWorkSet> worksetGrid;
	private ProjectTreeDataProvider projectTreeDataProvider;
	private WorksetDataProvider worksetDataProvider;
	
	private Button userLayoutSettings;
	private Button addWorksetButton;
	private Button delWorksetButton;
	private Button downloadWorksetButton;
	private Panel projectTreePanel;
	private Registration preojectTreeItemSelectRegistration;
	
	public CreateDocLayout(ICreateDocService createDocService) {
		this.projectService = createDocService.getProjectService();
		this.settingsService = createDocService.getUserSettingsService();
		this.messageSource = createDocService.getMessageSource();
		this.worksetService = createDocService.getWorkService();
		this.createDocService = createDocService;
		
		this.currentUser = settingsService.getCurrentUser();
		
		docSettingsJson = settingsService.getUserSettings();
		NodeWrapper treeSettings = docSettingsJson.getLeftTreeGroup();
		
		projectTreeDataProvider = new ProjectTreeDataProvider(projectService, treeSettings);
		worksetDataProvider = new WorksetDataProvider(worksetService);

		setSizeFull();
		
		setSplitPosition(docSettingsJson.getMainSplitPosition(), Unit.PERCENTAGE);
		addSplitPositionChangeListener(this::onMainSplitPositionChange);
		setMinSplitPosition(MAIN_MIN_SPLIT_POSITION, Unit.PERCENTAGE);
		addStyleName(ValoTheme.SPLITPANEL_LARGE);
		setFirstComponent(createLeftLayout());
		setSecondComponent(createRightLayout());
	}

	public void onMainSplitPositionChange(SplitPositionChangeEvent event) {
		docSettingsJson.setMainSplitPosition((int)event.getSplitPosition());
		saveUserSettings();
	}
	
	public void onLeftSplitPositionChange(SplitPositionChangeEvent event) {
		docSettingsJson.setLeftSplitPosition((int)event.getSplitPosition());
		saveUserSettings();
	}
	
	public void saveUserSettings() {
		settingsService.saveCreateDocSettings(this.currentUser, docSettingsJson);
	}

	public Component createLeftLayout() {
		leftLayout = new VerticalLayout();
		leftLayout.setMargin(false);
		leftLayout.setSpacing(false);
		leftLayout.setSizeFull();
		
		HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();
		splitPanel.setSizeFull();
		splitPanel.setSplitPosition(docSettingsJson.getLeftSplitPosition(), Unit.PERCENTAGE);
		splitPanel.setMinSplitPosition(LEFT_MIN_SPLIT_POSITION, Unit.PERCENTAGE);
		splitPanel.addSplitPositionChangeListener(this::onLeftSplitPositionChange);
		splitPanel.addComponent(createProjectTree());
		splitPanel.addComponent(createWorksetGrid());
		
		leftLayout.addComponent(createLeftHeadFeautures());
		leftLayout.addComponent(splitPanel);
		
		return leftLayout;
	}
	
	public Component createProjectTree() {
		projectTreePanel = new Panel(getI18nText(I18N_TREE_CAPTION, messageSource));
		projectTree = new DraggableTree<>();
		refreshProjectTree();
		projectTreePanel.setContent(projectTree);
		projectTreePanel.setSizeFull();
		return projectTreePanel;
	}
	
	public void refreshProjectTree() {
		docSettingsJson = settingsService.getUserSettings();
		NodeWrapper treeSettings = docSettingsJson.getLeftTreeGroup();
		projectTreeDataProvider = new ProjectTreeDataProvider(projectService, treeSettings);
		projectTree.setDataProvider(projectTreeDataProvider);
		projectTreePanel.setCaption(getI18nText(I18N_TREE_CAPTION, messageSource));
		projectTree.setItemCaptionGenerator(NodeWrapper::getNodeCaption);
		projectTree.setItemIconGenerator(new ProjectItemIconGenerator());
		projectTreePanel.setSizeFull();
		projectTree.setHeight(WORKSET_GRID_ROW_HEIGHT * WORKSET_GRID_ROWS + HEAD_ROW_HEIGHT - WORKSET_GRID_ROW_HEIGHT, Unit.PIXELS);
		if (Objects.nonNull(preojectTreeItemSelectRegistration)) {
			preojectTreeItemSelectRegistration.remove();
		}
		preojectTreeItemSelectRegistration = projectTree.addSelectionListener(listener -> {
			Optional<NodeWrapper> selected = listener.getFirstSelectedItem();
			if (selected.isPresent()) {
				worksetDataProvider.setParentNode(selected.get());
			}
			worksetDataProvider.refreshAll();
		});
	}
	
	public Component createWorksetGrid() {
		worksetGrid = new Grid<>();
		refreshWorksetGrid();
		return worksetGrid;
	}

	public void refreshWorksetGrid() {
		worksetDataProvider = new WorksetDataProvider(worksetService);
		CreateDocSettingsJson userSettings = settingsService.getUserSettings();
		List<ColumnSettings> columnSettings = userSettings.getLeftResultColumns();
		
		columnSettings.sort((cs1, cs2) -> 
			Integer.compare(cs1.getOrderIndex(), cs2.getOrderIndex())
		);
		worksetGrid.removeAllColumns();
		columnSettings.forEach(this::addWorksetGridColumn);
		worksetGrid.setSizeFull();
		worksetGrid.setHeightByRows(WORKSET_GRID_ROWS);
		worksetGrid.setColumnReorderingAllowed(true);
		worksetDataProvider.setShownColumns(columnSettings);
		worksetGrid.setDataProvider(worksetDataProvider);
		// test column headers
		userSettings.getLeftColumnHeaders();
		createWorksetHeaderColumns(userSettings);
	}
	
	public void createWorksetHeaderColumns(CreateDocSettingsJson userSettings) {
		if (userSettings.hasLeftColumnHeaders()) {
			refreshColumnHeaderGroups(userSettings.getLeftColumnHeaders());
		}
		worksetGrid.setHeightByRows(WORKSET_GRID_ROWS - worksetGrid.getHeaderRowCount() + 1);
	}

	public void refreshColumnHeaderGroups(List<ColumnHeaderGroup> groups) {
		final String headStyle = "v-grid-header-align-left";
		Deque<List<ColumnHeaderGroup>> childGroups = new LinkedList<>();
		childGroups.push(groups);
		removePrepandedHeaderRows();
		
		HeaderRow headerRow = worksetGrid.prependHeaderRow();
		headerRow.setStyleName(headStyle);

		while (!childGroups.isEmpty()) {
			List<ColumnHeaderGroup> list = childGroups.pop();
			Iterator<ColumnHeaderGroup> it = list.iterator();
			
			while (it.hasNext()) {
				ColumnHeaderGroup g = it.next();

				if (g.hasChildrenGroups()) {
					HeaderRow subRow = worksetGrid.prependHeaderRow();
					subRow.setStyleName(headStyle);
					childGroups.push(g.getChildren());
					HeaderCell groupCell = subRow.join(getWorksetColumnIds(g.getChildren()));
					groupCell.setText(g.getCaption());
					break;
				
				} else if (g.hasColumns()) {
					
						HeaderCell groupCell = headerRow.join(g.getColumns().stream().map(column -> 
								column.getEntityFieldName())
										.toArray(String[]::new));
						
						groupCell.setText(g.getCaption());
						childGroups.pollFirst();
				}
			}
		}
	}
	
	public void removePrepandedHeaderRows() {
		int count = worksetGrid.getHeaderRowCount();
		if (count > 1) {
			for (int i = 0; i < count - 1; i ++) {
				worksetGrid.removeHeaderRow(0);
			}
		}
	}
	
	/**
	 * Get column Ids that are contained in the given head groups:
	 *  _____________________________________
	 * |	 Header1	 |  	Header2	     |
	 * |colum1 | column2 | column3 | column4 |
	 * 
	 */
	public String[] getWorksetColumnIds(List<ColumnHeaderGroup> groups) {
		Set<String> columnIds = new HashSet<>();

		for (int i = 0; i < groups.size(); i ++) {
			Iterator<ColumnHeaderGroup> it = groups.listIterator(i);
			while (it.hasNext()) {
				ColumnHeaderGroup g = it.next();
				if (g.hasChildrenGroups()) {
					it = g.getChildren().iterator();
				} else if (g.hasColumns()) {
					columnIds.addAll(g.getColumns().stream().map(c -> c.getEntityFieldName()).collect(Collectors.toSet()));
				}
			}
		}
		return columnIds.toArray(new String[0]);
	}

	public void addWorksetGridColumn(ColumnSettings settings) {
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
	
	public <T> void addWorksetGridColumn(ColumnSettings settings, ValueProvider<IWorkSet, T> provider, String i18nCaption) {
		Column<IWorkSet, T> column = worksetGrid.addColumn(provider);
		column.setSortProperty(settings.getEntityFieldName());
		column.setSortable(true);
		column.setCaption(getI18nText(i18nCaption, messageSource));
		Double width = settings.getWidth();
		
		if (Objects.nonNull(width) && Double.isFinite(width) && width > 1) {
			column.setWidth(width);
		} else {
			column.setWidthUndefined();
		}
		
		if (!settings.isShown()) {	
			column.setHidden(true);
		}
		
		column.setId(settings.getEntityFieldName());
	}
	
	public Component createLeftHeadFeautures() {
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

	public Component createWorksetFilter() {
		worksetFilterField = new TextField();
		worksetFilterField.setWidth(200.0f, Unit.PIXELS);
		worksetFilterSettingsButton = new Button();
		worksetFilterSettingsButton.setIcon(VaadinIcons.FILTER);
		worksetFilterSettingsButton.setDescription(getI18nText(I18N_SEARCHSETTINGS_DESC, messageSource));
		JoinedLayout<TextField, Button> searchComp = new JoinedLayout<>(worksetFilterField, worksetFilterSettingsButton);
		worksetFilterField.setPlaceholder(getI18nText(I18N_SEARCHFIELD_PLACEHOLDER, messageSource));
		
		worksetFilterField.addValueChangeListener(e -> {
			worksetDataProvider.getFilter().setCommonTextFilter(e.getValue());
			worksetDataProvider.refreshAll();
		});
		return searchComp;
	}

	public Component createWorksetButtons() {
		JoinedLayout<Button, Button> buttons = new JoinedLayout<>();
		addWorksetButton = new Button(VaadinIcons.PLUS);
		delWorksetButton = new Button(VaadinIcons.MINUS);
		addWorksetButton.setDescription(getI18nText(I18N_ADDWORKSETBUTTON_DESC, messageSource));
		delWorksetButton.setDescription(getI18nText(I18N_DELWORKSETBUTTON_DESC, messageSource));
		addWorksetButton.setEnabled(false);
		delWorksetButton.setEnabled(false);
		
		buttons.addComponent(addWorksetButton);
		buttons.addComponent(delWorksetButton);
		
		return buttons;
	}

	public Component createExcelButton() {
		downloadWorksetButton = new Button(VaadinIcons.TABLE);
		downloadWorksetButton.setDescription(getI18nText(I18N_DOWNLOADWORKSETBUTTON_DESC, messageSource));
		StreamResource excelStreamResource = new StreamResource(
				(StreamResource.StreamSource) () -> Exporter.exportAsExcel(worksetGrid), "workset.xls");
		FileDownloader excelFileDownloader = new FileDownloader(excelStreamResource);
		excelFileDownloader.extend(downloadWorksetButton);
		
		return downloadWorksetButton;
	}
	
	public Component createSettingsButton() {
		userLayoutSettings = new Button();
		userLayoutSettings.setIcon(VaadinIcons.COG_O);
		userLayoutSettings.setDescription(getI18nText(I18N_USERLAYOUTSETTINGS_DESC, messageSource));
		userLayoutSettings.addClickListener(event -> {
			CreateDocSettingsWindow settingsWindow = new CreateDocSettingsWindow(settingsService, messageSource);
			settingsWindow.addOnSaveListener(closeEvent -> 
				refreshUiElements()
			);
			getUI().getUI().addWindow(settingsWindow);
		});
		return userLayoutSettings;
	}
	
	public void refreshUiElements() {
		refreshProjectTree();
		refreshWorksetGrid();
	}

	public Component createRightLayout() {
		rightLayout = new WorkSetDocumentation(this.createDocService);
		rightLayout.setParent(this);
		return rightLayout;
	}
}

class WorkSetDocumentation extends VerticalLayout implements I18n {
	private static final long serialVersionUID = -7505276213420043371L;

	public static final String I18N_DOCUMENT_COLUMN_NAME = "createdoc.DocCreatingLayout.documentGrid.columns.name";
	public static final String I18N_DOCUMENT_COLUMN_CODE = "createdoc.DocCreatingLayout.documentGrid.columns.code";
	
	private static final String I18N_DOCUMENT_COLUMN_TYPE = "createdoc.DocCreatingLayout.documentGrid.columns.type";
	private static final String I18N_DOCUMENT_COLUMN_WORK = "createdoc.DocCreatingLayout.documentGrid.columns.work";
	private static final String I18N_DOCUMENT_COLUMN_WORKSET = "createdoc.DocCreatingLayout.documentGrid.columns.workset";
	private static final String I18N_DOCUMENT_COLUMN_ID = "createdoc.DocCreatingLayout.documentGrid.columns.id";
	private static final String I18N_DOCUMENT_COLUMN_VERSION = "createdoc.DocCreatingLayout.documentGrid.columns.version";
	private static final String I18N_DOCUMENT_COLUMN_CREATEDATE = "createdoc.DocCreatingLayout.documentGrid.columns.createdate";
	private static final String I18N_DOCUMENT_COLUMN_CHANGEDATE = "createdoc.DocCreatingLayout.documentGrid.columns.changedate";
	
	private static final double DOCUMENT_GRID_ROWS = 10;
	
	private IUserSettigsService settingsService;
	private MessageSource messageSource;
	private DocumentService documentService;
	
	private Grid<IDocumentPresenter> documentsGrid;	
	private DocumentsDataProvider documnentsDataProvider;
	private CreateDocLayout parent;
	
	public WorkSetDocumentation(ICreateDocService service) {
		this.documentService = service.getDocumentService();
		this.settingsService = service.getUserSettingsService();
		this.messageSource = service.getMessageSource();
		refreshUiElements();
	}
	
	public void setParent(CreateDocLayout parent) {
		this.parent = parent;
	}
	
	public void refreshUiElements() {
		this.removeAllComponents();
		setMargin(false);
		setSpacing(false);
		setSizeFull();
		
		settingsService.getUserSettings().getRightTreeGroup();
		addComponent(createDocumentGrid());
	}

	public Component createDocumentGrid() {
		documentsGrid = new Grid<>();
		refreshDocumentGrid();
		return documentsGrid;
	}

	private void refreshDocumentGrid() {
		documentsGrid.removeAllColumns();
		documnentsDataProvider = new DocumentsDataProvider(documentService);
		CreateDocSettingsJson userSettings = settingsService.getUserSettings();
		List<ColumnSettings> columnSettings = userSettings.getRightResultColumns();
		
		columnSettings.sort((cs1, cs2) -> 
			Integer.compare(cs1.getOrderIndex(), cs2.getOrderIndex())
		);
		columnSettings.forEach(this::addDocumentGridColumn);
		documentsGrid.setSizeFull();
		documentsGrid.setHeightByRows(DOCUMENT_GRID_ROWS);
		documentsGrid.setColumnReorderingAllowed(true);
		documnentsDataProvider.setShownColumns(columnSettings);
		documentsGrid.setDataProvider(documnentsDataProvider);

		addDocumentHeaderColumns(userSettings);
	}
	
	
	public void addDocumentGridColumn(ColumnSettings settings) {
		switch (settings.getEntityFieldName()) {		
		case IDocument.FIELD_NAME:
			addDocumnentGridColumn(settings, IDocumentPresenter::getName, I18N_DOCUMENT_COLUMN_NAME);
			break;
		case IDocument.FIELD_CODE:
			addDocumnentGridColumn(settings, IDocumentPresenter::getCode, I18N_DOCUMENT_COLUMN_CODE);
			break;
		case IDocument.FIELD_TYPE:
			addDocumnentGridColumn(settings, field -> field.getTypeText(this.messageSource), I18N_DOCUMENT_COLUMN_TYPE);
			break;
		case IDocument.FIELD_WORK:
			addDocumnentGridColumn(settings, IDocumentPresenter::getWorkText, I18N_DOCUMENT_COLUMN_WORK);
			break;
		case IDocument.FIELD_WORKSET:
			addDocumnentGridColumn(settings, IDocumentPresenter::getWorksetText, I18N_DOCUMENT_COLUMN_WORKSET);
			break;
		case IDocument.FIELD_ID:
			addDocumnentGridColumn(settings, IDocumentPresenter::getId, I18N_DOCUMENT_COLUMN_ID);
			break;
		case IDocument.FIELD_VERSION:
			addDocumnentGridColumn(settings, IDocumentPresenter::getVersion, I18N_DOCUMENT_COLUMN_VERSION);
			break;
		case IDocument.FIELD_CREATE_DATE:
			addDocumnentGridColumn(settings, IDocumentPresenter::getCreateDate, I18N_DOCUMENT_COLUMN_CREATEDATE);
			break;
		case IDocument.FIELD_CHANGE_DATE:
			addDocumnentGridColumn(settings, IDocumentPresenter::getChangeDate, I18N_DOCUMENT_COLUMN_CHANGEDATE);
			break;
			default:
		}
	}
	
	public <T> void addDocumnentGridColumn(ColumnSettings settings, ValueProvider<IDocumentPresenter, T> provider, String i18nCaption) {
		Column<IDocumentPresenter, T> column = documentsGrid.addColumn(provider);
		column.setSortProperty(settings.getEntityFieldName());
		column.setSortable(true);
		column.setCaption(getI18nText(i18nCaption, messageSource));
		Double width = settings.getWidth();
		
		if (Objects.nonNull(width) && Double.isFinite(width) && width > 1) {
			column.setWidth(width);
		} else {
			column.setWidthUndefined();
		}
		
		if (!settings.isShown()) {	
			column.setHidden(true);
		}
		
		column.setId(settings.getEntityFieldName());
	}
	
	public void addDocumentHeaderColumns(CreateDocSettingsJson userSettings) {
		if (userSettings.hasLeftColumnHeaders()) {
			refreshColumnHeaderGroups(userSettings.getRightColumnHeaders());
		}
		documentsGrid.setHeightByRows(DOCUMENT_GRID_ROWS - documentsGrid.getHeaderRowCount() + 1);
	}

	public void refreshColumnHeaderGroups(List<ColumnHeaderGroup> groups) {
		final String headStyle = "v-grid-header-align-left";
		Deque<List<ColumnHeaderGroup>> childGroups = new LinkedList<>();
		childGroups.push(groups);
		removePrepandedHeaderRows();
		
		HeaderRow headerRow = documentsGrid.prependHeaderRow();
		headerRow.setStyleName(headStyle);

		while (!childGroups.isEmpty()) {
			List<ColumnHeaderGroup> list = childGroups.pop();
			Iterator<ColumnHeaderGroup> it = list.iterator();
			
			while (it.hasNext()) {
				ColumnHeaderGroup g = it.next();

				if (g.hasChildrenGroups()) {
					HeaderRow subRow = documentsGrid.prependHeaderRow();
					subRow.setStyleName(headStyle);
					childGroups.push(g.getChildren());
					HeaderCell groupCell = subRow.join(getDocumentColumnIds(g.getChildren()));
					groupCell.setText(g.getCaption());
					break;
				
				} else if (g.hasColumns()) {
					
						HeaderCell groupCell = headerRow.join(g.getColumns().stream().map(column -> 
								column.getEntityFieldName())
										.toArray(String[]::new));
						
						groupCell.setText(g.getCaption());
						childGroups.pollFirst();
				}
			}
		}
	}
	
	public void removePrepandedHeaderRows() {
		int count = documentsGrid.getHeaderRowCount();
		if (count > 1) {
			for (int i = 0; i < count - 1; i ++) {
				documentsGrid.removeHeaderRow(0);
			}
		}
	}
	
	/**
	 * Get column Ids that are contained in the given head groups:
	 *  _____________________________________
	 * |	 Header1	 |  	Header2	     |
	 * |colum1 | column2 | column3 | column4 |
	 * 
	 */
	public String[] getDocumentColumnIds(List<ColumnHeaderGroup> groups) {
		Set<String> columnIds = new HashSet<>();

		for (int i = 0; i < groups.size(); i ++) {
			Iterator<ColumnHeaderGroup> it = groups.listIterator(i);
			while (it.hasNext()) {
				ColumnHeaderGroup g = it.next();
				if (g.hasChildrenGroups()) {
					it = g.getChildren().iterator();
				} else if (g.hasColumns()) {
					columnIds.addAll(g.getColumns().stream().map(c -> c.getEntityFieldName()).collect(Collectors.toSet()));
				}
			}
		}
		return columnIds.toArray(new String[0]);
	}
	
}
