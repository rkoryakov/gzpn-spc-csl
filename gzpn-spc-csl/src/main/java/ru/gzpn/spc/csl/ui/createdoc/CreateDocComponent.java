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

import ru.gzpn.spc.csl.model.dataproviders.ProjectTreeDataProvider;
import ru.gzpn.spc.csl.model.dataproviders.WorksetDataProvider;
import ru.gzpn.spc.csl.model.enums.Entities;
import ru.gzpn.spc.csl.model.interfaces.IBaseEntity;
import ru.gzpn.spc.csl.model.interfaces.ICProject;
import ru.gzpn.spc.csl.model.interfaces.IPlanObject;
import ru.gzpn.spc.csl.model.interfaces.IWorkSet;
import ru.gzpn.spc.csl.model.jsontypes.ColumnHeaderGroup;
import ru.gzpn.spc.csl.model.jsontypes.ColumnSettings;
import ru.gzpn.spc.csl.model.jsontypes.CreateDocSettingsJson;
import ru.gzpn.spc.csl.model.presenters.interfaces.IWorkSetPresenter;
import ru.gzpn.spc.csl.model.utils.NodeWrapper;
import ru.gzpn.spc.csl.services.bl.interfaces.ICreateDocService;
import ru.gzpn.spc.csl.services.bl.interfaces.IProjectService;
import ru.gzpn.spc.csl.services.bl.interfaces.IUserSettingsService;
import ru.gzpn.spc.csl.services.bl.interfaces.IWorkSetService;
import ru.gzpn.spc.csl.ui.common.DraggableTree;
import ru.gzpn.spc.csl.ui.common.I18n;
import ru.gzpn.spc.csl.ui.common.JoinedLayout;
import ru.gzpn.spc.csl.ui.common.data.export.Exporter;

public class CreateDocComponent extends HorizontalSplitPanel implements I18n {
	private static final long serialVersionUID = -883906550551450076L;
	public static final Logger logger = LoggerFactory.getLogger(CreateDocComponent.class);
	
	public static final String I18N_SEARCHFIELD_PLACEHOLDER = "createdoc.CreateDocLayout.worksetFilterField.placeholder";
	public static final String I18N_SEARCHSETTINGS_DESC = "createdoc.CreateDocLayout.worksetFilterSettingsButton.desc";
	public static final String I18N_USERLAYOUTSETTINGS_DESC = "createdoc.CreateDocLayout.userLayoutSettings.desc";
	public static final String I18N_ADDWORKSETBUTTON_DESC = "createdoc.CreateDocLayout.addWorksetButton.desc";
	public static final String I18N_DELWORKSETBUTTON_DESC = "createdoc.CreateDocLayout.delWorksetButton.desc";
	public static final String I18N_DOWNLOADWORKSETBUTTON_DESC = "createdoc.CreateDocLayout.downloadWorksetButton.desc";
	public static final String I18N_TREE_CAPTION = "createdoc.CreateDocLayout.projectTreeCaption";
	public static final String I18N_SAVE_ITEM_CAP = "createdoc.CreateDocLayout.saveItem.cap";
	public static final String I18N_CANCELSAVE_ITEM_CAP = "createdoc.CreateDocLayout.cancelItem.cap";
	
	private static final int WORKSET_GRID_ROWS = 15;
	private static final int WORKSET_GRID_ROW_HEIGHT = 38;
	private static final int MAIN_MIN_SPLIT_POSITION = 30;
	private static final int LEFT_MIN_SPLIT_POSITION = 25;
	private static final int HEAD_ROW_HEIGHT = 37;

	
	private String currentUser;
	
	private IProjectService projectService;
	private IUserSettingsService settingsService;
	private MessageSource messageSource;
	private IWorkSetService worksetService;
	private ICreateDocService createDocService;
	
	private CreateDocSettingsJson docSettingsJson;
	
	private VerticalLayout leftLayout;
	private WorkSetDocumentationComponent rightLayout;
	
	private TextField worksetFilterField;
	private Button worksetFilterSettingsButton;
	
	private DraggableTree<NodeWrapper> projectTree;
	private Grid<IWorkSetPresenter> worksetGrid;
	private ProjectTreeDataProvider projectTreeDataProvider;
	private WorksetDataProvider worksetDataProvider;
	
	private Button userLayoutSettings;
	private Button addWorksetButton;
	private Button delWorksetButton;
	private Button downloadWorksetButton;
	private Panel projectTreePanel;
	private Registration preojectTreeItemSelectRegistration;
	private IWorkSetPresenter selectedWorkSet;
	
	public CreateDocComponent(ICreateDocService createDocService) {
		this.projectService = createDocService.getProjectService();
		this.settingsService = createDocService.getUserSettingsService();
		this.messageSource = createDocService.getMessageSource();
		this.worksetService = createDocService.getWorkSetService();
		this.createDocService = createDocService;
		
		this.currentUser = settingsService.getCurrentUser();
		
		docSettingsJson = (CreateDocSettingsJson)settingsService.getCreateDocUserSettings(currentUser, new CreateDocSettingsJson());
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
		settingsService.save(this.currentUser, docSettingsJson);
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
		docSettingsJson = (CreateDocSettingsJson)settingsService.getCreateDocUserSettings(this.currentUser, new CreateDocSettingsJson());
		NodeWrapper treeSettings = docSettingsJson.getLeftTreeGroup();
		
		projectTreeDataProvider = new ProjectTreeDataProvider(projectService, treeSettings);
		projectTree.setDataProvider(projectTreeDataProvider);
		// expand the tree and select the previously selected item
		if (worksetDataProvider.getParentNode() != null) {
			NodeWrapper parentNode = worksetDataProvider.getParentNode();
			projectTree.select(parentNode);
			
			while (parentNode.hasParent()) {
				projectTree.expand(parentNode);
				parentNode = parentNode.getParent();
			}
			projectTree.expand(parentNode);
		}
		
		projectTreePanel.setCaption(getI18nText(I18N_TREE_CAPTION, messageSource));
		projectTree.setItemCaptionGenerator(node -> node.getNodeCaption(this.projectService, this.messageSource));
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

	public Grid<IWorkSetPresenter> getWorkSetGrid(){
		return this.worksetGrid;
	}
	
 	public void refreshWorksetGrid() {
		NodeWrapper parentNode = worksetDataProvider.getParentNode();
		worksetDataProvider = new WorksetDataProvider(worksetService);
		worksetDataProvider.setParentNode(parentNode);
		
		CreateDocSettingsJson userSettings = (CreateDocSettingsJson)settingsService.getCreateDocUserSettings(currentUser, new CreateDocSettingsJson());
		List<ColumnSettings> columnSettings = userSettings.getLeftResultColumns();
		
		columnSettings.sort((cs1, cs2) -> 
			Integer.compare(cs1.getOrderIndex(), cs2.getOrderIndex())
		);
		
		worksetGrid.removeAllColumns();
		columnSettings.forEach(this::addWorksetGridColumn);
		worksetGrid.setSizeFull();
		worksetGrid.setColumnReorderingAllowed(true);
		worksetGrid.setHeightByRows(WORKSET_GRID_ROWS);
		worksetDataProvider.setShownColumns(columnSettings);
		worksetGrid.setDataProvider(worksetDataProvider);
		worksetGrid.getEditor().setEnabled(true);
		worksetGrid.getEditor().setSaveCaption(getI18nText(I18N_SAVE_ITEM_CAP, messageSource));
		worksetGrid.getEditor().setCancelCaption(getI18nText(I18N_CANCELSAVE_ITEM_CAP, messageSource));
		
		worksetGrid.getEditor().addSaveListener(saveEvent -> {
			saveWorkSetItem(saveEvent.getBean());
		});
		worksetGrid.getEditor().addCancelListener(cancelSaveEvent -> {
			worksetGrid.getDataProvider().refreshItem(cancelSaveEvent.getBean());
		});
		
		/* WorkSet Item Select */
		worksetGrid.addSelectionListener(selectEvent -> {
			if (Objects.nonNull(this.rightLayout)) {
				Optional<IWorkSetPresenter> item = selectEvent.getFirstSelectedItem();
				if (item.isPresent()) {	
					this.selectedWorkSet = item.get();
					rightLayout.getDocumentsDataProvider().setParentWorkSet(item.get().getWorkset());
					rightLayout.getDocumentsDataProvider().refreshAll();
				}
			}
		});
		
		/* select the previously selected workset */
		if (selectedWorkSet != null) {
			worksetGrid.select(selectedWorkSet);
		}
		
		// test column headers
		userSettings.getLeftColumnHeaders();
		createWorksetHeaderColumns(userSettings);
	}
	
	private void saveWorkSetItem(IWorkSetPresenter bean) {
		this.worksetService.save(bean);
		this.worksetGrid.getDataProvider().refreshItem(bean);
	}

	public void createWorksetHeaderColumns(CreateDocSettingsJson userSettings) {
		if (userSettings.hasLeftColumnHeaders()) {
			refreshColumnHeaderGroups(userSettings.getLeftColumnHeaders());
		}
		worksetGrid.setHeightByRows(WORKSET_GRID_ROWS - worksetGrid.getHeaderRowCount() + 1);
	}

//	public void refreshColumnHeaderGroups(List<ColumnHeaderGroup> groups) {
//		final String headStyle = "v-grid-header-align-left";
//		createColumnHeaders(groups);
//		
//	}
//	
//	private List<ColumnHeaderGroup> createColumnHeaders(List<ColumnHeaderGroup> groups) {
//		
//	}
	
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
			addWorksetGridColumn(settings, IWorkSetPresenter::getName, IWorkSet.FIELD_NAME)
				.setEditorComponent(new TextField(), IWorkSetPresenter::setName).setEditable(true);
			break;
		case IWorkSet.FIELD_CODE:
			addWorksetGridColumn(settings, IWorkSetPresenter::getCode, IWorkSet.FIELD_CODE)
				.setEditorComponent(new TextField(), IWorkSetPresenter::setCode).setEditable(true);
			break;
		case IWorkSet.FIELD_PIR:
			addWorksetGridColumn(settings, IWorkSetPresenter::getPirText, IWorkSet.FIELD_PIR);
			break;
		case IWorkSet.FIELD_SMR:
			addWorksetGridColumn(settings, IWorkSetPresenter::getSmrText, IWorkSet.FIELD_SMR);
			break;
		case IPlanObject.FIELD_NAME:
			addWorksetGridColumn(settings, IWorkSetPresenter::getPlanObjectNameText, IPlanObject.FIELD_NAME);
			break;
		case IPlanObject.FIELD_CODE:
			addWorksetGridColumn(settings, IWorkSetPresenter::getPlanObjectCodeText, IPlanObject.FIELD_CODE);
			break;
		case ICProject.FIELD_NAME:
			addWorksetGridColumn(settings, IWorkSetPresenter::getCProjectNameText, ICProject.FIELD_NAME);
			break;
		case ICProject.FIELD_CODE:
			addWorksetGridColumn(settings, IWorkSetPresenter::getCProjectCodeText, ICProject.FIELD_CODE);
			break;
		case IWorkSet.FIELD_ID:
			addWorksetGridColumn(settings, IWorkSetPresenter::getId, IWorkSet.FIELD_ID);
			break;
		case IWorkSet.FIELD_VERSION:
			addWorksetGridColumn(settings, IWorkSetPresenter::getVersion, IWorkSet.FIELD_VERSION);
			break;
		case IWorkSet.FIELD_CREATE_DATE:
			addWorksetGridColumn(settings, IWorkSetPresenter::getCreateDateText, IBaseEntity.BASE_FIELD_CREATE_DATE);
			break;
		case IWorkSet.FIELD_CHANGE_DATE:
			addWorksetGridColumn(settings, IWorkSetPresenter::getChangeDateText, IBaseEntity.BASE_FIELD_CHANGE_DATE);
			break;
			default:
		}
	}
	
	public <T> Column<IWorkSetPresenter, T> addWorksetGridColumn(ColumnSettings settings, ValueProvider<IWorkSetPresenter, T> provider, String field) {
		
		Column<IWorkSetPresenter, T> column = worksetGrid.addColumn(provider);
		column.setSortProperty(settings.getEntityFieldName());
		column.setSortable(true);
		column.setCaption(Entities.getEntityFieldText(field, messageSource));
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
		return column;
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
		
		addWorksetButton.addClickListener(clickListener -> {
			WorksetDataProvider provider = (WorksetDataProvider)worksetGrid.getDataProvider();
			provider.createEmptyItem();
			provider.refreshAll();
		});
		
		delWorksetButton.addClickListener(clickListener -> {
			worksetGrid.getSelectedItems().forEach(item -> {
				worksetService.remove(item);
			});
		
			worksetGrid.deselectAll();
			((WorksetDataProvider)worksetGrid.getDataProvider()).refreshAll();
		});
		
		addWorksetButton.setDescription(getI18nText(I18N_ADDWORKSETBUTTON_DESC, messageSource));
		delWorksetButton.setDescription(getI18nText(I18N_DELWORKSETBUTTON_DESC, messageSource));
		addWorksetButton.setEnabled(true);
		delWorksetButton.setEnabled(true);
		
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
			settingsWindow.addOnSaveAndCloseListener(closeEvent -> {
				refreshUiElements();
			});
			getUI().getUI().addWindow(settingsWindow);
		});
		return userLayoutSettings;
	}
	
	public void refreshUiElements() {
		refreshProjectTree();
		refreshWorksetGrid();
	}

	public Component createRightLayout() {
		rightLayout = new WorkSetDocumentationComponent(this.createDocService);
		rightLayout.setParent(this);
		return rightLayout;
	}
}


