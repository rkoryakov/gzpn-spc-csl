package ru.gzpn.spc.csl.ui.common;

import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.MessageSource;

import com.vaadin.event.Action;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.Registration;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.HeaderCell;
import com.vaadin.ui.components.grid.HeaderRow;

import ru.gzpn.spc.csl.model.dataproviders.AbstractRegistryDataProvider;
import ru.gzpn.spc.csl.model.dataproviders.ProjectTreeDataProvider;
import ru.gzpn.spc.csl.model.interfaces.IBaseEntity;
import ru.gzpn.spc.csl.model.jsontypes.ColumnHeaderGroup;
import ru.gzpn.spc.csl.model.jsontypes.ColumnSettings;
import ru.gzpn.spc.csl.model.jsontypes.ISettingsJson;
import ru.gzpn.spc.csl.model.utils.NodeWrapper;
import ru.gzpn.spc.csl.services.bl.interfaces.IDataService;
import ru.gzpn.spc.csl.services.bl.interfaces.IProjectService;
import ru.gzpn.spc.csl.services.bl.interfaces.IUserSettigsService;
import ru.gzpn.spc.csl.ui.common.data.export.Exporter;
import ru.gzpn.spc.csl.ui.createdoc.ProjectItemIconGenerator;

@SuppressWarnings("serial")
public abstract class AbstractTreeGridComponent<T extends IBaseEntity> extends VerticalLayout implements I18n {
	
	// event actions
	public static final Action GRID_ITEM_SELECTED_ACTION = new Action("gridItemSelectedAction");
	public static final Action TREE_ITEM_SELECTED_ACTION = new Action("treeItemSelectedAction");
	private static final String I18N_SEARCHSETTINGS_DESC = "AbstractTreeGridComponent.filtersettings.desc";
	private static final String I18N_SEARCHFIELD_PLACEHOLDER = "RegisterComponent.filter.placeholder";
	private static final String I18N_DOWNLOADWORKSETBUTTON_DESC = "AbstractTreeGridComponent.downloadWorksetButton.desc";
	private static final String I18N_USERLAYOUTSETTINGS_DESC = "AbstractTreeGridComponent.settings.desc";
	private static final String I18N_TREE_CAPTION = "AbstractTreeGridComponent.tree.cap";
	private static final int WORKSET_GRID_ROW_HEIGHT = 38;
	private static final int HEAD_ROW_HEIGHT = 37;
	private static final String I18N_SAVE_ITEM_CAP = "AbstractTreeGridComponent.saveItem.cap";
	private static final String I18N_CANCELSAVE_ITEM_CAP = "AbstractTreeGridComponent.cancelSave.cap";
	
	protected DraggableTree<NodeWrapper> tree;
	protected Grid<T> grid;
	
	protected IDataService<? super T, T> gridDataService;
	protected IProjectService treeDataService;
	protected IUserSettigsService userSettingsService;
	protected MessageSource messageSource;
	protected String user;
	
	private Map<Action, Set<Listener>> listeners;
	private HorizontalLayout headFuturesLayout;
	private TextField registerFilterField;
	private Button registerFilterSettingsButton;
	private Button downloadWorksetButton;
	private Button userLayoutSettingsButton;
	private VerticalLayout body;
	private Panel treePanel;
	private Registration treeItemSelectRegistration;
	private T selectedGridItem;
	
	public abstract boolean isTreeVisible();
	public abstract AbstractRegistryDataProvider<T, ?> getGridDataProvider();
	public abstract ProjectTreeDataProvider getTreeDataProvider();
	public abstract String getExcelName();
	public abstract AbstractTreeGridSettingsWindow getSettingsWindow();
	public abstract ISettingsJson getSettings();
	public abstract float getSplitPosition();
	public abstract int getGridRowsCount();
	public abstract void createGridColumn(ColumnSettings column);
	public abstract void saveGridItem(T item);
	
	public AbstractTreeGridComponent(IProjectService treeDataService, IDataService<? super T, T> gridDataService, IUserSettigsService userSettingsService) {
		this.treeDataService = treeDataService;
		this.gridDataService = gridDataService;
		this.userSettingsService = userSettingsService;
		this.user = userSettingsService.getCurrentUser();
		this.messageSource = gridDataService.getMessageSource();
		
		setSpacing(false);
		setMargin(false);

		initEventActions();
		createHeadFutures();
		createBody();
		createFooter();
	}

	public void initEventActions() {
		listeners = new HashMap<>();
		listeners.put(GRID_ITEM_SELECTED_ACTION, new HashSet<>());
		listeners.put(TREE_ITEM_SELECTED_ACTION, new HashSet<>());
	}

	public void createHeadFutures() {
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(false);
		verticalLayout.setSpacing(false);
		AbsoluteLayout layout = new AbsoluteLayout();
		
		layout.setStyleName("gzpn-head");
		layout.setHeight(50.0f, Unit.PIXELS);
		layout.setWidth(100.f, Unit.PERCENTAGE);
		headFuturesLayout = new HorizontalLayout();
		headFuturesLayout.addComponent(createRegisterFilter());

		headFuturesLayout.addComponent(createExcelButton());
		layout.addComponent(createSettingsButton(), "top:5px; left:5px");
		layout.addComponent(headFuturesLayout, "top:5px; right:5px");
		
		verticalLayout.addComponent(layout);
		this.addComponent(verticalLayout);	
	}

	public Component createRegisterFilter() {
		registerFilterField = new TextField();
		registerFilterField.setWidth(300.0f, Unit.PIXELS);
		registerFilterSettingsButton = new Button();
		registerFilterSettingsButton.setIcon(VaadinIcons.FILTER);
		registerFilterSettingsButton.setDescription(getI18nText(I18N_SEARCHSETTINGS_DESC, messageSource));
		JoinedLayout<TextField, Button> searchComp = new JoinedLayout<>(registerFilterField, registerFilterSettingsButton);
		registerFilterField.setPlaceholder(getI18nText(I18N_SEARCHFIELD_PLACEHOLDER, messageSource));
		
		registerFilterField.addValueChangeListener(e -> {
			getGridDataProvider().getFilter().setCommonTextFilter(e.getValue());
			getGridDataProvider().refreshAll();
		});
		return searchComp;
	}

	public Component createExcelButton() {
		downloadWorksetButton = new Button(VaadinIcons.TABLE);
		downloadWorksetButton.setDescription(getI18nText(I18N_DOWNLOADWORKSETBUTTON_DESC, messageSource));
		StreamResource excelStreamResource = new StreamResource(
				(StreamResource.StreamSource) () -> Exporter.exportAsExcel(grid), getExcelName());
		
		FileDownloader excelFileDownloader = new FileDownloader(excelStreamResource);
		excelFileDownloader.extend(downloadWorksetButton);
		
		return downloadWorksetButton;
	}

	public Component createSettingsButton() {
		userLayoutSettingsButton = new Button();
		userLayoutSettingsButton.setIcon(VaadinIcons.COG_O);
		userLayoutSettingsButton.setDescription(getI18nText(I18N_USERLAYOUTSETTINGS_DESC, messageSource, I18N_USERLAYOUTSETTINGS_DESC));
		userLayoutSettingsButton.addClickListener(event -> {
			AbstractTreeGridSettingsWindow settingsWindow = getSettingsWindow();
			settingsWindow.addOnSaveAndCloseListener(closeEvent -> {
				refreshUiElements();
			});
			getUI().getUI().addWindow(settingsWindow);
		});
		return userLayoutSettingsButton;
	}

	public void createBody() {
		body = new VerticalLayout();
		refreshUiElements();
		addComponent(body);
	}

	public void refreshUiElements() {
		body.removeAllComponents();
		
		if (isTreeVisible()) {
			HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();
			splitPanel.setFirstComponent(createTree());
			splitPanel.setSecondComponent(createGrid());
			splitPanel.setSplitPosition(getSplitPosition());
			body.addComponent(splitPanel);
		} else {
			body.addComponent(createGrid());
		}
	}
	
	public Component createTree() {
		treePanel = new Panel(getI18nText(I18N_TREE_CAPTION, messageSource));
		tree = new DraggableTree<>();
		
		refreshProjectTree();
		treePanel.setContent(tree);
		treePanel.setSizeFull();
		return treePanel;
	}
	
	@SuppressWarnings("rawtypes")
	public void refreshProjectTree() {
		AbstractRegistryDataProvider gridDataProvider = getGridDataProvider();
		
		tree.setDataProvider(getTreeDataProvider());
		// expand the tree and select the previously selected item
		if (gridDataProvider.getParentNode() != null) {
			NodeWrapper parentNode = gridDataProvider.getParentNode();
			tree.select(parentNode);
			
			while (parentNode.hasParent()) {
				tree.expand(parentNode);
				parentNode = parentNode.getParent();
			}
			tree.expand(parentNode);
		}
		
		tree.setItemCaptionGenerator(node -> node.getNodeCaption(this.treeDataService, this.messageSource));
		tree.setItemIconGenerator(new ProjectItemIconGenerator());
		tree.setHeight(WORKSET_GRID_ROW_HEIGHT * getGridRowsCount() + HEAD_ROW_HEIGHT - WORKSET_GRID_ROW_HEIGHT, Unit.PIXELS);
		if (Objects.nonNull(treeItemSelectRegistration)) {
			treeItemSelectRegistration.remove();
		}
		treeItemSelectRegistration = tree.addSelectionListener(listener -> {
			Optional<NodeWrapper> selected = listener.getFirstSelectedItem();
			if (selected.isPresent()) {
				gridDataProvider.setParentNode(selected.get());
				onTreeItemSelect();
			}
			gridDataProvider.refreshAll();
		});
	}
	
	public Component createGrid() {
		grid = new Grid<>();
		refreshWorksetGrid();
		return grid;
	}
	
	public void refreshWorksetGrid() {
		AbstractRegistryDataProvider<T, ?> gridDataProvider = (AbstractRegistryDataProvider<T, ?>)grid.getDataProvider();
		NodeWrapper parentNode = gridDataProvider.getParentNode();
		gridDataProvider = getGridDataProvider();
		gridDataProvider.setParentNode(parentNode);

		List<ColumnSettings> columnSettings = getSettings().getColumns();
		
		columnSettings.sort((cs1, cs2) -> 
			Integer.compare(cs1.getOrderIndex(), cs2.getOrderIndex())
		);
		
		grid.removeAllColumns();
		columnSettings.forEach(this::createGridColumn);
		grid.setSizeFull();
		grid.setColumnReorderingAllowed(true);
		grid.setHeightByRows(getGridRowsCount());
		gridDataProvider.setShownColumns(columnSettings);
		grid.setDataProvider(gridDataProvider);
		grid.getEditor().setEnabled(true);
		grid.getEditor().setSaveCaption(getI18nText(I18N_SAVE_ITEM_CAP, messageSource));
		grid.getEditor().setCancelCaption(getI18nText(I18N_CANCELSAVE_ITEM_CAP, messageSource));
		
		grid.getEditor().addSaveListener(saveEvent -> {
			saveGridItem(saveEvent.getBean());
		});
		grid.getEditor().addCancelListener(cancelSaveEvent -> {
			grid.getDataProvider().refreshItem(cancelSaveEvent.getBean());
		});
		
		/* Grid Item Select */
		grid.addSelectionListener(selectEvent -> {
			Optional<T> item = selectEvent.getFirstSelectedItem();
			if (item.isPresent()) {
				this.selectedGridItem = item.get();
				onGridItemSelect();
			}
		});
		
		/* select the previously selected grid item */
		if (selectedGridItem != null) {
			grid.select(selectedGridItem);
		}
		
		// test column headers
		getSettings().getHeaders();
		createGridHeaderColumns(getSettings());
	}
	
	public void createGridHeaderColumns(ISettingsJson settings) {
		if (settings.getHeaders() != null && !settings.getHeaders().isEmpty()) {
			refreshColumnHeaderGroups(settings.getHeaders());
		}
		grid.setHeightByRows(getGridRowsCount() - grid.getHeaderRowCount() + 1);
	}
	
	public void refreshColumnHeaderGroups(List<ColumnHeaderGroup> headers) {
		final String headStyle = "v-grid-header-align-left";
		Deque<List<ColumnHeaderGroup>> childGroups = new LinkedList<>();
		childGroups.push(headers);
		removePrepandedHeaderRows();
		
		HeaderRow headerRow = grid.prependHeaderRow();
		headerRow.setStyleName(headStyle);

		while (!childGroups.isEmpty()) {
			List<ColumnHeaderGroup> list = childGroups.pop();
			Iterator<ColumnHeaderGroup> it = list.iterator();
			
			while (it.hasNext()) {
				ColumnHeaderGroup g = it.next();

				if (g.hasChildrenGroups()) {
					HeaderRow subRow = grid.prependHeaderRow();
					subRow.setStyleName(headStyle);
					childGroups.push(g.getChildren());
					HeaderCell groupCell = subRow.join(getGridColumnIds(g.getChildren()));
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
		int count = grid.getHeaderRowCount();
		if (count > 1) {
			for (int i = 0; i < count - 1; i ++) {
				grid.removeHeaderRow(0);
			}
		}
	}
	
	public String[] getGridColumnIds(List<ColumnHeaderGroup> children) {
		Set<String> columnIds = new HashSet<>();
		
		for (int i = 0; i < children.size(); i ++) {
			Iterator<ColumnHeaderGroup> it = children.listIterator(i);
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
	
	public void onGridItemSelect() {
		handleAction(GRID_ITEM_SELECTED_ACTION);
	}
	
	public void onTreeItemSelect() {
		handleAction(TREE_ITEM_SELECTED_ACTION);
	}
	
	public void createFooter() {
		
	}

	public void addOnTreeItemSelect(Listener selectListener) {
		listeners.get(TREE_ITEM_SELECTED_ACTION).add(selectListener);
	}
	
	public void addOnGridItemSelect(Listener selectListener) {
		listeners.get(GRID_ITEM_SELECTED_ACTION).add(selectListener);
	}
	
	public void handleAction(Action action) {
		for (Listener listener : listeners.get(action)) {
			listener.componentEvent(new Event(this));
		}
	}
}
