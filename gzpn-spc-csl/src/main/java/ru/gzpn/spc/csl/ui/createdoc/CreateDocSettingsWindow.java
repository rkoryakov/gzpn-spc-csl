package ru.gzpn.spc.csl.ui.createdoc;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.MessageSource;

import com.vaadin.data.TreeData;
import com.vaadin.data.provider.TreeDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.grid.DropMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.GridDragSource;
import com.vaadin.ui.components.grid.GridDropTarget;
import com.vaadin.ui.components.grid.TreeGridDragSource;
import com.vaadin.ui.components.grid.TreeGridDropTarget;
import com.vaadin.ui.themes.ValoTheme;

import ru.gzpn.spc.csl.model.enums.Entities;
import ru.gzpn.spc.csl.model.jsontypes.ColumnHeaderGroup;
import ru.gzpn.spc.csl.model.jsontypes.ColumnSettings;
import ru.gzpn.spc.csl.model.jsontypes.CreateDocSettingsJson;
import ru.gzpn.spc.csl.model.utils.NodeWrapper;
import ru.gzpn.spc.csl.services.bl.interfaces.IUserSettigsService;
import ru.gzpn.spc.csl.ui.common.DraggableTree;
import ru.gzpn.spc.csl.ui.common.I18n;
import ru.gzpn.spc.csl.ui.common.UISettingsWindow;

public class CreateDocSettingsWindow extends UISettingsWindow {

	public static final int GRID_ROWS = 8;
	public static final int GRID_ROW_HEIGHT = 38;
	public static final Logger logger = LogManager.getLogger(CreateDocSettingsWindow.class);
	public static final String I18N_TREE_CAPTION = "createDocSettingsWindow.projectTree.caption";
	public static final String I18N_WINDOW_CAPTION = "createDocSettingsWindow.caption";
	public static final String I18N_COLUMNSTAB_CAPTION = "createDocSettingsWindow.tabSheet.columns.cap";
	public static final String I18N_HEADERSTAB_CAPTION = "createDocSettingsWindow.tabSheet.headers.cap";
	private static final String I18N_SETTINGS_ADD_HEADER_BUTTON = "createDocSettingsWindow.addButton.cap";
	
	private VerticalLayout leftLayout;
	private HorizontalSplitPanel splitPanel;
	private DraggableTree<NodeWrapper> projectTree;
	private List<NodeWrapper> draggedItems;
	private TreeDataProvider<NodeWrapper> treeDataProvider;
	private TreeData<NodeWrapper> treeData;
	private Grid<ColumnSettingsPresenter> columnsGrid;
	private List<ColumnSettingsPresenter> columnsGridData;
	private TabSheet tabSheet;
	private Grid<ColumnHeaderPresenter> headersGrid;
	private Button addButton;
	private List<ColumnHeaderPresenter> headersGridData;
	
	public CreateDocSettingsWindow(IUserSettigsService settingsService, MessageSource messageSource) {
		super(settingsService, messageSource);
		this.userSettings = settingsService.getUserSettings(user, new CreateDocSettingsJson());
		this.center();
		this.setCaption(getI18nText(I18N_WINDOW_CAPTION, messageSource));
		this.setModal(true);
		this.setWidth(70, Unit.PERCENTAGE);
		this.setHeight(60, Unit.PERCENTAGE);
	}

	@Override
	public ComponentContainer createBodyLayout() {
		VerticalLayout bodyLayout = new VerticalLayout();
		tabSheet = new TabSheet();
		
		VerticalLayout columnsSettings = new VerticalLayout();
		splitPanel = new HorizontalSplitPanel();
		splitPanel.setFirstComponent(createLeftColumnsSettings());
		splitPanel.setSecondComponent(createRightColumnsSettings());
		splitPanel.setSplitPosition(30);
		columnsSettings.addComponent(splitPanel);
		
		tabSheet.addTab(splitPanel, getI18nText(I18N_COLUMNSTAB_CAPTION, messageSource));
		tabSheet.addTab(createColumnHeadersSettings(), getI18nText(I18N_HEADERSTAB_CAPTION, messageSource));
		
		bodyLayout.addComponent(tabSheet);
		
		return bodyLayout;
	}

	private Component createColumnHeadersSettings() {
		VerticalLayout headersLayout = new VerticalLayout();
		
		headersLayout.setSizeFull();
		headersLayout.setMargin(true);
		headersLayout.setSpacing(true);
		
		refreshHeadersGrid();
		headersLayout.addComponent(createButtons());
		headersLayout.addComponent(headersGrid);
		headersGrid.setSizeFull();
		headersGrid.setHeightByRows(GRID_ROWS);
		
		return headersLayout;
	}

	private Component createButtons() {
		HorizontalLayout layout = new HorizontalLayout();
		addButton = new Button();
		addButton.setIcon(VaadinIcons.PLUS);
		addButton.setDescription(getI18nText(I18N_SETTINGS_ADD_HEADER_BUTTON, messageSource));
		layout.addComponent(addButton);
		
		addButton.addClickListener(clickEvent -> {
			CreateDocSettingsJson settingsJson = (CreateDocSettingsJson)settingsService
					.getUserSettings(user, new CreateDocSettingsJson());
			
			Collection<ColumnHeaderGroup> headers = toFlatSet(settingsJson.getLeftColumnHeaders());
			ColumnHeaderGroup newHeaderGroup = new ColumnHeaderGroup("Item " + (headers.size() + 1));
			ColumnHeaderPresenter newGridRow = new ColumnHeaderPresenter(newHeaderGroup, settingsService);
			
			this.headersGridData.add(newGridRow);
			this.headersGrid.setItems(headersGridData);
		});
		return layout;
	}

	public void refreshHeadersGrid() {
		headersGrid = new Grid<>();
		CreateDocSettingsJson settingsJson = (CreateDocSettingsJson)settingsService
				.getUserSettings(user, new CreateDocSettingsJson());
		
		List<ColumnHeaderGroup> headers = settingsJson.getLeftColumnHeaders();
		Set<ColumnHeaderGroup> flatHeaders = toFlatSet(headers);

		setColumnAttribures("id", headersGrid.addColumn(presenter -> presenter.getCaption()), I18N_SETTINGS_HEADERS_COLUMN_ID);
		setColumnAttribures("visibility", headersGrid.addComponentColumn(ColumnHeaderPresenter::getVisibilityCheckBox), I18N_SETTINGS_HEADERS_COLUMN_VISIBLE);
		setColumnAttribures("headers", headersGrid.addComponentColumn(ColumnHeaderPresenter::getMergedHeadComboBox), I18N_SETTINGS_HEADERS_COLUMN_HEAD);
		
		this.headersGridData = flatHeaders.stream().map(item -> {
			ColumnHeaderPresenter resultItem = new ColumnHeaderPresenter(item, settingsService);
			resultItem.setVisibilityCheckBox(cretateGridColumnHeaderVisibleCheckBox(resultItem));
			resultItem.setMergedHeadComboBox(cretateGridColumnHeaderMergedHeadComboBox(item, headers, flatHeaders));
			resultItem.setRemoveButton(cretateGridColumnHeaderRemoveButton(resultItem));
			return resultItem;
		}).collect(Collectors.toList());
		
		columnsGrid.setItems(this.columnsGridData);
	}

	private Set<ColumnHeaderGroup> toFlatSet(Collection<ColumnHeaderGroup> leftColumnHeaders) {
		Set<ColumnHeaderGroup> result = new HashSet<>();
		for (ColumnHeaderGroup g : leftColumnHeaders) {
			if (g.hasChildrenGroups()) {
				result.addAll(g.getChildren());
			}
		}
		return result;
	}

	public Component createLeftColumnsSettings() {
		leftLayout = new VerticalLayout();
		leftLayout.setMargin(false);
		leftLayout.setSpacing(false);
		leftLayout.setSizeFull();
		leftLayout.addComponent(createProjectTree());
		return leftLayout;
	}
	
	public Component createProjectTree() {
		projectTree = new DraggableTree<>();
		Panel panel = new Panel(getI18nText(I18N_TREE_CAPTION, messageSource));
		
		projectTree.setItemCaptionGenerator(item -> item.getNodeSettingsCaption(messageSource, getLocale()));
		projectTree.setItemIconGenerator(new ProjectItemIconGenerator());
		projectTree.setSizeFull();
		projectTree.setHeight(GRID_ROW_HEIGHT*(GRID_ROWS-1)+GRID_ROW_HEIGHT-1, Unit.PIXELS);
		refreshUiTreeData();
		
		TreeGridDragSource<NodeWrapper> dragSource = new TreeGridDragSource<>(projectTree.getCompositionRoot());
		TreeGridDropTarget<NodeWrapper> dropTarget = new TreeGridDropTarget<>(projectTree.getCompositionRoot(), DropMode.ON_TOP);
		
		dragSource.addGridDragStartListener(event -> {
			draggedItems = event.getDraggedItems();
		});
		
		dropTarget.addTreeGridDropListener(event -> {
			event.getDropTargetRow().ifPresent(this::putDraggedItemsAt);
		});
		
		panel.setContent(projectTree);
		panel.setSizeFull();
		
		return panel;
	}
	
	private void putDraggedItemsAt(NodeWrapper to) {
		if (CollectionUtils.isNotEmpty(draggedItems)) {
			NodeWrapper draggedNode = draggedItems.get(0);
			
			List<NodeWrapper> listNodes = new LinkedList<>();
			TreeData<NodeWrapper> newTreeData = new TreeData<>();
			NodeWrapper root = null;
			List<NodeWrapper> children = treeData.getChildren(root);
			
			while (CollectionUtils.isNotEmpty(children)) {
				listNodes.add(children.get(0));
				children = treeData.getChildren(children.get(0));
			}
			
			int pasteIndex = listNodes.indexOf(to);
			int fromIndex = listNodes.indexOf(draggedNode);
			listNodes.remove(draggedNode);
			
			// when we drag a node from top to bottom
			pasteIndex = (pasteIndex < 0 || pasteIndex > listNodes.size()) ? listNodes.size() : pasteIndex;
			
			listNodes.add(pasteIndex, draggedNode);
			ListIterator<NodeWrapper> it = listNodes.listIterator();
			while (it.hasNext()) {
				NodeWrapper node = it.next();
				node.setParent(root);
				
				if (Objects.nonNull(root)) {
					root.setChild(node);
				}
				newTreeData.addItem(root, node);
				root = node;
			}
			NodeWrapper lastNode = it.previous();
			lastNode.setChild(null);
			
			treeData = newTreeData;
			onRefreshUiTreeData(newTreeData);
		}
	}
	
	public Component createRightColumnsSettings() {
		VerticalLayout columnsLayout = new VerticalLayout();
		
		columnsLayout.setSizeFull();
		columnsLayout.setMargin(false);
		columnsLayout.setSpacing(false);
		
		refreshColumnsGrid();
		columnsLayout.addComponent(columnsGrid);
		columnsGrid.setSizeFull();
		columnsGrid.setHeightByRows(GRID_ROWS);
		
		return columnsLayout;
	}

	public void refreshColumnsGrid() {
		columnsGrid = new Grid<>();
		GridDragSource<ColumnSettingsPresenter> dragSource = new GridDragSource<>(columnsGrid);
		GridDropTarget<NodeWrapper> dropTarget = new GridDropTarget<>(projectTree.getCompositionRoot(), DropMode.ON_TOP);
		
		dragSource.addGridDragStartListener(dragEvent -> {
			List<ColumnSettingsPresenter> items = dragEvent.getDraggedItems();
			this.draggedItems = items.stream().map(item -> 
				new NodeWrapper(item.getEntityName(), item.getEntityFieldName().substring(item.getEntityName().length() + 1)))
					.collect(Collectors.toList());
		});
		
		CreateDocSettingsJson settingsJson = (CreateDocSettingsJson)settingsService
				.getUserSettings(user, new CreateDocSettingsJson());
		List<ColumnSettings> columns = settingsJson.getLeftResultColumns();

		setColumnAttribures("id", columnsGrid.addColumn(presenter -> presenter.getEntityFieldNameText(messageSource)), I18N_SETTINGS_COLUMN_ID);
		setColumnAttribures("entity", columnsGrid.addColumn(presenter -> presenter.getEntityNameText(messageSource)), I18N_SETTINGS_COLUMN_ENTITY);
		setColumnAttribures("visibility", columnsGrid.addComponentColumn(ColumnSettingsPresenter::getVisibilityCheckBox), I18N_SETTINGS_COLUMN_VISIBLE);
		setColumnAttribures("headers", columnsGrid.addComponentColumn(ColumnSettingsPresenter::getMergedHeadComboBox), I18N_SETTINGS_COLUMN_MERGEDHEAD);
		
		this.columnsGridData = columns.stream().map(item -> {
			ColumnSettingsPresenter resultItem = new ColumnSettingsPresenter(item, settingsService);
			resultItem.setVisibilityCheckBox(cretateGridColumnVisibleCheckBox(resultItem));
			resultItem.setMergedHeadComboBox(cretateGridColumnMergedHeadComboBox(item));
			return resultItem;
		}).collect(Collectors.toList());
		
		columnsGrid.setItems(this.columnsGridData);
	}
	
	public <C, T> void setColumnAttribures(String id, Column<C, T> column, String i18nCaption) {
		column.setSortProperty(id);
		column.setSortable(true);
		column.setCaption(getI18nText(i18nCaption, messageSource));
		column.setId(id);
	}
	
	public CheckBox cretateGridColumnVisibleCheckBox(ColumnSettings item) {
		CheckBox checkBox = new CheckBox();
		checkBox.setValue(item.isShown());
		
		checkBox.addValueChangeListener(changeEvent -> {
			int index = columnsGridData.indexOf(item);
			columnsGridData.get(index).setShown(changeEvent.getValue());
			columnsGrid.setItems(columnsGridData);
		});
		return checkBox;
	}
	
	public CheckBox cretateGridColumnHeaderVisibleCheckBox(ColumnHeaderGroup item) {
		CheckBox checkBox = new CheckBox();
		checkBox.setValue(item.isShown());
		
		checkBox.addValueChangeListener(changeEvent -> {
			int index = headersGridData.indexOf(item);
			headersGridData.get(index).setShown(changeEvent.getValue());
			headersGrid.setItems(headersGridData);
		});
		return checkBox;
	}
	
	private Button cretateGridColumnHeaderRemoveButton(ColumnHeaderPresenter item) {
		Button removeButton = new Button(VaadinIcons.TRASH);
		removeButton.setStyleName(ValoTheme.BUTTON_BORDERLESS);
		removeButton.addClickListener(clickEvent -> clickEvent.getSource());
		return removeButton;
	}
	
	public ComboBox<String> cretateGridColumnMergedHeadComboBox(ColumnSettings item) {
		ComboBox<String> mergedHeadComboBox = new ComboBox<>();
		ColumnHeaderGroup mergedHeadGroup = null;
		CreateDocSettingsJson settingsJson = (CreateDocSettingsJson)settingsService
				.getUserSettings(settingsService.getCurrentUser(), new CreateDocSettingsJson());
		
		List<ColumnHeaderGroup> headers = settingsJson.getLeftColumnHeaders();
		ListIterator<ColumnHeaderGroup> it = headers.listIterator();
		mergedHeadComboBox.setItems(headers.stream().map(element -> element.getCaption()));
		
		while (it.hasNext()) {
			ColumnHeaderGroup headGroup = it.next();
			if (headGroup.hasChildrenGroups()) {
				it = headGroup.getChildren().listIterator();
			} else if (headGroup.hasColumns()
					&& headGroup.getColumns().stream().anyMatch(element -> element.equals(item))) {
					mergedHeadComboBox.setSelectedItem(headGroup.getCaption());
					mergedHeadGroup = headGroup;
					break;
			}
		}
		
		return mergedHeadComboBox;
	}
	
	public ComboBox<String> cretateGridColumnHeaderMergedHeadComboBox(ColumnHeaderGroup item, List<ColumnHeaderGroup> headers, Set<ColumnHeaderGroup> flatHeaders) {
		ComboBox<String> mergedHeadComboBox = new ComboBox<>();
//		CreateDocSettingsJson settingsJson = (CreateDocSettingsJson)settingsService
//				.getUserSettings(settingsService.getCurrentUser(), new CreateDocSettingsJson());
//		
//		List<ColumnHeaderGroup> headers = settingsJson.getLeftColumnHeaders();
//		Set<ColumnHeaderGroup> flatHeaders = toFlatSet(headers);
		mergedHeadComboBox.setItems(flatHeaders.stream().map(element -> element.getCaption()));
		mergedHeadComboBox.setSelectedItem(getParentHeader(headers, item).getCaption());
		
		return mergedHeadComboBox;
	}
	
	private ColumnHeaderGroup getParentHeader(List<ColumnHeaderGroup> headers, ColumnHeaderGroup item) {
		ColumnHeaderGroup result = null;
		for (ColumnHeaderGroup header : headers) {
			if (header.hasChildrenGroups() && header.getChildren().contains(item)) {
				result = header;
				break;
			} else if (header.hasChildrenGroups()) {
				getParentHeader(header.getChildren(), item);
			}
		}
		return result;
	}
	
	@Override
	public void refreshUiElements() {
		refreshUiTreeData();
		refreshColumnsGrid();
		//refreshHeadersGrid();
	}

	public void refreshUiTreeData() {
		CreateDocSettingsJson settingsJson = (CreateDocSettingsJson)settingsService
									.getUserSettings(user, new CreateDocSettingsJson());
		NodeWrapper rootNode = settingsJson.getLeftTreeGroup();
		treeData = new TreeData<>();
		treeData.addItem(null, rootNode);
		
		while (rootNode.hasChild()) {
			treeData.addItem(rootNode, rootNode.getChild());
			rootNode = rootNode.getChild();
		}
		
		onRefreshUiTreeData(treeData);
	}
	
	public void onRefreshUiTreeData(TreeData<NodeWrapper> treeData) {
		treeDataProvider = new TreeDataProvider<>(treeData);
		projectTree.setDataProvider(treeDataProvider);
		projectTree.expandRecursively(treeData.getRootItems(), 10);
	}

	@Override
	public void refreshSettings() {
		NodeWrapper next = treeData.getRootItems().get(0);
		NodeWrapper result = next;
		result.setParent(null);
		
		while (!treeData.getChildren(next).isEmpty()) {
			NodeWrapper child = treeData.getChildren(next).get(0);
			child.setParent(next);
			next.setChild(child);
			next = child;
		}
		
		((CreateDocSettingsJson)this.userSettings).setLeftTreeGroup(result);
		((CreateDocSettingsJson)this.userSettings)
			.setLeftResultColumns(this.columnsGridData.stream()
						.map(item -> {
							ColumnSettings newItem  = new ColumnSettings();
							newItem.setEntityFieldName(item.getEntityFieldName());
							newItem.setEntityName(item.getEntityName());
							newItem.setOrderIndex(item.getOrderIndex());
							newItem.setShown(item.isShown());
							newItem.setWidth(item.getWidth());
						
							return newItem;
						}).collect(Collectors.toList()));
		
		((CreateDocSettingsJson)this.userSettings)
			.setLeftColumnHeaders(this.headersGridData.stream()
					.map(item -> {
						ColumnHeaderGroup newItem = new ColumnHeaderGroup();
						newItem.setCaption(item.getCaption());
						newItem.setChildren(item.getChildren());
						newItem.setColumns(item.getColumns());
						newItem.setShown(item.isShown());
						
						return newItem;
					}).collect(Collectors.toList()));
	}
	

	@SuppressWarnings("serial")
	public static class ColumnSettingsPresenter extends ColumnSettings implements I18n {
		CheckBox visibilityCheckBox;
		ComboBox<String> mergedHeadComboBox;
		IUserSettigsService settingsService;
		
		public ColumnSettingsPresenter(ColumnSettings columnSettings, IUserSettigsService settingsService) {
			this.setWidth(columnSettings.getWidth());
			this.setEntityFieldName(columnSettings.getEntityFieldName());
			this.setEntityName(columnSettings.getEntityName());
			this.setOrderIndex(columnSettings.getOrderIndex());
			this.setShown(columnSettings.isShown());
			this.settingsService = settingsService;
		}
		
		public Object getEntityFieldNameText(MessageSource messageSource) {
			return Entities.getEntityFieldText(this.getEntityFieldName(), messageSource, getLocale());
		}

		public String getEntityNameText(MessageSource messageSource) {
			Entities entity = Entities.valueOf(this.getEntityName().toUpperCase());
			return entity.getEntityText(messageSource, getLocale());
		}

		public CheckBox getVisibilityCheckBox() {
			return visibilityCheckBox;
		}
		public void setVisibilityCheckBox(CheckBox visibilityCheckBox) {
			this.visibilityCheckBox = visibilityCheckBox;
		}
		public ComboBox<String> getMergedHeadComboBox() {
			return mergedHeadComboBox;
		}
		public void setMergedHeadComboBox(ComboBox<String> mergedHeadComboBox) {
			this.mergedHeadComboBox = mergedHeadComboBox;
		}
	}
	
	@SuppressWarnings("serial")
	public static class ColumnHeaderPresenter extends ColumnHeaderGroup implements I18n {
		CheckBox visibilityCheckBox;
		ComboBox<String> mergedHeadComboBox;
		Button removeButton;
		IUserSettigsService settingsService;
		
		public ColumnHeaderPresenter(ColumnHeaderGroup item, IUserSettigsService settingsService) {
			this.setCaption(item.getCaption());
			this.setShown(item.isShown());
			this.setChildren(item.getChildren());
			this.setColumns(item.getColumns());
			this.settingsService = settingsService;
		}

		
		public CheckBox getVisibilityCheckBox() {
			return visibilityCheckBox;
		}

		public void setVisibilityCheckBox(CheckBox visibilityCheckBox) {
			this.visibilityCheckBox = visibilityCheckBox;
		}

		public ComboBox<String> getMergedHeadComboBox() {
			return mergedHeadComboBox;
		}

		public void setMergedHeadComboBox(ComboBox<String> mergedHeadComboBox) {
			this.mergedHeadComboBox = mergedHeadComboBox;
		}

		public Button getRemoveButton() {
			return removeButton;
		}

		public void setRemoveButton(Button removeButton) {
			this.removeButton = removeButton;
		}

		public IUserSettigsService getSettingsService() {
			return settingsService;
		}

		public void setSettingsService(IUserSettigsService settingsService) {
			this.settingsService = settingsService;
		}
	
	}
}
